package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class ParticleSparkle extends ParticleWizardry {
    public ParticleSparkle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, true);

        this.setColor(1, 1, 1);
        this.maxAge = 48 + this.random.nextInt(12);
        this.scale(1.2f);
        this.gravityStrength = 0;
        this.collidesWithWorld = false;
        this.shaded = false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age > this.maxAge / 2) {
            this.setAlpha(1 - ((float) this.age - (float) (this.maxAge / 2)) / (float) this.maxAge);
        }
    }

    @Deprecated
    public static class SparkleFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public SparkleFactory(SpriteProvider sprite) {
            spriteProvider = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleSparkle(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld world, double x, double y, double z) {
            return new ParticleSparkle(world, x, y, z, spriteProvider);
        }
    }

}
