package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class ParticleMagicFire extends ParticleWizardry {
    public ParticleMagicFire(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, true);
        this.setColor(1, 1, 1);
        this.alpha = 1;
        this.maxAge = 12 + random.nextInt(4);
        this.shaded = false;
        this.collidesWithWorld = true;

        // Set a random sprite from the spriteProvider
        this.setSprite(spriteProvider.getSprite(world.random));
    }

    @Deprecated
    public static class MagicFireFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public MagicFireFactory(SpriteProvider sprite) {
            spriteProvider = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleMagicFire(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld world, double x, double y, double z) {
            return new ParticleMagicFire(world, x, y, z, spriteProvider);
        }
    }
}
