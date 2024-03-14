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

public class LightningRay extends SpellRay{
    public LightningRay() {
        super("lightning_ray", UseAction.NONE, true);
    }

    // TODO: MISSING SPELL LIGHTNING RAY SOUNDS


    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if(target instanceof LivingEntity livingTarget){
            EntityUtil.attackEntityWithoutKnockback(livingTarget, livingTarget.getDamageSources().indirectMagic(caster, livingTarget),
                    getFloatProperty(DAMAGE) * modifiers.get(SpellModifiers.POTENCY));

            if(world.isClient){
                if(ticksInUse % 3 == 0){
                    ParticleBuilder.create(WizardryParticles.LIGHTNING).entity(caster).pos(caster != null ? origin.subtract(caster.getPos()) : origin).target(target).spawn(world);
                }

                for (int i = 0; i < 5; i++) {
                    ParticleBuilder.create(WizardryParticles.SPARK, target).spawn(world);
                }
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
        if (world.isClient && ticksInUse % 4 == 0) {
            double freeRange = 0.8 * getRange();

            if (caster != null) {
                ParticleBuilder.create(WizardryParticles.LIGHTNING).entity(caster).pos(origin.subtract(caster.getPos())).length(freeRange).spawn(world);
            } else {
                ParticleBuilder.create(WizardryParticles.LIGHTNING).pos(origin).target(origin.add(direction.multiply(freeRange))).spawn(world);
            }
        }

        return true;
    }
}
