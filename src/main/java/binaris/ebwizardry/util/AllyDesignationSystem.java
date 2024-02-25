package binaris.ebwizardry.util;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.config.WizardryConfig;
import binaris.ebwizardry.registry.WizardryEffects;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

/**
 * Contains some useful static methods for interacting with the ally designation system. Also handles the friendly fire
 * setting.
 */
public final class AllyDesignationSystem {
    private AllyDesignationSystem(){} // No instances!

    /** Set of constants for each of the four friendly fire settings. */
    public enum FriendlyFire {
        ALL("All", false, false),
        ONLY_PLAYERS("Only players", false, true),
        ONLY_OWNED("Only summoned/tamed creatures", true, false),
        NONE("None", true, true);

        /** Constant array storing the names of each of the constants, in the order they are declared. */
        public static final String[] names;

        static {
            names = new String[values().length];
            for (FriendlyFire setting : values()) {
                names[setting.ordinal()] = setting.name;
            }
        }
        public final String name;
        public final boolean blockPlayers;
        public final boolean blockOwned;

        /** The readable name for this friendly fire setting that will be displayed on the button in the config GUI. */
        FriendlyFire(String name, boolean blockPlayers, boolean blockOwned) {
            this.name = name;
            this.blockPlayers = blockPlayers;
            this.blockOwned = blockOwned;
        }

        /**
         * Gets a friendly fire setting from its string name (ignoring case), or ALL if the given name is not a valid
         * setting.
         */
        public static FriendlyFire fromName(String name) {
            for (FriendlyFire setting : values()) {
                if (setting.name.equalsIgnoreCase(name)) return setting;
            }

            Wizardry.LOGGER.info("Invalid string for the friendly fire setting. Using default (all) instead.");
            return ALL;
        }
    }

    /**
     * Returns whether the given target can be attacked by the given attacker. It is up to the caller of this method to
     * work out what this means; it doesn't necessarily mean the target is completely immune (for example, revenge
     * targeting might reasonably bypass this). This method is intended for use where the damage is indirect and/or
     * unavoidable; direct attacks should not check this method. Currently, this means the following situations check
     * this method:
     * <p></p>
     * - AI targeting for summoned creatures<br>
     * - AI targeting for mind-controlled creatures<br>
     * - Constructs with an area of effect<br>
     * - Instantaneous spells with an area of effect around the caster (e.g. forest's curse, thunderstorm)<br>
     * - Any lightning chaining effects<br>
     * - Any projectiles which seek targets
     * <p></p>
     * Also note that the friendly fire option is dealt with in the event handler. This method acts as a sort of wrapper
     * for all the AllyDesignationSystem stuff in {@link WizardData}; more details about the ally designation system can be found there.
     *
     * @param attacker The entity that cast the spell originally
     * @param target The entity being attacked
     *
     * @return False under any of the following circumstances, true otherwise:
     *         <p></p>
     *         - The target is null
     *         <p></p>
     *         - The target is the attacker (this isn't as stupid as it sounds - anything with an AoE might cause this
     *         to be true, as can summoned creatures)
     *         <p></p>
     *         - The target and the attacker are both players and the target is an ally of the attacker (but the
     *         attacker need not be an ally of the target)
     *         <p></p>
     *         - The target is a creature that was summoned/controlled by the attacker or by an ally of the attacker.
     *         <p></p>
     *         - The target is a creature tamed by the attacker or by an ally of the attacker
     *         (see {@link Ownable}).
     *         <p></p>
     *         <i>As of wizardry 4.1.2, this method now returns <b>true</b> instead of false if the attacker is null. This
     *         is because in the vast majority of cases, it makes more sense this way: if a construct has no caster, it
     *         should affect all entities; if a minion has no caster it should target all entities; etc.</i>
     */
    public static boolean isValidTarget(Entity attacker, Entity target){
        // Owned entities inherit their owner's allies
        if(attacker instanceof Ownable && !isValidTarget(((Ownable)attacker).getOwner(), target)) return false;

        // Always return false if the target is null
        if(target == null) return false;

        // Always return true if the attacker is null - this must be after the target null check!
        if(attacker == null) return true;

        // Tests whether the target is the attacker
        if(target == attacker) return false;

        // Use a positive check for these rather than a negative check for monsters, because we only want mobs
        // that are definitely passive
        // TODO: Check passive mobs here

        // Tests whether the target is a creature that was summoned/tamed (or is otherwise owned) by the attacker
        if(target instanceof Ownable && ((Ownable)target).getOwner() == attacker){
            return false;
        }

        // TODO: MIND CONTROL HERE (PANIC)

        // Ally section
        if(attacker instanceof PlayerEntity playerAttacker){ // TODO: && WizardData.get(playerAttacker) != null
            if(target instanceof PlayerEntity playerTarget){
                // Tests whether the target is an ally of the attacker
                // TODO: PLAYER ALLIES HERE
                //if(WizardData.get(playerAttacker).isPlayerAlly(playerTarget))return false;

            }else if(target instanceof Ownable){
                // Tests whether the target is a creature that was summoned/tamed by an ally of the attacker
                // TODO: OWNED ENTITIES HERE
                //if(isOwnerAlly((PlayerEntity)attacker, (Ownable)target)) return false;

            } // TODO: MIND CONTROL HERE (PANIC)
        }

        return true;
    }

    /** Umbrella method that covers both {@link AllyDesignationSystem#isPlayerAlly(EntityPlayer, EntityPlayer)} and
     * {@link AllyDesignationSystem#isOwnerAlly(EntityPlayer, IEntityOwnable)}, returning true if the second entity is
     * either owned by the first entity, an ally of the first entity, or owned by an ally of the first entity. This is
     * generally used to determine targets for healing or other group buffs. */
    public static boolean isAllied(LivingEntity allyOf, LivingEntity possibleAlly){

        // Owned entities inherit their owner's allies
        if(allyOf instanceof Ownable){
            Entity owner = ((Ownable)allyOf).getOwner();
            if(owner instanceof LivingEntity && (owner == possibleAlly || isAllied((LivingEntity) owner, possibleAlly))) return true;
        }

        if(allyOf instanceof PlayerEntity && possibleAlly instanceof PlayerEntity
                && isPlayerAlly((PlayerEntity)allyOf, (PlayerEntity)possibleAlly)){
            return true;
        }

        if(possibleAlly instanceof Ownable){
            Ownable pet = (Ownable)possibleAlly;
            if(pet.getOwner() == allyOf) return true;
            if(allyOf instanceof PlayerEntity && isOwnerAlly((PlayerEntity)allyOf, pet)) return true;
        }

        return false;
    }

    public static boolean isPlayerAlly(PlayerEntity allyOf, PlayerEntity possibleAlly) {
        //WizardData data = WizardData.get(allyOf);
        //return data != null && data.isPlayerAlly(possibleAlly);
        return false;
    }

    public static boolean isOwnerAlly(PlayerEntity allyOf, Ownable ownable) {
        //WizardData data = WizardData.get(allyOf);
        //if (data == null) return false;
        //Entity owner = ownable.getOwner();
        //return owner instanceof Player ? data.isPlayerAlly((Player) owner) : data.isPlayerAlly(ownable.getOwnerUUID());
        return false;

    }
}
