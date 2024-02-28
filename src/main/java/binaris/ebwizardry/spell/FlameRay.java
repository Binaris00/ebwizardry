package binaris.ebwizardry.spell;

import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.EntityUtil;
import binaris.ebwizardry.util.ParticleBuilder;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FlameRay extends SpellRay{
    public FlameRay() {
        super("flame_ray", UseAction.NONE, true);
        this.particleVelocity(1);
        this.particleSpacing(0.5);
        this.soundValues(2.5f, 1, 0);
    }
    // TODO: MISSING SPELL FLAMERAY SOUNDS

    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if (target instanceof LivingEntity livingTarget) {
            if(ticksInUse % livingTarget.defaultMaxHealth == 1){
                livingTarget.setOnFireFor((int) (getFloatProperty(BURN_DURATION) * modifiers.get(WizardryItems.DURATION_UPGRADE)));
                EntityUtil.attackEntityWithoutKnockback(livingTarget, livingTarget.getDamageSources().indirectMagic(caster, livingTarget), getFloatProperty(DAMAGE) * modifiers.get(SpellModifiers.POTENCY));
            }
        }

        return true;
    }

    @Override
    protected boolean onBlockHit(World world, BlockPos pos, Direction side, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }

    @Override
    protected boolean onMiss(World world, @Nullable LivingEntity caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
        return true;
    }

    @Override
    protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz) {
        // TODO: PARTICLE FACTORY COLLIDE TRUE
        ParticleBuilder.create(WizardryParticles.MAGIC_FIRE).pos(x, y, z).velocity(vx, vy, vz).spawn(world);
        ParticleBuilder.create(WizardryParticles.MAGIC_FIRE).pos(x, y, z).velocity(vx, vy, vz).spawn(world);
    }
}
