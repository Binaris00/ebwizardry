package binaris.ebwizardry.entity.living;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.item.SpellCastingItem;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.AllyDesignationSystem;
import binaris.ebwizardry.util.EntityUtil;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Predicate;
/**
 * Interface for all summoned creatures. You can use this to extend vanilla creatures or modded creatures. An example
 * is the {@link binaris.ebwizardry.entity.living.ZombieMinionEntity ZombieMinionEntity} which extends {@link net.minecraft.entity.mob.ZombieEntity ZombieEntity} to use
 * all the attributes and methods of the vanilla entity.
 * */
@Deprecated
public interface SummonedEntity extends Ownable {
    String NAMEPLATE_TRANSLATION_KEY = "entity." + Wizardry.MODID + ".summonedcreature.nameplate" ;
    // ============================================= Getters and setters ================================================
    // Those should be private, but interfaces can't have private methods, so we'll have to make do with public ones.

    /** Sets the lifetime of the summoned creature in ticks. */
    void setLifetime(int ticks);

    /** Returns the lifetime of the summoned creature in ticks. */
    int getLifetime();

    /** Internal, do not use. Implementing classes should implement this to set their owner UUID field. */
    void setOwnerId(UUID uuid);


    @Nullable
    @Override
    default Entity getOwner(){return getCaster();};

    @Nullable
    UUID getOwnerId();

    /**
     * Returns the EntityLivingBase that summoned this creature, or null if it no longer exists. Cases where the entity
     * may no longer exist are: entity died or was deleted, mob despawned, player logged out, entity teleported to
     * another dimension, or this creature simply had no caster in the first place.
     */
    @Nullable
    default LivingEntity getCaster(){
        if(this instanceof Entity){
            Entity entity = EntityUtil.getEntityByUUID(((Entity) this).getWorld(), getOwnerId());

            if(entity != null && !(entity instanceof LivingEntity)){ // Should never happen
                Wizardry.LOGGER.warn("{} has a non-living owner!", this);
                return null;
            }

            return (LivingEntity) entity;

        }else{
            Wizardry.LOGGER.warn("{} implements ISummonedCreature but is not an SoundLoopSpellEntity!", this.getClass());
            return null;
        }
    }

    /**
     * Sets the EntityLivingBase that summoned this creature.
     */
    default void setCaster(@Nullable LivingEntity caster) {
        setOwnerId(caster == null ? null : caster.getUuid());
    }

    /**
     * Determines whether the given target is valid. Used by the default target selector (see
     * {@link SummonedEntity#getTargetSelector()}) and revenge targeting checks. This method is responsible for the
     * ally designation system, default classes that may be targeted and the config whitelist/blacklist.
     * Implementors may override this if they want to do something different or add their own checks.
     * @see AllyDesignationSystem#isValidTarget(Entity, Entity)
     */
    default boolean isValidTarget(Entity target){
        // If the target is valid based on the ADS...
        if(AllyDesignationSystem.isValidTarget(this.getCaster(), target)){

            // ...and is a player, they can be attacked, since players can't be in the whitelist or the
            // blacklist...
            if(target instanceof PlayerEntity){
                // ...unless the creature was summoned by a good wizard who the player has not angered.
                if(getCaster() instanceof EntityWizard){
                    //if(getCaster().getRevengeTarget() != target
                    //        && ((EntityWizard)getCaster()).getAttackTarget() != target) {
                    //    return false;
                    //}
                }

                return true;
            }

            // ...and is a mob, a summoned creature, a wizard...
            // ...it can be attacked.
            // TODO: Wizardry Data
            //                    // ...or in the whitelist...
            //                    || Arrays.asList(Wizardry.settings.summonedCreatureTargetsWhitelist)
            //                    .contains(EntityList.getKey(target.getClass())))
            //                    // ...and isn't in the blacklist...
            //                    && !Arrays.asList(Wizardry.settings.summonedCreatureTargetsBlacklist)
            //                    .contains(EntityList.getKey(target.getClass()))
            return target instanceof Monster || target instanceof SummonedEntity
                    || (target instanceof EntityWizard && !(getCaster() instanceof EntityWizard))
                    // ...or something that's attacking the owner...
                    || (target instanceof LivingEntity && ((LivingEntity) target).getAttacker() == getCaster());
        }

        return false;
    }

    /**
     * Returns an entity selector to be passed into AI methods. Normally, this should not be overridden, but it is
     * possible for implementors to override this to do something special when selecting a target.
     */
    default Predicate<LivingEntity> getTargetSelector(){
        return entity -> !entity.isInvisible() && (getCaster() == null ? entity instanceof PlayerEntity &&
                !((PlayerEntity)entity).isCreative() : isValidTarget(entity));
    }


    /**
     * Called when this creature has existed for 1 tick, effectively when it has just been spawned. Normally used to add
     * particles, sounds, etc.
     */
    void onSpawn();

    /**
     * Called when this summoned creature vanishes. Normally used to add particles, sounds, etc.
     */
    void onDespawn();

    /** Whether this creature should spawn a subtle black swirl particle effect while alive. */
    boolean hasParticleEffect();

    /** Returns whether this creature has an animation when appearing and disappearing. */
    default boolean hasAnimation(){
        return true;
    }

    /** Returns the colour of this creature's appear/disappear animation. */
    default int getAnimationColour(float animationProgress){
        return 0x000000;
    }

    /**
     * Called from the event handler after the damage change is applied. Does nothing by default, but can be overridden
     * to do something when a successful attack is made. This was added because the event-based damage source system can
     * cause parts of attackEntityAsMob not to fire, since attackEntityFrom is intercepted and canceled.
     * <p></p>
     * Usage examples: {@link EntitySilverfishMinion} uses this to summon more silverfish if the target is killed,
     * {@link EntitySkeletonMinion} and {@link EntitySpiderMinion} use this to add potion effects to the target.
     */
    default void onSuccessfulAttack(LivingEntity target){
    }

    // Delegates

    /**
     * Implementors should call this from writeEntityToNBT. Can be overridden as long as super is called, but there's
     * very little point in doing that since anything extra could just be added to writeEntityToNBT anyway.
     */
    default void writeNBTDelegate(NbtCompound nbtCompound){
        if(this.getCaster() != null){
            nbtCompound.putUuid("casterUUID", this.getCaster().getUuid());
        }
        nbtCompound.putInt("lifetime", getLifetime());
    }

    /**
     * Implementors should call this from readEntityFromNBT. Can be overridden as long as super is called, but there's
     * very little point in doing that since anything extra could just be added to readEntityFromNBT anyway.
     */
    default void readNBTDelegate(NbtCompound nbtCompound){
        this.setOwnerId(nbtCompound.getUuid("casterUUID"));
        this.setLifetime(nbtCompound.getInt("lifetime"));
    }

    /**
     * Implementors should call this from setRevengeTarget, and call super.setRevengeTarget if and only if this method
     * returns <b>true</b>.
     */
    default boolean shouldRevengeTarget(LivingEntity entity){
        // Allows the config to prevent minions from revenge-targeting their owners (or anything else, for that matter)
        return false;
    }

    /**
     * Implementors should call this from onUpdate. Can be overridden as long as super is called, but there's very
     * little point in doing that since anything extra could just be added to onUpdate anyway.
     */
    default void updateDelegate(){
        if(!(this instanceof Entity entity))
            throw new ClassCastException("Implementations of ISummonedCreature must extend SoundLoopSpellEntity!");

        if(entity.age == 1){
            this.onSpawn();
        }

        // For some reason Minecraft reads the entity from NBT just after the entity is created, so setting -1 as a
        // default lifetime doesn't work. The easiest way around this is to use 0 - nobody's going to need it!
        if(entity.age > this.getLifetime() && this.getLifetime() > 0){
            this.onDespawn();
            entity.discard();
        }

        if(this.hasParticleEffect() && entity.getWorld().isClient && entity.getWorld().random.nextInt(6) == 0)
            ParticleBuilder.create(WizardryParticles.DARK_MAGIC)
                    .pos(entity.prevX , entity.prevY + entity.getWorld().random.nextDouble() * 1.5, entity.prevZ)
                    .color(0.1f, 0.0f, 0.0f)
                    .spawn(entity.getWorld());

    }

    /**
     * Implementors should call this from processInteract, and call super.processInteract if and only if this method
     * returns <b>false</b>.
     */
    default ActionResult interactDelegate(PlayerEntity player, Hand hand){
        ItemStack stack = player.getStackInHand(hand);
        // Selects one of the player's minions.
        if(player.isSneaking() && stack.getItem() instanceof SpellCastingItem){

            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }
}
