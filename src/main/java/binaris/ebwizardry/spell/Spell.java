package binaris.ebwizardry.spell;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.config.SpellProperties;
import binaris.ebwizardry.constant.Element;
import binaris.ebwizardry.constant.SpellType;
import binaris.ebwizardry.constant.Tier;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class Spell {
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
    /** A reference to the global spell properties for this spell, so they are only loaded once. */
    private SpellProperties globalProperties;
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

    /**
     * Adds the given JSON identifiers to the configurable base properties of this spell. This should be called from
     * the constructor or {@link Spell#init()}. <i>It is highly recommended that property keys be defined as constants,
     * as they will be needed later to retrieve the properties during the casting methods.</i>
     * <p></p>
     * General spell classes will call this method to set any properties they require in order to work properly, and
     * the relevant keys will be public constants.
     * @param keys One or more spell property keys to add to the spell. By convention, these are lowercase_with_underscores.
     *             If any of these already exists, a warning will be printed to the console.
     * @return The spell instance, allowing this method to be chained onto the constructor.
     * @throws IllegalStateException if this method is called after the spell properties have been initialised.
     */
    // Nobody can remove property keys, which guarantees that spell classes always have the properties they need.
    // It also means that subclasses need not worry about properties already defined and used in their superclass.
    // Conversely, general spell classes ONLY EVER define the properties they ACTUALLY USE.
    public final Spell addProperties(String... keys){

        if(arePropertiesInitialised()) throw new IllegalStateException("Tried to add spell properties after they were initialised");

        for(String key : keys) if(propertyKeys.contains(key)) Wizardry.LOGGER.warn("Tried to add a duplicate property key '"
                + key + "' to spell " + this.name);

        Collections.addAll(propertyKeys, keys);

        return this;
    }




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
    public void createProperties(Spell spell, Tier tier, Element element, SpellType type, int cost, int chargeup, int cooldown){
        this.properties = new SpellProperties(spell, tier, element, type, cost, chargeup, cooldown);
    }


    public String getStringProperty(String key){
        return (String) properties.getProperties(key);
    }

    public Integer getIntProperty(String key){
        return (Integer) properties.getProperties(key);
    }
    public boolean getBooleanProperty(String key){
        return (Boolean) properties.getProperties(key);
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
