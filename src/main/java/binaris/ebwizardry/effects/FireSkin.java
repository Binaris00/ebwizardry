package binaris.ebwizardry.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class FireSkin extends MagicStatusEffect{
    public FireSkin() {
        super(StatusEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void spawnCustomParticle(World world, double x, double y, double z) {
        world.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.extinguish(); // Remove fire from the entity
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
