package binaris.ebwizardry.spell;

import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.ParticleBuilder;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Wither extends SpellRay {
    public Wither() {
        super("wither", UseAction.NONE, false);
        this.soundValues(1, 1.1f, 0.2f);
    }

    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if(target instanceof LivingEntity livingTarget){
            livingTarget.damage(livingTarget.getDamageSources().indirectMagic(caster, livingTarget), getFloatProperty(DAMAGE) * modifiers.get(SpellModifiers.POTENCY));
            livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, (int) (getFloatProperty(EFFECT_DURATION) * modifiers.get(WizardryItems.DURATION_UPGRADE)),
                    (int) (getFloatProperty(EFFECT_STRENGTH) + SpellBuff.getStandardBonusAmplifier(modifiers.get(SpellModifiers.POTENCY)))));
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
        ParticleBuilder.create(WizardryParticles.DARK_MAGIC).pos(x, y, z).color(0.1f, 0, 0).spawn(world);
        ParticleBuilder.create(WizardryParticles.SPARKLE).pos(x, y, z).time(12 + world.random.nextInt(8)).color(0.1f, 0, 0.05f).spawn(world);
    }
}
