package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class ParticleCloud extends ParticleWizardry{
    public ParticleCloud(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, false);
        this.setColor(1, 1, 1);
        this.maxAge = 48 + this.random.nextInt(12);
        this.scale(3);
        this.setGravity(false);
        this.setAlpha(0);
        this.collidesWithWorld = false;
        this.shaded = true;
    }

    @Override
    public void tick() {
        super.tick();

        float fadeTime = this.maxAge * 0.3f;
        this.setAlpha(MathHelper.clamp(Math.min(this.age / fadeTime, (this.maxAge - this.age) / fadeTime), 0, 1));
    }

    @Deprecated
    public static class CloudFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public CloudFactory(SpriteProvider spriteProvider) {
            ParticleCloud.CloudFactory.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleCloud(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld world, double x, double y, double z) {
            return new ParticleCloud(world, x, y, z, spriteProvider);
        }
    }
}
