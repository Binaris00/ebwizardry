package binaris.ebwizardry.spell;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.function.BiFunction;

public class SpellThrowable<T extends ThrownEntity> extends Spell{
    /** NBT key for storing a damage modifier in an external entity (i.e., from vanilla or another mod). Entities with a
     * float value stored under this key will have their damage dealt multiplied by that value. This is not just for
     * projectiles; it will work for any entity that uses a damage source with itself as the immediate source. */
    public static final String DAMAGE_MODIFIER_NBT_KEY = Wizardry.MODID + "DamageModifier";
    private static final float LAUNCH_Y_OFFSET = 0.1f;
    protected final BiFunction<World, LivingEntity, T> projectileFactory;

    public SpellThrowable(String name, BiFunction<World, LivingEntity, T> projectileFactory){
        this(Wizardry.MODID, name, projectileFactory);
    }

    public SpellThrowable(String modID, String name, BiFunction<World, LivingEntity, T> projectileFactory){
        super(modID, name, UseAction.NONE, false);
        this.projectileFactory = projectileFactory;
        // TODO: NPC STUFF
        //this.npcSelector((e, o) -> true);
    }

    @Override
    public boolean requiresPacket() {
        return false;
    }

    /** Trajectory calculation - see {@link SpellProjectile} for a more detailed explanation */
    protected float calculateVelocity(SpellModifiers modifiers, float launchHeight){
        float g = 0.03f;
        // TODO: RANGE UPGRADE
        float range = getFloatProperty(RANGE); //* modifiers.get(WizardryItems.range_upgrade);
        return range / MathHelper.sqrt(2 * launchHeight/g);
    }

    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers){

        if(!world.isClient){
            float velocity = calculateVelocity(modifiers, caster.getStandingEyeHeight() - LAUNCH_Y_OFFSET);
            T projectile = projectileFactory.apply(world, caster);
            projectile.setVelocity(caster, caster.getPitch(), caster.getYaw(), 0.0f, velocity, 1.0f);
            // TODO: DAMAGE MODIFIER
            //projectile.getEntityData().setFloat(DAMAGE_MODIFIER_NBT_KEY, modifiers.get(SpellModifiers.POTENCY));
            addProjectileExtras(projectile, caster, modifiers);
            world.spawnEntity(projectile);
        }
        // TODO: SOUND
        //this.playSound(world, caster, ticksInUse, -1, modifiers);
        caster.swingHand(hand);
        return true;
    }

    @Override
    public boolean cast(World world, LivingEntity caster, Hand hand, int ticksInUse, LivingEntity target, SpellModifiers modifiers){

        if(target != null){

            if(!world.isClient){
                float velocity = calculateVelocity(modifiers, caster.getStandingEyeHeight() - LAUNCH_Y_OFFSET);
                T projectile = projectileFactory.apply(world, caster);
                // TODO: AIMING ERROR
                //int aimingError = caster instanceof ISpellCaster ? ((ISpellCaster)caster).getAimingError(world.getDifficulty())
                //        : EntityUtils.getDefaultAimingError(world.getDifficulty());
                aim(projectile, caster, target, velocity, 0);
                addProjectileExtras(projectile, caster, modifiers);
                world.spawnEntity(projectile);
            }
            // TODO: SOUND
            //this.playSound(world, caster, ticksInUse, -1, modifiers);
            caster.swingHand(hand);
            return true;
        }

        return false;
    }

    // Copied from EntityMagicProjectile (ugh what a mess)
    private void aim(T throwable, LivingEntity caster, Entity target, float speed, float aimingError){


        throwable.prevY = caster.prevY + (double)caster.getStandingEyeHeight() - LAUNCH_Y_OFFSET;
        double dx = target.prevX - caster.prevX;
        double dy = !throwable.hasNoGravity() ? target.prevY + (double)(target.getHeight() / 3.0f) - throwable.prevY
                : target.prevY + (double)(target.getHeight() / 2.0f) - throwable.prevY;
        double dz = target.prevZ - caster.prevZ;
        double horizontalDistance = MathHelper.sqrt((float) (dx * dx + dz * dz));

        if(horizontalDistance >= 1.0E-7D){

            double dxNormalised = dx / horizontalDistance;
            double dzNormalised = dz / horizontalDistance;
            throwable.setPosition(caster.prevX + dxNormalised, throwable.prevY, caster.prevZ + dzNormalised);

            // Depends on the horizontal distance between the two entities and accounts for bullet drop,
            // but of course if gravity is ignored throwable should be 0 since there is no bullet drop.
            float bulletDropCompensation = !throwable.hasNoGravity() ? (float)horizontalDistance * 0.2f : 0;
            // It turns out that throwable method normalises the input (x, y, z) anyway
            throwable.setVelocity(dx, dy + (double)bulletDropCompensation, dz, speed, aimingError);
        }
    }

    /**
     * Does nothing by default, but can be overridden to call extra methods or set additional fields on the launched
     * projectile.
     */
    protected void addProjectileExtras(T projectile, LivingEntity caster, SpellModifiers modifiers){}


    // FIXME: OnEntityDamage event
    // https://github.com/Electroblob77/Wizardry/blob/1.12.2/src/main/java/electroblob/wizardry/spell/SpellThrowable.java#L51
}
