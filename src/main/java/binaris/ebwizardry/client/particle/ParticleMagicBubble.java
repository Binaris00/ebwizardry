package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ParticleMagicBubble extends ParticleWizardry{
    public ParticleMagicBubble(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, false);
        this.setColor(1, 1, 1);
        this.setBoundingBoxSpacing(0.02F, 0.02F);
        this.scale(this.random.nextFloat() * 0.6F + 0.2F);
        this.maxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        this.prevPosY += 0.002D;
        this.move(this.velocityX, this.velocityY, this.velocityY);
        this.velocityX *= 0.8500000238418579D;
        this.velocityY *= 0.8500000238418579D;
        this.velocityZ *= 0.8500000238418579D;

        if (this.maxAge-- <= 0) {
            this.markDead();
        }
    }

    public static class MagicBubbleFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public MagicBubbleFactory(SpriteProvider sprite) {
            spriteProvider = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleMagicBubble(world, x, y, z, spriteProvider);
        }
        public static ParticleWizardry createParticle(ClientWorld clientWorld, Vec3d vec3d) {
            return new ParticleMagicBubble(clientWorld, vec3d.x, vec3d.y, vec3d.z, spriteProvider);
        }
    }
}
