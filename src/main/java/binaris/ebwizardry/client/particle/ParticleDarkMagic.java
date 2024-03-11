package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class ParticleDarkMagic extends ParticleWizardry{
    public ParticleDarkMagic(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, true);
        this.setColor(1, 1, 1);
        this.setVelocity(0, 0.07000000298023224D, 0);
        this.scale(1.25F);
        this.setMaxAge((int) (8.0D / (Math.random() * 0.8D + 0.2D)));
        this.collidesWithWorld = true;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Deprecated
    public static class DarkMagicFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public DarkMagicFactory(SpriteProvider sprite) {
            spriteProvider = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleDarkMagic(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld world, double x, double y, double z) {
            return new ParticleDarkMagic(world, x, y, z, spriteProvider);
        }
    }
}
