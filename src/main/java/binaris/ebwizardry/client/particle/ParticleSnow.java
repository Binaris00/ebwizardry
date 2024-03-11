package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class ParticleSnow extends ParticleWizardry{
    public ParticleSnow(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, true);
        this.setVelocity(0, -0.02, 0);
        this.scale(0.6f);
        this.gravityStrength = 0;
        this.collidesWithWorld = true;
        this.setMaxAge(40 + random.nextInt(10));
        this.setColor(0.9f + 0.1f * random.nextFloat(), 0.95f + 0.05f * random.nextFloat(), 1);

        // Set a random sprite from the spriteProvider
        this.setSprite(spriteProvider.getSprite(world.random));
    }

    @Deprecated
    public static class SnowFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public SnowFactory(SpriteProvider sprite) {
            spriteProvider = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleSnow(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld world, double x, double y, double z) {
            return new ParticleSnow(world, x, y, z, spriteProvider);
        }
    }
}
