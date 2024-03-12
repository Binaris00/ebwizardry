package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
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

        public static ParticleWizardry createParticle(ClientWorld clientWorld, Vec3d vec3d) {
            return new ParticleSpark(clientWorld, vec3d.x, vec3d.y, vec3d.z, spriteProvider);
        }
    }
}
