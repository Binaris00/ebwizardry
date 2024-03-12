package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ParticleLightningPulse extends ParticleWizardry{
    public ParticleLightningPulse(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, true);
        this.scale = 32f;
        this.setColor(1, 1, 1);
        this.shaded = false;
        this.collidesWithWorld = false;
        this.setMaxAge(7);
        this.pitch = 90;
        this.yaw = 0;
    }

    public static class LightningPulseFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public LightningPulseFactory(SpriteProvider sprite) {
            spriteProvider = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleLightningPulse(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld clientWorld, Vec3d vec3d) {
            return new ParticleLightningPulse(clientWorld, vec3d.x, vec3d.y, vec3d.z, spriteProvider);
        }
    }
}
