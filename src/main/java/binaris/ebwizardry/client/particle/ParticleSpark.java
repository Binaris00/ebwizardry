package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class ParticleSpark extends ParticleWizardry{
    public ParticleSpark(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, true);
        this.scale(1.4f);
        this.setColor(1, 1, 1);
        this.shaded = false;
        this.collidesWithWorld = false;
        this.setMaxAge(3);
        this.setSpriteForAge(spriteProvider);
    }

    @Deprecated
    public static class SparkFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public SparkFactory(SpriteProvider sprite) {
            spriteProvider = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleSpark(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld world, double x, double y, double z) {
            return new ParticleSpark(world, x, y, z, spriteProvider);
        }
    }
}
