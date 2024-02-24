package binaris.ebwizardry.effects;

import net.minecraft.world.World;

public interface ICustomPotionParticles {
    void spawnCustomParticle(World world, double x, double y, double z);

    default boolean shouldMixColour() {
        return false;
    }
}
