package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ParticlePath extends ParticleWizardry{
    public ParticlePath(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, false);
        this.scale = 0.1f * 1.25f;
        this.gravityStrength = 0;
        this.shaded = false;
        this.collidesWithWorld = false;
        this.setColor(1, 1, 1);
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        if (this.age++ >= this.maxAge) {
            this.markDead();
        }

        this.move(this.velocityX, this.velocityY, this.velocityZ);

        if (this.age > this.maxAge / 2) {
            this.setAlpha(1.0F - 2 * (((float) this.age - (float) (this.maxAge / 2)) / (float) this.maxAge));
        }
    }

    public static class PathFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public PathFactory(SpriteProvider sprite) {
            spriteProvider = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticlePath(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld clientWorld, Vec3d vec3d) {
            return new ParticlePath(clientWorld, vec3d.x, vec3d.y, vec3d.z, spriteProvider);
        }
    }
}
