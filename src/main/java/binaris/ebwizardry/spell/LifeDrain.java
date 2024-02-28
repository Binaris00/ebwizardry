package binaris.ebwizardry.spell;

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

public class LifeDrain extends SpellRay{
    public static final String HEAL_FACTOR = "heal_factor";

    public LifeDrain() {
        super("life_drain", UseAction.NONE, true);
        this.particleVelocity(-0.5);
        this.particleSpacing(0.4);
        this.soundValues(0.6f, 1, 0);
    }
    // TODO: SOUND VALUES
    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if (target instanceof LivingEntity livingTarget) {
            if (ticksInUse % 12 == 0) {
                float damage = getFloatProperty(DAMAGE) * modifiers.get(SpellModifiers.POTENCY);

                EntityUtil.attackEntityWithoutKnockback(livingTarget, livingTarget.getDamageSources().indirectMagic(caster, livingTarget), damage);
                if (caster != null) caster.heal(damage * getFloatProperty(HEAL_FACTOR));
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
        if (world.random.nextInt(5) == 0)
            ParticleBuilder.create(WizardryParticles.DARK_MAGIC).pos(x, y, z).color(0.1f, 0, 0).spawn(world);
        ParticleBuilder.create(WizardryParticles.SPARKLE).pos(x, y, z).velocity(vx, vy, vz).time(8 + world.random.nextInt(6))
                .color(0.5f, 0, 0).spawn(world);
    }
}
