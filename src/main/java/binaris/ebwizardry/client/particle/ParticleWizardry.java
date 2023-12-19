package binaris.ebwizardry.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ParticleWizardry extends SpriteBillboardParticle {
    SpriteProvider spriteProvider;
    protected ParticleWizardry(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.spriteProvider = spriteProvider;
        this.setSprite(spriteProvider);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(spriteProvider);
    }

    /** The entity this particle is linked to. The particle will move with this entity. */
    @Nullable
    protected Entity entity = null;

    /**
     * Sets the velocity of the particle.
     * @param vx The x velocity
     * @param vy The y velocity
     * @param vz The z velocity
     */
    public void setVelocity(double vx, double vy, double vz){
        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void setMaxAge(int maxAge) {
        super.setMaxAge(maxAge);
    }

    @Override
    public Particle scale(float scale) {
        return super.scale(scale);
    }

    @Override
    public void setColor(float red, float green, float blue) {
        super.setColor(red, green, blue);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType>{
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleWizardry(world, x, y, z, spriteProvider);
        }
    }
}
