package binaris.ebwizardry.spell;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.config.SpellProperties;
import binaris.ebwizardry.constant.Element;
import binaris.ebwizardry.constant.SpellType;
import binaris.ebwizardry.constant.Tier;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;

import java.util.HashSet;
import java.util.Set;


public class Spell {
    // ========================================= Spell properties Keys ===============================================
    // Spell property keys. These are public constants so that they can be accessed from anywhere, they are
    // effectively immutable and should not be changed.
    public static final String DAMAGE = "damage";
    public static final String RANGE = "range";
    public static final String DURATION = "duration";
    public static final String EFFECT_RADIUS = "effect_radius";
    public static final String BLAST_RADIUS = "blast_radius";
    public static final String EFFECT_DURATION = "effect_duration";
    public static final String EFFECT_STRENGTH = "effect_strength";
    public static final String BURN_DURATION = "burn_duration";
    public static final String DIRECT_DAMAGE = "direct_damage";
    public static final String SPLASH_DAMAGE = "splash_damage";
    public static final String HEALTH = "health";
    public static final String SEEKING_STRENGTH = "seeking_strength";
    public static final String DIRECT_EFFECT_DURATION = "direct_effect_duration";
    public static final String DIRECT_EFFECT_STRENGTH = "direct_effect_strength";
    public static final String SPLASH_EFFECT_DURATION = "splash_effect_duration";
    public static final String SPLASH_EFFECT_STRENGTH = "splash_effect_strength";

    public static final String TIER_MATCH_PREFIX = "tier";
    public static final String ELEMENT_MATCH_PREFIX = "element";
    public static final String TYPE_MATCH_PREFIX = "type";
    public static final String DISCOVERED_MATCH_PREFIX = "discovered";
    public static final String MODID_MATCH_PREFIX = "modid";

    public static final String TIER_MATCH_ALIAS = "t";
    public static final String ELEMENT_MATCH_ALIAS = "e";
    public static final String TYPE_MATCH_ALIAS = "p";
    public static final String DISCOVERED_MATCH_ALIAS = "d";
    public static final String MODID_MATCH_ALIAS = "m";

    public static final String MATCH_CONDITION_SEPARATOR = ";";
    public static final String MATCH_KEY_VALUE_SEPARATOR = "=";
    public static final String MATCH_VALUE_SEPARATOR = ",";
    private final String name;
    /** The action the player does when this spell is cast. */
    public final UseAction action;
    /** Whether or not the spell is continuous (keeps going as long as the mouse button is held) */
    public final boolean isContinuous;

    /** ResourceLocation of the spell icon. */
    private final Identifier icon;
    private static int nextSpellId = 0;
    public int id;
    /** False if the spell has been disabled in the config file, true otherwise. This is now encapsulated to stop it
     * being fiddled with. */
    private boolean enabled = true;

    // The spell properties system turned out to be a bit of a pain. Ideally I'd attach them to a world or whatever, but
    // often we're accessing spell properties from places where a world is not available (and besides, I don't want to
    // change the API now). Since spell properties are immutable and don't get reassigned while a world is loaded, in
    // theory that makes them thread-safe (at least while the world is open, and they get wiped after that anyway).
    // We still need to send them to clients in multiplayer (and LAN guests) so the simplest thing to do is send them
    // to players' clients and only assign them if they haven't already been assigned, but without printing a warning.
    // Yes, *technically* this is reaching across sides but the fields are effectively immutable so it doesn't matter.
    /** This spell's associated SpellProperties object. */
    private SpellProperties properties;

    /** Used in initialisation. */
    private Set<String> propertyKeys = new HashSet<>();

    /**
     * This constructor should be called from any subclasses, either feeding in the constants directly or through their
     * own constructor from wherever the spell is registered. This is the constructor for wizardry's own spells; spells
     * added by other mods should use
     * {@link Spell#Spell(String, String, UseAction, boolean)}.
     * @param name The <i>registry name</i> of the spell. This will also be the name of the icon file. The spell's
     *        unlocalised name will be a resource location with the format [modid]:[name].
     * @param action The vanilla usage action to be displayed when casting this spell.
     * @param isContinuous Whether this spell is continuous, meaning you cast it for a length of time by holding the
     *                     use item button.
     */
    public Spell(String name, UseAction action, boolean isContinuous){
        this(Wizardry.MODID, name, action, isContinuous);
    }


    public Spell(String modId, String name, UseAction action, boolean isContinuous){
        this.name = name;
        this.action = action;
        this.isContinuous = isContinuous;
        this.icon = new Identifier(modId, "textures/spells/" + name + ".png");
        this.id = nextSpellId++;
        Wizardry.LOGGER.debug("Spells: Loading %s With ID: %d".formatted(name, id));
    }

    /** Returns the {@code Identifier} for this spell's icon. */
    public final Identifier getIcon(){
        return icon;
    }
    public String getName(){return name;}

    public int getId() {return id;}
    /** Called from {@code init()} in the main mod class. Used to initialise spell fields and properties that depend on
     * other things being registered (e.g. potions). <i>Always initialise things in the constructor wherever possible.</i> */
    public void init(){}




    /** Returns true if this spell's properties have been initialised, false if not. Check this if you're attempting
     * to access them from code that could be called before wizardry's {@code init()} method (e.g. item attributes). */
    public final boolean arePropertiesInitialised(){
        return properties != null;
    }

    /** Internal, do not use. */
    public final String[] getPropertyKeys(){
        return propertyKeys.toArray(new String[0]);
    }

    // ============================================= Spell properties ===============================================
    public Spell createProperties(Tier tier, Element element, SpellType type, int cost, int chargeup, int cooldown){
        this.properties = new SpellProperties(this, tier, element, type, cost, chargeup, cooldown);
        return this;
    }

    public Spell addProperties(String key, Object value){
        this.properties.addMoreProperties(this, key, value);
        return this;
    }


    public String getStringProperty(String key){
        if(!properties.hasProperties(key)){
            throw new IllegalArgumentException("Error getting property with key '" + key + "' from spell '" + this.name + "': No such property exists.");
        }
        return (String) properties.getProperties(key);
    }

    public int getIntProperty(String key){
        if(!properties.hasProperties(key)){
            throw new IllegalArgumentException("Error getting property with key '" + key + "' from spell '" + this.name + "': No such property exists.");
        }
        return Integer.parseInt(properties.getProperties(key).toString());

    }
    public boolean getBooleanProperty(String key){
        if(!properties.hasProperties(key)){
            throw new IllegalArgumentException("Error getting property with key '" + key + "' from spell '" + this.name + "': No such property exists.");
        }
        return (Boolean) properties.getProperties(key);
    }

    public float getFloatProperty(String key){
        if(!properties.hasProperties(key)){
            throw new IllegalArgumentException("Error getting property with key '" + key + "' from spell '" + this.name + "': No such property exists.");
        }
        return Float.parseFloat(properties.getProperties(key).toString());
    }


    // ============================================= Misc methods ===============================================
    /** Returns whether the spell is enabled in any of the given {@link SpellProperties.Context Context}s.
     * A spell may be disabled globally in the config, or it may be disabled for one or more specific contexts in
     * its JSON file using a resource pack. If called with no arguments, defaults to any context, i.e. only returns
     * false if the spell is completely disabled in all contexts. */
    public final boolean isEnabled(SpellProperties.Context... contexts){
        return enabled && (contexts.length == 0 || properties.isEnabled(contexts));
    }

    /** Sets whether the spell is enabled or not. */
    public final void setEnabled(boolean isEnabled){
        this.enabled = isEnabled;
    }
}
