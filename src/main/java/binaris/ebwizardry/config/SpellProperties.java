package binaris.ebwizardry.config;

import binaris.ebwizardry.constant.Element;
import binaris.ebwizardry.constant.SpellType;
import binaris.ebwizardry.constant.Tier;
import binaris.ebwizardry.spell.Spell;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SpellProperties {

    /** Set of enum constants representing contexts in which a spell can be enabled/disabled. */
    public enum Context {

        /** Disabling this context will make a spell's book unobtainable and unusable. */			book("book"),
        /** Disabling this context will make a spell's scroll unobtainable and unusable. */			scroll("scroll"),
        /** Disabling this context will prevent a spell from being cast using a wand. */			wands("wands"),
        /** Disabling this context will prevent NPCs from casting or dropping a spell. */			npcs("npcs"),
        /** Disabling this context will prevent dispensers from casting a spell. */					dispensers("dispensers"),
        /** Disabling this context will prevent a spell from being cast using commands. */			commands("commands"),
        /** Disabling this context will prevent a spell's book or scroll generating in chests. */ 	treasure("treasure"),
        /** Disabling this context will prevent a spell's book or scroll from being sold by NPCs.*/ trades("trades"),
        /** Disabling this context will prevent a spell's book or scroll being dropped by mobs. */	looting("looting");

        /** The JSON identifier for this context. */
        public final String name;

        Context(String name){
            this.name = name;
        }
    }

    /** A map storing whether each context is enabled for this spell. */
    private final Map<Context, Boolean> enabledContexts;

    /** A map storing the base values for this spell. */
    private final Map<String, Object> properties;

    /** The tier this spell belongs to. */
    private final Tier tier;
    /** The element this spell belongs to. */
    private final Element element;
    /** The type of spell this is classified as. */
    private final SpellType type;
    /** Mana cost of the spell. If it is a continuous spell the cost is per second. */
    private final int cost;
    /** The charge-up time of the spell, in ticks. */
    private final int chargeup;
    /** The cooldown time of the spell, in ticks. */
    private final int cooldown;

    /**
     * This creates a JSON file for the spell if it doesn't already exist.
     * Also creates a SpellProperties object for the spell.
     *
     * @param spell The spell this SpellProperties object is for.
     *              This is needed to get the name of the spell, which is used to create the JSON file.
     * @param tier The tier of the spell.
     * @param element The element of the spell.
     * @param type The type of the spell.
     * @param cost The mana cost of the spell.
     * @param chargeup The chargeup time of the spell.
     * @param cooldown The cooldown time of the spell.
     * **/
    public SpellProperties(Spell spell, Tier tier, Element element, SpellType type, int cost, int chargeup, int cooldown) {
        this.tier = tier;
        this.element = element;
        this.type = type;
        this.cost = cost;
        this.chargeup = chargeup;
        this.cooldown = cooldown;
        this.enabledContexts = new EnumMap<>(Context.class);
        this.properties = new HashMap<>();

        // Create the directory if it doesn't already exist
        File path = new File("config/ebwizardry/spells/");
        if (!path.exists()) {
            path.mkdirs();
        }

        // Create the JSON file if it doesn't already exist
        File file = new File("config/ebwizardry/spells/" + spell.getSpellName() + ".json");
        try {
            if (!file.exists()) {
                file.createNewFile();
                JsonObject json = new JsonObject();
                json.addProperty("tier", tier.name());
                json.addProperty("element", element.name());
                json.addProperty("type", type.name());
                json.addProperty("cost", cost);
                json.addProperty("chargeup", chargeup);
                json.addProperty("cooldown", cooldown);

                for (Context context : Context.values()) {
                    json.addProperty(context.name(), true);
                }

                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();
                FileWriter writer = new FileWriter(file);
                writer.write(gson.toJson(json));
                writer.close();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Read the JSON and put all the values into the maps
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(new FileReader(file));

            for (Context context : Context.values()) {
                enabledContexts.put(context, (boolean) json.get(context.name()));
            }

            this.properties.put("tier", json.get("tier"));
            this.properties.put("element", json.get("element"));
            this.properties.put("type", json.get("type"));
            this.properties.put("cost", json.get("cost"));
            this.properties.put("chargeup", json.get("chargeup"));
            this.properties.put("cooldown", json.get("cooldown"));

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the base value for this spell that corresponds to the given key. To check whether an key
     * exists, use {@link SpellProperties#hasProperties(String)} (String)}.
     *
     * @param key The string key to fetch the base value for.
     * @return The base value, as a {@code String}.
     * @throws IllegalArgumentException if no base value was defined with the given key.
     */
    public Object getProperties(String key){
        if(!properties.containsKey(key)){
            throw new IllegalArgumentException("Base value with key '" + key + "' is not defined.");
        }
        return properties.get(key);
    }

    /**
     * Returns whether a base value was defined with the given identifier.
     * @param key The string identifier to check for.
     * @return True if a base value was defined with the given identifier, false otherwise.
     */
    public boolean hasProperties(String key){
            return properties.containsKey(key);
    }


    public void addMoreProperties(Spell spell, String key, Object value){
        File file = new File("config/ebwizardry/spells/" + spell.getSpellName() + ".json");
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(new FileReader(file));

            // If the key already exists, don't do anything
            if(!json.containsKey(key)){
                json.put(key, value);

                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();
                FileWriter writer = new FileWriter(file);
                writer.write(gson.toJson(json));
                writer.close();
            }

            // Add the key to the map from the json file
            properties.put(key, json.get(key));
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Failed to add custom property '" + key + "' to spell " + spell.getSpellName());
        }
    }

    /**
     * Returns whether the spell is enabled in any of the given contexts.
     * @param contexts The context in which to check if the spell is enabled.
     * @return True if the spell is enabled in any of the given contexts, false if not.
     */
    public boolean isEnabled(Context... contexts){
        return enabledContexts.entrySet().stream().anyMatch(e -> e.getValue() && Arrays.asList(contexts).contains(e.getKey()));
    }

    // -------------------------------- Getters --------------------------------
    public Tier getTier() {
        return tier;
    }

    public Element getElement() {
        return element;
    }

    public SpellType getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public int getChargeup() {
        return chargeup;
    }

    public int getCooldown() {
        return cooldown;
    }
}
