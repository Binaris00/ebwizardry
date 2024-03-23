package binaris.ebwizardry.config;

import binaris.ebwizardry.constant.Element;
import binaris.ebwizardry.constant.SpellType;
import binaris.ebwizardry.constant.Tier;
import binaris.ebwizardry.spell.Spell;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
// FIXME: Not a error, but JSONObject print a lot of warnings because use raw types,
//  maybe remove this in any new version
@SuppressWarnings("unchecked")
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
    /** A map storing custom values for this spell.
     * E.g., range, damage, entities, etc. */
    private final Map<String, Object> base_properties;

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
        this.base_properties = new HashMap<>();

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
                JSONObject json = new JSONObject();

                // Create a map of enabled contexts with all contexts enabled by default
                for (Context context : Context.values()) {
                    enabledContexts.put(context, true);
                }
                json.put("enabled", enabledContexts);

                // Put the normal values into the JSON
                HashMap<String, Object> properties = new HashMap<>();
                properties.put("tier", tier.name());
                properties.put("element", element.name());
                properties.put("type", type.name());
                properties.put("cost", cost);
                properties.put("chargeup", chargeup);
                properties.put("cooldown", cooldown);

                json.put("properties", properties);

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

            // Read the enabled contexts
            JSONObject enabled = (JSONObject) json.get("enabled");
            for (Context context : Context.values()) {
                enabledContexts.put(context, (Boolean) enabled.get(context.name));
            }

            // Read the normal values
            HashMap<String, Object> properties = (HashMap<String, Object>) json.get("properties");
            this.properties.put("tier", properties.get("tier"));
            this.properties.put("element", properties.get("element"));
            this.properties.put("type", properties.get("type"));
            this.properties.put("cost", properties.get("cost"));
            this.properties.put("chargeup", properties.get("chargeup"));
            this.properties.put("cooldown", properties.get("cooldown"));

            // Read the base values
            if(json.containsKey("base_properties")){
                JSONObject base = (JSONObject) json.get("base_properties");
                base.forEach((key, value) -> base_properties.put((String) key, value));
            }
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
        if(!base_properties.containsKey(key)){
            throw new IllegalArgumentException("Base value with key '" + key + "' is not defined.");
        }
        return base_properties.get(key);
    }

    /**
     * Returns whether a base value was defined with the given identifier.
     * @param key The string identifier to check for.
     * @return True if a base value was defined with the given identifier, false otherwise.
     */
    public boolean hasProperties(String key){
            return base_properties.containsKey(key);
    }


    public void addMoreProperties(Spell spell, String key, Object value){
        File file = new File("config/ebwizardry/spells/" + spell.getSpellName() + ".json");
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(new FileReader(file));

            // if not exists, create a list in the json file for custom values
            if(!json.containsKey("base_properties")){
                json.put("base_properties", new HashMap<String, Object>());
            }

            HashMap<String, Object> base_properties = (HashMap<String, Object>) json.get("base_properties");
            base_properties.put(key, value);
            json.put("base_properties", base_properties);

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(json));
            writer.close();
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
