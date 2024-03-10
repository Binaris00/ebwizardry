package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

public class ParticleLeaf extends ParticleWizardry{
    public ParticleLeaf(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider);
        this.setVelocity(0, -0.03, 0);
        this.setMaxAge(10 + random.nextInt(5));
        this.scale *= 1.4f;
        this.gravityStrength = 0;
        this.collidesWithWorld = true;
        this.setColor(0.1f + 0.3f * random.nextFloat(), 0.5f + 0.3f * random.nextFloat(), 0.1f);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age > this.maxAge / 2) {
            this.setAlpha(1 - ((float) this.age - (float) (this.maxAge / 2)) / (float) this.maxAge);
        }
    }

    @Override
    public ParticleWizardry getParticle(ClientWorld world, double x, double y, double z) {
        return new ParticleLeaf(world, x, y, z, this.spriteProvider);
    }


}
