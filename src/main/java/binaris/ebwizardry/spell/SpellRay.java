package binaris.ebwizardry.spell;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.util.EntityUtil;
import binaris.ebwizardry.util.RayTracer;
import binaris.ebwizardry.util.SpellModifiers;
import binaris.ebwizardry.util.WizardryUtils;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Generic superclass for all spells which use a raytrace to do something and (optionally) spawn particles along that
 * trajectory. This is for both continuous ('stream') spells and non-continuous ('bolt') spells. This allows all the
 * relevant code to be centralised. This class differs from most other spell superclasses in that it is abstract and as
 * such must be subclassed to define what the spell actually does. This is because ray-like spells do a wider variety of
 * different things, so it does not make sense to define more specific functions in this class since they would be
 * redundant in the majority of cases.
 * <p></p>
 * <i>N.B. The three abstract methods in this class have a {@link Nullable} caster parameter (the caster is null when
 * the spell is cast by a dispenser). When implementing these methods, be sure to check whether the  caster is
 * {@code null} and deal with it appropriately.</i>
 * <p></p>
 * Properties added by this type of spell: {@link Spell#RANGE}
 * <p></p>
 * By default, this type of spell can be cast by NPCs. {@link Spell#canBeCastBy(LivingEntity, boolean)}
 * <p></p>
 * By default, this type of spell can be cast by dispensers. {@link Spell#canBeCastBy(DoubleBlockProperties)}
 * <p></p>
 * By default, this type of spell requires a packet to be sent. {@link Spell#requiresPacket()}
 *
 * @author Electroblob
 * @since Wizardry 4.2
 */
public abstract class SpellRay extends Spell {
    /**
     * The distance below the caster's eyes that the bolt particles start from.
     */
    protected static final double Y_OFFSET = 0.25;

    /**
     * The distance between spawned particles. Defaults to 0.85.
     */
    // 0.85 was chosen to keep it similar to the most common method used previously, which gave an effective spacing of
    // 10/12 = 0.8333 when the spell did not hit anything.
    protected double particleSpacing = 0.85;
    /**
     * The maximum jitter (random position offset) for spawned particles. Defaults to 0.1.
     */
    protected double particleJitter = 0.1;
    /**
     * The velocity of spawned particles in the direction the caster is aiming, can be negative. Defaults to 0.
     */
    protected double particleVelocity = 0;
    /**
     * Whether living entities are ignored when raytracing. Defaults to false.
     */
    protected boolean ignoreLivingEntities = false;
    /**
     * Whether liquids count as blocks when raytracing. Defaults to false.
     */
    protected boolean hitLiquids = false;
    /**
     * Whether to ignore uncollidable blocks when raytracing. Defaults to true.
     */
    protected boolean ignoreUncollidables = true;
    /**
     * The aim assist to use when raytracing. Defaults to 0.
     */
    protected float aimAssist = 0;

    public SpellRay(String name, UseAction action, boolean isContinuous) {
        this(Wizardry.MODID, name, action, isContinuous);
    }

    public SpellRay(String modID, String name, UseAction action, boolean isContinuous) {
        super(modID, name, action, isContinuous);
    }

    /**
     * Sets the distance between spawned particles.
     *
     * @param particleSpacing The distance between particles in the ray effect.
     * @return The spell instance, allowing this method to be chained onto the constructor.
     */
    public Spell particleSpacing(double particleSpacing) {
        this.particleSpacing = particleSpacing;
        return this;
    }


    /**
     * Sets the maximum jitter (random position offset) for spawned particles.
     *
     * @param particleJitter The maximum jitter for particles in the ray effect.
     * @return The spell instance, allowing this method to be chained onto the constructor.
     */
    public Spell particleJitter(double particleJitter) {
        this.particleJitter = particleJitter;
        return this;
    }

    /**
     * Sets the velocity of spawned particles; usually used for continuous spells.
     *
     * @param particleVelocity The velocity of spawned particles in the direction the caster is aiming, can be negative.
     * @return The spell instance, allowing this method to be chained onto the constructor.
     */
    public Spell particleVelocity(double particleVelocity) {
        this.particleVelocity = particleVelocity;
        return this;
    }

    /**
     * Sets whether entities are ignored when raytracing.
     *
     * @param ignoreLivingEntities Whether to ignore living entities when raytracing. If this is true, the spell
     *                             will pass through living entities as if they weren't there.
     * @return The spell instance, allowing this method to be chained onto the constructor.
     */
    public Spell ignoreLivingEntities(boolean ignoreLivingEntities) {
        this.ignoreLivingEntities = ignoreLivingEntities;
        return this;
    }

    /**
     * Sets whether liquids count as blocks when raytracing.
     *
     * @param hitLiquids Whether to hit liquids when raytracing. If this is false, the spell will pass through
     *                   liquids as if they weren't there.
     * @return The spell instance, allowing this method to be chained onto the constructor.
     */
    public Spell hitLiquids(boolean hitLiquids) {
        this.hitLiquids = hitLiquids;
        return this;
    }

    /**
     * Sets whether uncollidable blocks are ignored when raytracing.
     *
     * @param ignoreUncollidables Whether to hit uncollidable blocks when raytracing. If this is true, the spell will
     *                            pass through uncollidable blocks as if they weren't there.
     * @return The spell instance, allowing this method to be chained onto the constructor.
     */
    public Spell ignoreUncollidables(boolean ignoreUncollidables) {
        this.ignoreUncollidables = ignoreUncollidables;
        return this;
    }


    public Spell aimAssist(float aimAssist) {
        this.aimAssist = aimAssist;
        return this;
    }

    @Override
    public boolean canBeCastBy(DoubleBlockProperties dispenser) {
        return true;
    }


    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers) {
        Vec3d look = caster.getRotationVector();
        Vec3d origin = new Vec3d(caster.getX(), caster.getY() + caster.getStandingEyeHeight() - Y_OFFSET, caster.getZ());

        if(!this.isContinuous && world.isClient && WizardryUtils.isFirstPerson(caster)){
            origin = origin.add(look.multiply(1.2));
        }
        if (!shootSpell(world, origin, look, caster, ticksInUse, modifiers)) return false;

        if (casterSwingsArm()) caster.swingHand(hand);
        this.playSound(world, caster, ticksInUse, -1, modifiers);
        return true;
    }

    @Override
    public boolean cast(World world, LivingEntity caster, Hand hand, int ticksInUse, LivingEntity target, SpellModifiers modifiers) {
        Vec3d origin = new Vec3d(caster.prevX, caster.prevY + caster.getStandingEyeHeight() - Y_OFFSET, caster.prevZ);
        Vec3d targetPos = null;

        if (target != null) {
            if (!ignoreLivingEntities || !EntityUtil.isLiving(target)) {
                targetPos = new Vec3d(target.prevX, target.prevY + target.getHeight() / 2, target.prevZ);
            } else {
                int x = MathHelper.floor(target.prevX);
                int y = (int) target.prevY - 1; // -1 because we need the block under the target
                int z = MathHelper.floor(target.prevX);
                BlockPos pos = new BlockPos(x, y, z);

                // This works as if the NPC had actually aimed at the floor beneath the target, so it needs to check that
                // the block is not air and (optionally) not a liquid.
                if (!world.isAir(pos) && (!world.getBlockState(pos).isLiquid() || hitLiquids)) {
                    targetPos = new Vec3d(x + 0.5, y + 1, z + 0.5);
                }
            }
        }
        if (targetPos == null)
            return false; // If there was nothing to aim at (e.g. snare when the target is in the air)

        if (!shootSpell(world, origin, targetPos.subtract(origin).normalize(), (PlayerEntity) caster, ticksInUse, modifiers))
            return false;

        if (casterSwingsArm()) caster.swingHand(hand);
        // TODO: PLAY SOUND
        //this.playSound(world, caster, ticksInUse, -1, modifiers);
        return true;
    }

    @Override
    public boolean cast(World world, double x, double y, double z, Direction direction, int ticksInUse, int duration, SpellModifiers modifiers) {
        Vec3d vec = new Vec3d(direction.getUnitVector());
        Vec3d origin = new Vec3d(x, y, z);
        return shootSpell(world, origin, vec, null, ticksInUse, modifiers);

        // This MUST be the coordinates of the actual dispenser, so we need to offset it
        // TODO: PLAY SOUND
        //this.playSound(world, x - direction.getXOffset(), y - direction.getYOffset(), z - direction.getZOffset(), ticksInUse, duration, modifiers);
    }

    /**
     * Hook allowing subclasses to override the default range calculation on a per-cast basis. For example, grapple
     * overrides this to change the range based on casting time so that its vine attaches to entities/blocks at the
     * correct point and moves them accordingly.
     *
     * @return The range to be used for this particular casting of the spell.
     */
    @Deprecated
    protected double getRange() {
        return getDoubleProperty(RANGE);
        // TODO: MODIFIERS
        //return getBooleanProperty(RANGE).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
    }

    /**
     * Hook allowing subclasses to determine whether the caster swings their arm when casting the spell. By default,
     * returns true for non-continuous spells without an action.
     * @return True if the caster should swing their arm when casting this spell, false if not.
     */
    protected boolean casterSwingsArm() {
        return !this.isContinuous && this.action == UseAction.NONE;
    }

    /** Takes care of the shared stuff for the three casting methods. This is mainly for internal use.
     * You should normally not need to override this method.
     * <p></p>
     * Set the origin to the caster's eyes (if the caster is not null) and the direction to the caster's look vector.
     * Use a raytrace to find the first entity or block hit by the spell, then call the appropriate method based on
     * what was hit. If the spell hits nothing, call the onMiss method.
     * */
    protected boolean shootSpell(World world, Vec3d origin, Vec3d direction, PlayerEntity caster, int ticksInUse, SpellModifiers modifiers) {
        double range = getRange();
        Vec3d endpoint = origin.add(direction.multiply(range));

        HitResult rayTrace = RayTracer.rayTrace(world, caster, origin, endpoint, aimAssist, hitLiquids, Entity.class, ignoreLivingEntities ? EntityUtil::isLiving : RayTracer.ignoreEntityFilter(caster));

        boolean flag = false;

        if (rayTrace != null) {
            if (rayTrace.getType() == HitResult.Type.ENTITY) {
                flag = onEntityHit(world, ((EntityHitResult) rayTrace).getEntity(), rayTrace.getPos(), caster, origin, ticksInUse, modifiers);

                if (flag) range = origin.distanceTo(rayTrace.getPos());
            } else if (rayTrace.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) rayTrace;
                flag = onBlockHit(world, blockHitResult.getBlockPos(), blockHitResult.getSide(), rayTrace.getPos(), caster, origin, ticksInUse, modifiers);

                range = origin.distanceTo(rayTrace.getPos());
            }
        }

        if (!flag && !onMiss(world, caster, origin, direction, ticksInUse, modifiers)) return false;


        if (world.isClient) {
            spawnParticleRay(world, origin, direction, range);
        }

        return true;
    }

    /**
     * Called when the spell hits an entity. Will never be called if ignoreLivingEntities is true.
     * @param world The world the entity is in.
     * @param target The entity that was hit.
     * @param hit A vector representing the exact position at which the spell first hit the entity. Usually used for
     * particle spawning.
     * @param caster The caster of this spell, or null if this spell was cast from a dispenser. <i> N.B. It is strongly
     * recommended that the origin parameter is used instead of taking the caster's position directly.</i>
     * @param origin The position at which this spell originated. If the caster is not null, this will be in the caster's
     * eyes.
     * @param ticksInUse The number of ticks the spell has already been cast for (used only for continuous spells).
     * @param modifiers The modifiers this spell was cast with.
     * @return True to continue with spell casting and spawn particles, false to trigger a miss (N.B. you will need to
     * return false from {@link SpellRay#onMiss(World, LivingEntity, Vec3d, Vec3d, int, SpellModifiers)}  if a miss
     * should not consume mana). Returning false from this method will make it look as if the spell passed right
     * through it, so if a spell spawns particles when it misses, this method should return true even for non-living
     * entities.
     */
    protected abstract boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers);

    /**
     * Called when the spell hits a block.
     * @param world The world the block is in.
     * @param pos The BlockPos of the block that was hit.
     * @param side The side of the block that was hit.
     * @param hit A vector representing the exact position at which the spell first hit the block. Usually used for
     * particle spawning.
     * @param caster The caster of this spell, or null if this spell was cast from a dispenser.
     * @param origin The position at which this spell originated. If the caster is not null, this will be in the caster's
     * eyes.
     * @param ticksInUse The number of ticks the spell has already been cast for (used only for continuous spells).
     * @param modifiers The modifiers this spell was cast with.
     * @return True to continue with spell casting and spawn particles, false to trigger a miss (N.B. you will need to
     * return false from {@link SpellRay#onMiss(World, LivingEntity, Vec3d, Vec3d, int, SpellModifiers)} if a miss should not consume
     * mana).
     */
    protected abstract boolean onBlockHit(World world, BlockPos pos, Direction side, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers);

    /**
     * Called when the spell does not hit anything or when the spell hits something it has no effect on. Most of the time
     * this will just return true or false, but some spells may, for example, display a chat readout or spawn custom
     * particles. It is worth noting that this can affect how straightforward the spell is to identify.
     * @param world The world the spell is in.
     * @param caster The caster of this spell, or null if this spell was cast from a dispenser.
     * @param origin The position at which this spell originated. If the caster is not null, this will be in the caster's
     * eyes.
     * @param direction A normalized vector in the direction this spell was cast (useful for custom particle effects).
     * @param ticksInUse The number of ticks the spell has already been cast for (used only for continuous spells).
     * @param modifiers The modifiers this spell was cast with.
     * @return True to continue with spell casting and spawn particles, false to cause the spell to fail.
     */
    protected abstract boolean onMiss(World world, @Nullable LivingEntity caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers);

    /**
     * A particle method, only called in client-side. In normal circumstances, you don't need to override this method.
     * But if you want to change the way particles are spawned or add more special features to the spell.
     * <p></p>
     * Spawns a line of particles along the trajectory of the spell. This method is called by {@link SpellRay#shootSpell(World, Vec3d, Vec3d, PlayerEntity, int, SpellModifiers)}
     * And uses the jitter and spacing parameters to spawn particles at regular intervals along the line to pass it to {@link SpellRay#spawnParticle(World, double, double, double, double, double, double)}
     * @param world The world in which to spawn the particles.
     * @param origin A vector representing the start point of the line of particles.
     * @param direction A normalised vector representing the direction of the line of particles.
     * @param distance The length of the line of particles, already set to the appropriate distance based on the spell's
     */
    protected void spawnParticleRay(World world, Vec3d origin, Vec3d direction, double distance){

        Vec3d velocity = direction.multiply(particleVelocity);

        for(double d = particleSpacing; d <= distance; d += particleSpacing){
            double x = origin.x + d*direction.x + particleJitter * (world.random.nextDouble()*2 - 1);
            double y = origin.y + d*direction.y + particleJitter * (world.random.nextDouble()*2 - 1);
            double z = origin.z + d*direction.z + particleJitter * (world.random.nextDouble()*2 - 1);
            spawnParticle(world, x, y, z, velocity.x, velocity.y, velocity.z);
        }
    }

    /**
     * Called to add a particle effect for the spell trajectory. Only called in client-side code.
     * Override to add custom particle effects.
     * <p></p>
     * If you want to change the way particles are spawned, you should override {@link #spawnParticleRay(World, Vec3d, Vec3d, double)} instead.
     * @param world The world in which to spawn the particle.
     * @param x The x-coordinate to spawn the particle at, with jitter already applied.
     * @param y The y-coordinate to spawn the particle at, with jitter already applied.
     * @param z The z-coordinate to spawn the particle at, with jitter already applied.
     * @param vx The x velocity to spawn the particle with. Usually this is only non-zero for continuous spells.
     * @param vy The y velocity to spawn the particle with. Usually this is only non-zero for continuous spells.
     * @param vz The z velocity to spawn the particle with. Usually this is only non-zero for continuous spells.
     */
    protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz){}

}
