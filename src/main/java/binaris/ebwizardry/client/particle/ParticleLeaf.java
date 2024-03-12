package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ParticleLeaf extends ParticleWizardry{
    public ParticleLeaf(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, false);
        this.setVelocity(0, -0.03, 0);
        this.setMaxAge(10 + random.nextInt(5));
        this.scale(1.8f);
        this.gravityStrength = 0;
        this.collidesWithWorld = true;
        this.setColor(0.1f + 0.3f * random.nextFloat(), 0.5f + 0.3f * random.nextFloat(), 0.1f);

        // Set a random sprite from the spriteProvider
        this.setSprite(spriteProvider.getSprite(world.random));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age > this.maxAge / 2) {
            this.setAlpha(1 - ((float) this.age - (float) (this.maxAge / 2)) / (float) this.maxAge);
        }
    }

    public static class LeafFactory implements ParticleFactory<DefaultParticleType>{
        static SpriteProvider spriteProvider;

        public LeafFactory(SpriteProvider spriteProvider) {
            LeafFactory.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleLeaf(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld clientWorld, Vec3d vec3d) {
            return new ParticleLeaf(clientWorld, vec3d.x, vec3d.y, vec3d.z, spriteProvider);
        }
    }
}
