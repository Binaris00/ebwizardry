package binaris.ebwizardry.effects;

import binaris.ebwizardry.constant.Constants;
import binaris.ebwizardry.registry.WizardryEffects;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.world.World;

public class Frost extends MagicStatusEffect {
    public Frost() {
        super(StatusEffectCategory.HARMFUL, 0);
        this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "35dded48-2f19-4541-8510-b29e2dc2cd51", -Constants.FROST_SLOWNESS_PER_LEVEL, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity.isOnFire()){
            if(entity.hasStatusEffect(WizardryEffects.FROST)){
                entity.removeStatusEffect(WizardryEffects.FROST);
                entity.setOnFireFor(0);
            }
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void spawnCustomParticle(World world, double x, double y, double z) {
        ParticleBuilder.create(WizardryParticles.SNOW).pos(x, y, z).time(15 + world.random.nextInt(5)).spawn(world);
    }
}
