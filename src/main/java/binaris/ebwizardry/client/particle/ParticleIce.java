package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ParticleIce extends ParticleWizardry{
    public ParticleIce(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, false);
        this.collidesWithWorld = true;

        this.setColor(1, 1, 1);
        this.scale(1.2f);
        this.setGravity(true);
        this.shaded = false;

        // Set a random sprite from the spriteProvider
        this.setSprite(spriteProvider.getSprite(world.random));
    }

    public static class IceFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public IceFactory(SpriteProvider spriteProvider) {
            IceFactory.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleIce(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld clientWorld, Vec3d vec3d) {
            return new ParticleIce(clientWorld, vec3d.x, vec3d.y, vec3d.z, spriteProvider);
        }
    }
}
