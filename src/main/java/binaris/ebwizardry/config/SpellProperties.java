package binaris.ebwizardry.config;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.constant.Element;
import binaris.ebwizardry.constant.SpellType;
import binaris.ebwizardry.constant.Tier;
import binaris.ebwizardry.spell.Spell;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.WorldSavePath;
import net.minecraft.world.World;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpellProperties {

    private static final Gson gson = new Gson();

    /** Set of enum constants representing contexts in which a spell can be enabled/disabled. */
    public enum Context {

        /** Disabling this context will make a spell's book unobtainable and unusable. */			BOOK("book"),
        /** Disabling this context will make a spell's scroll unobtainable and unusable. */			SCROLL("scroll"),
        /** Disabling this context will prevent a spell from being cast using a wand. */			WANDS("wands"),
        /** Disabling this context will prevent NPCs from casting or dropping a spell. */			NPCS("npcs"),
        /** Disabling this context will prevent dispensers from casting a spell. */					DISPENSERS("dispensers"),
        /** Disabling this context will prevent a spell from being cast using commands. */			COMMANDS("commands"),
        /** Disabling this context will prevent a spell's book or scroll generating in chests. */ 	TREASURE("treasure"),
        /** Disabling this context will prevent a spell's book or scroll from being sold by NPCs.*/ TRADES("trades"),
        /** Disabling this context will prevent a spell's book or scroll being dropped by mobs. */	LOOTING("looting");

        /** The JSON identifier for this context. */
        public final String name;

        Context(String name){
            this.name = name;
        }
    }

    /** A map storing whether each context is enabled for this spell. */
    private final Map<Context, Boolean> enabledContexts;
    /** A map storing the base values for this spell. These values are defined by the spell class and cannot be
     * changed. */
    // We're using Number here because it makes implementors think about what they convert it to.
    // If we did what attributes do and just use doubles, people (myself included!) might plug them into calculations
    // without thinking. However, with Number you can't just do that, you have to convert and therefore you have to
    // decide how to do the conversion. Internally they're handled as floats though.
    private final Map<String, Number> baseValues;

    /** The tier this spell belongs to. */
    public final Tier tier;
    /** The element this spell belongs to. */
    public final Element element;
    /** The type of spell this is classified as. */
    public final SpellType type;
    /** Mana cost of the spell. If it is a continuous spell the cost is per second. */
    public final int cost;
    /** The charge-up time of the spell, in ticks. */
    public final int chargeup;
    /** The cooldown time of the spell, in ticks. */
    public final int cooldown;


    // Sometimes it just makes more sense to do the JSON parsing in the constructor
    // It's the only way we're gonna keep the fields final!
    /**
     * Parses the given JSON object and constructs a new {@code SpellProperties} from it, setting all the relevant
     * fields and references.
     *
     * @param json A JSON object representing the spell properties to be constructed.
     * @param spell The spell that this {@code SpellProperties} object is for.
     * @throws JsonSyntaxException if at any point the JSON object is found to be invalid.
     */
    private SpellProperties(JsonObject json, Spell spell){
        String[] baseValueNames = spell.getPropertyKeys();

        enabledContexts = new EnumMap<>(Context.class);
        baseValues = new HashMap<>();

        JsonObject enabled = JsonHelper.getObject(json, "enabled");


        // This time we know the exact set of properties so we can iterate over them instead of the json object
        // In fact, we actually want to throw an exception if any of them are missing
        for(Context context : Context.values()){
            enabledContexts.put(context, JsonHelper.getBoolean(enabled, context.name));
        }

        try {
            tier = Tier.fromName(JsonHelper.getString(json, "tier"));
            element = Element.fromName(JsonHelper.getString(json, "element"));
            type = SpellType.fromName(JsonHelper.getString(json, "type"));
        }catch(IllegalArgumentException e){
            throw new JsonSyntaxException("Incorrect spell property value", e);
        }

        cost = JsonHelper.getInt(json, "cost");
        chargeup = JsonHelper.getInt(json, "chargeup");
        cooldown = JsonHelper.getInt(json, "cooldown");

        // There's not much point specifying the classes of the numbers here because the json getter methods just
        // perform conversion to the requested type anyway. It therefore makes very little difference whether the
        // conversion is done during JSON parsing or when we actually use the value - and at least in the latter case,
        // individual subclasses have control over how it is converted.

        // My case in point: summoning 2.5 spiders is obviously nonsense, but what happens when we cast that with a
        // modifier of 2? Should we round the base value down to 2 and then apply the x2 modifier to get 4 spiders?
        // Should we round it up instead? Or should we apply the modifier first and then do the rounding, so with no
        // modifier we still get 2 spiders but with the x2 modifier we get 5?
        // The most pragmatic solution is to let the spell class decide for itself.
        // (Of course, we can only hope that the users aren't jerks and don't try to summon 2 and a half spiders...)

        JsonObject baseValueObject = JsonHelper.getObject(json, "base_properties");

        // If the code requests more values than the JSON file contains, that will cause a JsonSyntaxException here anyway.
        // If there are redundant values in the JSON file, chances are that a user has misunderstood the system and tried
        // to add properties that aren't implemented. However, redundant values will also be found if a programmer has
        // forgotten to call addProperties in their spell constructor (I know I have!), potentially causing a crash at
        // some random point in the future. Since redundant values aren't a problem by themselves, we shouldn't throw an
        // exception, but a warning is appropriate.

        int redundantKeys = baseValueObject.size() - baseValueNames.length;
        if(redundantKeys > 0) Wizardry.LOGGER.warn("Spell " + spell.getName() + " has " + redundantKeys +
                " redundant spell property key(s) defined in its JSON file. Extra values will have no effect! (Modders:" +
                " make sure you have called addProperties(...) during spell construction)");

        if(baseValueNames.length > 0){

            for(String baseValueName : baseValueNames){
                baseValues.put(baseValueName, JsonHelper.getFloat(baseValueObject, baseValueName));
            }
        }

    }



    /** Constructs a new SpellProperties object for the given spell, reading its values from the given ByteBuf. */
    public SpellProperties(Spell spell, ByteBuf buf){

        enabledContexts = new EnumMap<>(Context.class);
        baseValues = new HashMap<>();

        for(Context context : Context.values()){
            // Enum maps have a guaranteed iteration order so this works fine
            enabledContexts.put(context, buf.readBoolean());
        }

        tier = Tier.values()[buf.readShort()];
        element = Element.values()[buf.readShort()];
        type = SpellType.values()[buf.readShort()];

        cost = buf.readInt();
        chargeup = buf.readInt();
        cooldown = buf.readInt();

        List<String> keys = Arrays.asList(spell.getPropertyKeys());
        Collections.sort(keys); // Should be the same list of keys in the same order they were written to the ByteBuf

        for(String key : keys){
            baseValues.put(key, buf.readFloat());
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

    /**
     * Returns whether a base value was defined with the given identifier.
     * @param identifier The string identifier to check for.
     * @return True if a base value was defined with the given identifier, false otherwise.
     */
    public boolean hasBaseValue(String identifier){
        return baseValues.containsKey(identifier);
    }

    /**
     * Returns the base value for this spell that corresponds to the given identifier. To check whether an identifier
     * exists, use {@link SpellProperties#hasBaseValue(String)}.
     * @param identifier The string identifier to fetch the base value for.
     * @return The base value, as a {@code Number}.
     * @throws IllegalArgumentException if no base value was defined with the given identifier.
     */
    // Better to throw an exception than make this nullable because the vast majority of uses are for retrieving
    // specific spells' properties that are known to exist, and IntelliJ would scream at us for not checking
    public Number getBaseValue(String identifier){
        if(!baseValues.containsKey(identifier)){
            throw new IllegalArgumentException("Base value with identifier '" + identifier + "' is not defined.");
        }
        return baseValues.get(identifier);
    }










    public void setBaseValues(Spell spell, MinecraftServer server, World world){
        File file = new File(getPropertiesFile(spell, server));
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getPropertiesFile(Spell spell, MinecraftServer server){
        return "%s./data/spells/%s.json".formatted(server.getSavePath(WorldSavePath.ROOT).toString(), spell.getName());
    }
}
