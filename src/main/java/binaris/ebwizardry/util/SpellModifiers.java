package binaris.ebwizardry.util;

import com.google.common.collect.Sets;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SpellModifiers {

    /** Constant string identifier for the potency modifier. */
    public static final String POTENCY = "potency";
    /** Constant string identifier for the mana cost modifier. */
    public static final String COST = "cost";
    /** Constant string identifier for the wand charge-up modifier. */
    public static final String CHARGEUP = "chargeup";
    /** Constant string identifier for the wand progression modifier. */
    public static final String PROGRESSION = "progression";

    private final Map<String, Float> multiplierMap;
    private final Map<String, Float> syncedMultiplierMap;

    /**
     * Creates an empty SpellModifiers object. All calls to <code>get(...)</code> on an empty SpellModifiers object will
     * return a value of 1.
     */
    public SpellModifiers(){
        multiplierMap = new HashMap<>();
        syncedMultiplierMap = new HashMap<>();
    }

    private SpellModifiers(Map<String, Float> multiplierMap, Map<String, Float> syncedMultiplierMap){
        this.multiplierMap = multiplierMap;
        this.syncedMultiplierMap = syncedMultiplierMap;
    }

    /** Returns a deep copy (with copies of the underlying maps) of this {@code SpellModifiers} object. */
    public SpellModifiers copy(){
        return new SpellModifiers(new HashMap<>(this.multiplierMap), new HashMap<>(this.syncedMultiplierMap));
    }

    /**
     * Combines the given SpellModifiers object with this one. More specifically: for each modifier, multiplies the
     * values from both objects together and sets it to sync if either object does so. This method changes the object
     * it is called on but not the one given as a parameter; apart from this it does not matter which way round the two
     * objects are.
     * @param modifiers The SpellModifiers object to combine with this one; will not be modified by this method.
     * @return The SpellModifiers object, allowing this method to be chained onto the constructor.
     */
    public SpellModifiers combine(SpellModifiers modifiers){
        for(String key : Sets.union(this.multiplierMap.keySet(), modifiers.multiplierMap.keySet())){
            float newValue = this.get(key) * modifiers.get(key);
            // Also need to update the synced map if the modifier is synced in either object
            boolean sync = this.syncedMultiplierMap.containsKey(key) || modifiers.syncedMultiplierMap.containsKey(key);
            this.set(key, newValue, sync);
        }
        return this;
    }

    /**
     * Adds the given multiplier to this SpellModifiers object, using the string identifier that the given wand upgrade
     * item was registered with.
     *
     * @throws IllegalArgumentException if the given item is not a registered special wand upgrade.
     * @param upgrade The upgrade item the multiplier corresponds to.
     * @param multiplier The multiplier value, with 1 being default. Usage of modifiers is up to individual spells to
     *        implement.
     * @param needsSyncing Whether this multiplier should be synchronised with the client via packets. <i>Only set this
     *        to true if particles will be spawned which need to know the value of the multiplier.</i>
     * @return The SpellModifiers object, allowing this method to be chained onto the constructor.
     */
    public SpellModifiers set(Item upgrade, float multiplier, boolean needsSyncing){
        this.set(WandHelper.getIdentifier(upgrade), multiplier, needsSyncing);
        return this;
    }

    /**
     * Adds the given multiplier to this SpellModifiers object, using the given string key. In most cases, the
     * multiplier will correspond to a wand upgrade, in which case use {@link SpellModifiers#set(Item, float, boolean)}
     * instead.
     *
     * @param key The key used to identify the multiplier.
     * @param multiplier The multiplier value, with 1 being default. Usage of modifiers is up to individual spells to
     *        implement.
     * @param needsSyncing Whether this multiplier should be synchronised with the client via packets. <i>Only set this
     *        to true if particles will be spawned which depend on the multiplier.</i>
     * @return The SpellModifiers object, allowing this method to be chained onto the constructor.
     */
    public SpellModifiers set(String key, float multiplier, boolean needsSyncing){
        multiplierMap.put(key, multiplier);
        if(needsSyncing) syncedMultiplierMap.put(key, multiplier);
        return this;
    }

    /**
     * Returns the multiplier corresponding to the given wand upgrade item, or 1 if no multiplier was stored.
     *
     * @throws IllegalArgumentException if the given item is not a registered special wand upgrade.
     */
    public float get(Item upgrade){
        return get(WandHelper.getIdentifier(upgrade));
    }

    /**
     * Returns the multiplier corresponding to the given string key, or 1 if no multiplier was stored. In most cases,
     * the multiplier will correspond to a wand upgrade, in which case use {@link SpellModifiers#get(Item)} instead.
     */
    public float get(String key){
        Float value = multiplierMap.get(key);
        // Must check for null before unboxing, and if it is null, return the default 1.
        return value == null ? 1 : value;
    }

    // Not sure this really makes sense with the current system, it may just be better to keep it how it is
//	/**
//	 * Returns the <i>level</i> of upgrade (i.e. number of upgrades or wand tier) that would be required to
//	 * generate a modifier with the given key. <i>This does not necessarily mean that was how this modifier was
//	 * applied; and the returned value may not be a whole number if commands were involved.</i>
//	 */
//	public float level(String key){
//		return 0;
//	}

    /**
     * Returns an amplified version of the multiplier corresponding to the given string key. An <i>amplified</i>
     * modifier is the original modifier <i>scaled about 1</i> - for example, amplifying by 2 would produce the
     * following results:<br>
     * 1.3 -> 1.6<br>
     * 2 -> 3<br>
     * 0.7 -> 0.4<br>
     * 1 -> 1<br>
     * (In other words, the modifier is decreased by 1, multiplied by the scalar and then increased by 1 again.)<br>
     * <b>N.B. This does not change the stored modifier.</b>
     */
    public float amplified(String key, float scalar){
        return (get(key) - 1) * scalar + 1;
    }

    /**
     * Returns an unmodifiable map of the modifiers stored in this SpellModifiers object. Useful for iterating through
     * the modifiers.
     */
    public Map<String, Float> getModifiers(){
        return Collections.unmodifiableMap(this.multiplierMap);
    }

    /** Removes all modifiers from this SpellModifiers object, effectively resetting them all to 1. */
    public void reset(){
        this.multiplierMap.clear();
        this.syncedMultiplierMap.clear();
    }

    /** Reads this SpellModifiers object from the given ByteBuf.
     * TODO: This is not tested yet. With the new system, this could be not working.
     * */
    @Deprecated
    public void read(PacketByteBuf buf){
        int entryCount = buf.readInt();
        for(int i = 0; i < entryCount; i++){
            this.set(buf.readString(), buf.readFloat(), false);
        }
    }

    /** Writes this SpellModifiers object to the given PacketByteBuf so it can be sent via packets.
     * TODO: This is not tested yet. With the new system, this could be not working.
     * */
    @Deprecated
    public void write(PacketByteBuf buf){
        buf.writeInt(syncedMultiplierMap.size());
        for(Map.Entry<String, Float> entry : syncedMultiplierMap.entrySet()){
            buf.writeString(entry.getKey());
            buf.writeFloat(entry.getValue());
        }
    }

    // These two don't use the Map <-> NBT methods in WizardryUtilities because it's better to use the strings as keys
    // themselves rather than storing them separately.

    /**
     * Creates a new SpellModifiers object from the given NBTTagCompound. The NBTTagCompound should have 1 or more float
     * tags, which will be stored as modifiers under the same name as the tag. For example, the following NBT tag (in
     * command syntax) represents a SpellModifiers object with a damage modifier of 1.5 and a range modifier of 2:
     * <p></p>
     * <code>{damage:1.5, range:2}</code>
     * <p></p>
     * Note that needsSyncing is set to true for all returned modifiers.
     */
    public static SpellModifiers fromNBT(NbtCompound nbt){
        SpellModifiers modifiers = new SpellModifiers();
        for(String key : nbt.getKeys()){
            modifiers.set(key, nbt.getFloat(key), true);
        }
        return modifiers;
    }

    /**
     * Creates a new NBTTagCompound for this SpellModifiers object. The NBTTagCompound will have a float tags for each
     * modifier, which will be stored using the modifier names as keys. For example, the following NBT tag (in
     * command syntax) represents a SpellModifiers object with a damage modifier of 1.5 and a range modifier of 2:
     * <p></p>
     * <code>{damage:1.5, range:2}</code>
     * <p></p>
     * Note that information about syncing of modifiers is discarded.
     */
    public NbtCompound toNBT(){
        NbtCompound nbt = new NbtCompound();
        for(Map.Entry<String, Float> entry : multiplierMap.entrySet()){
            nbt.putFloat(entry.getKey(), entry.getValue());
        }
        return nbt;
    }
}
