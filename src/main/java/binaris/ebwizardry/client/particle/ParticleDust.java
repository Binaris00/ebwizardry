package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class ParticleDust extends ParticleWizardry {
    public ParticleDust(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, false);
        this.setBoundingBoxSpacing(0.01F, 0.01F);

        this.scale *= this.random.nextFloat() + 0.2F;
        this.maxAge = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
        this.setColor(1, 1, 1);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Deprecated
    public static class DustFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public DustFactory(SpriteProvider spriteProvider) {
            ParticleDust.DustFactory.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleDust(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld world, double x, double y, double z) {
            return new ParticleDust(world, x, y, z, spriteProvider);
        }
    }
}
