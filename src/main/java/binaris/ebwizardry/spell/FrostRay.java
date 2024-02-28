package binaris.ebwizardry.spell;

import binaris.ebwizardry.registry.WizardryEffects;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.EntityUtil;
import binaris.ebwizardry.util.ParticleBuilder;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FrostRay extends SpellRay{
    public FrostRay() {
        super("frost_ray", UseAction.NONE, true);
        this.particleVelocity(1);
        this.particleSpacing(0.5);
    }

    // TODO: MISSING SPELL FLAMERAY SOUNDS


    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if(target instanceof LivingEntity livingTarget){
            if(livingTarget.isOnFire()) livingTarget.extinguish();

            livingTarget.addStatusEffect(new StatusEffectInstance(WizardryEffects.FROST, (int) (getFloatProperty(EFFECT_DURATION) * modifiers.get(WizardryItems.DURATION_UPGRADE)), getIntProperty(EFFECT_STRENGTH)));
            if(ticksInUse % livingTarget.defaultMaxHealth == 1){
                float damage = getFloatProperty(DAMAGE) * modifiers.get(SpellModifiers.POTENCY);
                if(target instanceof BlazeEntity || target instanceof MagmaCubeEntity) damage *= 2;

                EntityUtil.attackEntityWithoutKnockback(livingTarget, livingTarget.getDamageSources().indirectMagic(caster, livingTarget), damage);
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
        float brightness = world.random.nextFloat();
        // TODO: PARTICLE FACTORY COLLIDE TRUE
        ParticleBuilder.create(WizardryParticles.SPARKLE).pos(x, y, z).velocity(vx, vy, vz).time(8 + world.random.nextInt(12))
                .color(0.4f + 0.6f * brightness, 0.6f + 0.4f * brightness, 1).spawn(world);
        ParticleBuilder.create(WizardryParticles.SNOW).pos(x, y, z).velocity(vx, vy, vz).time(8 + world.random.nextInt(12)).spawn(world);
    }
}
