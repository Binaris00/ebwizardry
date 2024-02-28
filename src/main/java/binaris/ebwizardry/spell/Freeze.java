package binaris.ebwizardry.spell;

import binaris.ebwizardry.registry.WizardryEffects;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.BlockUtils;
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
// FIXME: Particle problems
public class Freeze extends SpellRay{
    public Freeze() {
        super("freeze", UseAction.NONE, false);
        this.soundValues(1, 1.4f, 0.4f);
        this.hitLiquids(true);
        this.ignoreUncollidables(false);
    }

    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if (EntityUtil.isLiving(target)) {
            if (target instanceof BlazeEntity || target instanceof MagmaCubeEntity) {
                target.damage(target.getDamageSources().indirectMagic(caster, target), getFloatProperty(DAMAGE) * modifiers.get(SpellModifiers.POTENCY));
            }

            if(target instanceof LivingEntity livingEntity){
                livingEntity.addStatusEffect(new StatusEffectInstance(WizardryEffects.FROST, (int) (getFloatProperty(EFFECT_DURATION) * modifiers.get(WizardryItems.DURATION_UPGRADE)), getIntProperty(EFFECT_STRENGTH)));
            }

            if (target.isOnFire()) target.extinguish();

            return true;
        }

        return false;
    }

    @Override
    protected boolean onBlockHit(World world, BlockPos pos, Direction side, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if (!world.isClient && BlockUtils.canPlaceBlock(caster, world, pos)) {
            BlockUtils.freeze(world, pos, true);
        }

        return true;
    }

    @Override
    protected boolean onMiss(World world, @Nullable LivingEntity caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }

    @Override
    protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz) {
        float brightness = 0.5f + (world.random.nextFloat() / 2);
        ParticleBuilder.create(WizardryParticles.SPARKLE).pos(x, y, z).time(12 + world.random.nextInt(8))
                .color(brightness, brightness + 0.1f, 1).spawn(world);
        ParticleBuilder.create(WizardryParticles.SNOW).pos(x, y, z).spawn(world);

    }
}
