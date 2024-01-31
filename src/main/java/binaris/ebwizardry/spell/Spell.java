package binaris.ebwizardry.spell;


import binaris.ebwizardry.entity.living.EntityWizard;
import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.config.SpellProperties;
import binaris.ebwizardry.constant.Element;
import binaris.ebwizardry.constant.SpellType;
import binaris.ebwizardry.constant.Tier;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public abstract class Spell {
    // ========================================= Spell properties Keys ===============================================
    // Spell property keys. These are public constants so that they can be accessed from anywhere, they are
    // effectively immutable and should not be changed.
    // ================================================================================================================
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
    public static final String KNOCKBACK_STRENGTH = "knockback_strength";

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

    /** This spell's associated SpellProperties object. */
    private SpellProperties properties;

    @Nullable
    private String translationKey;

    /**
     * This constructor should be called from any subclasses, either feeding in the constants directly or through their
     * own constructor from wherever the spell is registered. This is the constructor for wizardry's own spells; spells
     * added by other mods should use
     * {@link Spell#Spell(String, String, UseAction, boolean)}.
     * @param name The <i>registry name</i> of the spell. This will also be the name of the icon file. The spell's
     *        localized name will be a resource location with the format [modid]:[name].
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

    // ============================================= Getters =======================================================

    /** Returns the {@code Identifier} for this spell's icon. */
    public final Identifier getIcon(){
        return icon;
    }
    public String getSpellName(){return name;}

    public int getId() {return id;}

    /** Returns the {@code SpellProperties} object for this spell.
     * @throws IllegalStateException if the properties have not been initialized yet. */
    public final SpellProperties getProperties(){
        return properties;
    }

    /**
     * Whether this spell requires a packet to be sent when it is cast. Returns true by default, but can be overridden
     * to return false <b>if</b> the spell's cast() method does not use any code that must be executed client-side (i.e.
     * particle spawning). This is not checked for continuous spells, because they never need to send packets.
     * <p></p>
     * <i>If in doubt, leave this method as is; it is purely an optimisation.</i>
     *
     * @return <b>false</b> if the spell code should only be run on the server and the client of the player casting
     *         it<br>
     *         <b>true</b> if the spell code should be run on the server and all clients in the dimension
     */
    // Edit: Turns out that swingItem() actually sends packets to all nearby clients, but not the client doing the
    // swinging.
    // Also, now I think about it, this method isn't going to make the slightest bit of difference to the item usage
    // actions since setItemInUse() is called in ItemWand, not the spell class - so the only thing that matters here is
    // the particles.
    public boolean requiresPacket(){
        return true;
    }

    /** Called from {@code init()} in the main mod class. Used to initialize spell fields and properties that depend on
     * other things being registered (e.g. potions). <i>Always initialise things in the constructor wherever possible.</i> */
    @Deprecated
    public void init(){}


    /** Returns true if this spell's properties have been initialized, false if not. Check this if you're attempting
     * to access them from code that could be called before wizardry's {@code init()} method (e.g. item attributes). */
    public final boolean arePropertiesInitialised(){
        return properties != null;
    }

    // ============================================== Casting =======================================================
    //
    // ================================================================================================================

    /**
     * Casts the spell. Each subclass must override this method and within it execute the code to make the spell work.
     * Returns a boolean so that the main onItemRightClick or onUsingItemTick method can check if the spell was actually
     * cast or whether a spell-specific condition caused it not to be (for example, heal won't work if the player is on
     * full health), preventing unfair drain of mana.
     * <p></p>
     * Each spell must return true when it works or the spell will not use up mana. Note that (!world.isRemote) does not
     * count as a condition; return true should be outside it - in other words, return a value on both the client and
     * the server.
     * <p></p>
     * It's worth noting that on the client side, this method only gets called if the server side cast() method
     * succeeded, so you can put any particle spawning code outside any success conditions if there are discrepancies
     * between the client and server.
     *
     * @param world The world in which the spell is being cast.
     * @param caster The EntityPlayer that cast the spell.
     * @param hand The hand that is holding the item used to cast the spell. If no item was used, this will be the main
     *        hand.
     * @param ticksInUse The number of ticks the spell has already been cast for. For all non-continuous spells, this is
     *        0 and is not used. For continuous spells, it is passed in as the maximum use duration of the item minus
     *        the count parameter in onUsingItemTick, and therefore it increases by 1 each tick.
     * @param modifiers A {@link SpellModifiers} object containing the modifiers that have been applied to the spell.
     *        See the javadoc for that class for more information.
     * @return True if the spell succeeded and mana should be used up, false if not.
     */
    public abstract boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers);

    /**
     * Casts the spell, but with an EntityLiving as the caster. Each subclass can optionally override this method and
     * within it execute the code to make the spell work. Returns a boolean to allow whatever calls this method to check
     * if the spell was actually cast or whether a spell-specific condition caused it not to be (for example, heal won't
     * work if the caster is on full health).
     * <p></p>
     * This method is intended for use by NPCs (see {@link EntityWizard}) so that they can cast spells. Override it if
     * you want a spell to be cast by wizards. Note that you must also override {@link Spell#canBeCastBy(LivingEntity, boolean)} to
     * return true to allow wizards to select the spell. For some spells, this method may well be exactly the same as
     * the regular cast method; for others it won't be - for example, projectile-based spells are normally done using
     * the player's look vector, but NPCs need to use a target-based method instead.
     * <p></p>
     * Each spell must return true when it works. Note that (!world.isRemote) does not count as a condition; return true
     * should be outside it - in other words, return a value on both the client and the server.
     * <p></p>
     * It's worth noting that on the client side, this method only gets called if the server side cast() method
     * succeeded, so you can put any particle spawning code outside any success conditions if there are discrepancies
     * between the client and server.
     *
     * @param world The world in which the spell is being cast.
     * @param caster The EntityLiving that cast the spell.
     * @param hand The hand that is holding the item used to cast the spell. This will almost certainly be the main
     *        hand.
     * @param ticksInUse The number of ticks the spell has already been cast for. For all non-continuous spells, this is
     *        0 and is not used.
     * @param target The EntityLivingBase that is targeted by the spell. May be null in some cases.
     * @param modifiers A {@link SpellModifiers} object containing the modifiers that have been applied to the spell.
     *        See the javadoc for that class for more information. If no modifiers are required, pass in
     *        {@code new SpellModifiers()}.
     * @return True if the spell succeeded, false if not. Returns false by default.
     */
    public boolean cast(World world, LivingEntity caster, Hand hand, int ticksInUse, LivingEntity target,
                        SpellModifiers modifiers){
        return false;
    }

    /**
     * Casts the spell, but with an origin and a direction instead of a caster. Each subclass can optionally override this
     * method and within it execute the code to make the spell work. Returns a boolean to allow whatever calls this method
     * to check if the spell was actually cast or whether a spell specific condition caused it not to be (for example, heal
     * won't work if the caster is on full health).
     * <p></p>
     * This method is intended for use by dispensers and command blocks so that they can cast spells. Override it if
     * you want a spell to be cast by dispensers. Note that you must also override {@link Spell#canBeCastBy(DoubleBlockProperties)} to
     * return true to allow dispensers to select the spell. For some spells, this method may well be exactly the same as
     * the regular cast method; for others it won't be - for example, projectile-based spells are normally done using
     * the player's look vector, but dispensers need to use a facing-based method instead.
     * <p></p>
     * Each spell must return true when it works. Note that (!world.isRemote) does not count as a condition; return true
     * should be outside it - in other words, return a value on both the client and the server.
     * <p></p>
     * It's worth noting that on the client side, this method only gets called if the server side cast() method
     * succeeded, so you can put any particle spawning code outside of any success conditions if there are discrepancies
     * between the client and server.
     *
     * @param world The world in which the spell is being cast.
     * @param x The x coordinate of the origin point of the spell.
     * @param y The y coordinate of the origin point of the spell.
     * @param z The z coordinate of the origin point of the spell.
     * @param direction The cardinal (UDNSEW) direction in which the spell is being cast.
     * @param ticksInUse The number of ticks the spell has already been cast for. For all non-continuous spells, this is
     *        0 and is not used.
     * @param duration The duration this spell will be cast for, or -1 if it will be cast indefinitely. For all
     *                 non-continuous spells, this is 0 and is not used. This is intended for use in sound loops; there
     *                 should be no need to use it for anything else.
     * @param properties A {@link SpellProperties} object containing the modifiers that have been applied to the spell.
     *        See the javadoc for that class for more information. If no modifiers are required, pass in
     *        {@code new SpellModifiers()}.
     * @return True if the spell succeeded, false if not. Returns false by default.
     */
    public boolean cast(World world, double x, double y, double z, Direction direction, int ticksInUse, int duration, SpellProperties properties){
        return false;
    }

    /**
     * Called when the spell stops being cast, either from running out of mana, being stopped by the caster, or due
     * to a stack of scrolls running out.
     * This method is mostly used
     * for adding particle effects and sounds on spell finish.
     * <p></p>
     * Because this method is not used in the majority of cases, it was deemed excessive to have three separate
     * methods for players, NPCs and dispensers.
     * Instead, some parameters may be null depending on the circumstances,
     * similar to the implementation in {@link binaris.ebwizardry.event.SpellCastEvent SpellCastEvent}.
     * Be sure to check for this before using them!
     *
     * @param world The world in which the spell was cast.
     * @param caster The player or NPC that cast the spell, or null if it was cast from a dispenser.
     * @param x The x coordinate of the origin point of the spell, or NaN if the spell wasn't cast from a dispenser.
     * @param y The y coordinate of the origin point of the spell, or NaN if the spell wasn't cast from a dispenser.
     * @param z The z coordinate of the origin point of the spell, or NaN if the spell wasn't cast from a dispenser.
     * @param direction The cardinal (UDNSEW) direction in which the spell was cast, or null if the spell wasn't cast
     *                  from a dispenser.
     * @param duration The number of ticks the spell was cast for.
     * @param modifiers The modifiers the spell was cast with.
     */
    // Conveniently, we can't always get a reference to the target for NPC casting once the spell ends (because it
    // might have died or run off, or the NPC might have lost interest...) - so let's just not bother!
    public void finishCasting(World world, @Nullable LivingEntity caster, double x, double y, double z,
                              @Nullable Direction direction, int duration, SpellModifiers modifiers){}


    /**
     * Whether the given entity can cast this spell. If you have overridden
     * {@link Spell#cast(World, LivingEntity, Hand, int, LivingEntity, SpellModifiers)}, you should override
     * this to return true (either always or under certain circumstances), or alternatively assign an NPC selector via
     * @param npc The entity to query.
     * @param override True if a player in creative mode is assigning this spell to the given entity, false otherwise.
     *                 Usually this means situational conditions should be ignored.
     */
    // We could make this final and force everyone to move over to the predicate system, but for particularly complex
    // behaviour (i.e. several lines of code) it gets too ugly, and then you end up moving the contents of the predicate
    // to a static method anyway and referring to it via method reference... so we may as well leave people the option.
    public boolean canBeCastBy(LivingEntity npc, boolean override){
        // return npcSelector.test(npc, override);
        // TODO: Implement this.
        return false;
    }

    /**
     * Whether the given dispenser can cast this spell. If you have overridden
     * {@link Spell#cast(World, double, double, double, Direction, int, int, SpellProperties)}, you should override this
     * to return true (either always or under certain circumstances).
     * @param dispenser The dispenser to query.
     */
    public boolean canBeCastBy(DoubleBlockProperties dispenser){
        return false;
    }

    // ============================================= Spell properties ===============================================
    /** Creates a {@link SpellProperties} object for this spell, which is used to store all the spell's properties.
     * This should be called from the constructor of any subclasses, after the spell's tier, element and type have been
     * set, these are just specific and not changeable values, if you want to add more properties to the spell, use the
     * {@link Spell#addProperties(String, Object)} method.
     * */
    public Spell createProperties(Tier tier, Element element, SpellType type, int cost, int chargeup, int cooldown){
        this.properties = new SpellProperties(this, tier, element, type, cost, chargeup, cooldown);
        return this;
    }

    /** Adds a property to this spell. This should be called from the constructor of any subclasses, after the spell's
     * tier, element and type have been set. */
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


    // ============================================= Translation ===============================================
    // Most of this is copied from Item.java, but it's not worth making a superclass just for this...
    // Methods for getting the localized name of the spell. This is done by creating a resource location with the
    // format spell.[modid].spellName and translating it in the lang files. The translation key is cached for efficiency.
    // =========================================================================================================

    /**
     * Returns the localized name of this spell <i>without formatting</i>.
     * For the formatted version, see {@link Spell#getNameWithFormatting()}.
     * @return The localized name of this spell.
     * */
    public Text getName() {
        return Text.translatable(this.getOrCreateTranslationKey());
    }

    /**
     * Return the string name for translation lookup of this spell.
     * @return The unlocalized name of this spell.
     * **/
    protected String getOrCreateTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.createTranslationKey("spell", Wizardry.REGISTRIES_SPELL.getId(this));
        }
        return this.translationKey;
    }

    /**
     * Gets the translation key for the given spell.
     * @return The translation key for this spell.
     * **/
    public String getTranslationKey() {
        return this.getOrCreateTranslationKey();
    }

    /**
     * Returns the localized name of this spell <i>with formatting</i>.
     * For the unformatted version, see {@link Spell#getName()}.
     * @return The localized name of this spell with formatting.
     * */
    public Text getNameWithFormatting() {
        return Text.translatable(this.getOrCreateTranslationKey()).formatted(this.properties.getElement().getColour());
    }




}
