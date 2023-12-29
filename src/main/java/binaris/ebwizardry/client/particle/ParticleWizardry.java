package binaris.ebwizardry.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class ParticleWizardry extends SpriteBillboardParticle {
    SpriteProvider spriteProvider;
    ParticleProperties properties;
    public ParticleWizardry(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.spriteProvider = spriteProvider;
        this.setSprite(spriteProvider);
    }

    public void setProperties(ParticleProperties properties){
        this.properties = properties;
        this.setColor(properties.getRed(), properties.getGreen(), properties.getBlue());
        this.setMaxAge(properties.getMaxAge());
        this.scale(properties.getScale());
        this.setPos(properties.getX(), properties.getY(), properties.getZ());
    }

    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
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
        if (properties != null && maxAge >= 0) {
            super.setMaxAge(maxAge);
        } else {
            super.setMaxAge(maxAge);
        }
    }

    @Override
    public Particle scale(float scale) {
        return properties != null ? super.scale(properties.getScale()) : super.scale(scale);
    }

    @Override
    public void setColor(float red, float green, float blue) {
        super.setColor(red, green, blue);
    }

    @Environment(EnvType.CLIENT)
    public static class WizardryFactory implements ParticleFactory<DefaultParticleType>{
        private final SpriteProvider spriteProvider;
        private static ParticleProperties properties;

        public WizardryFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            ParticleWizardry particle = new ParticleWizardry(world, x, y, z, spriteProvider);
            if(properties != null){particle.setProperties(properties);}
            return particle;
        }
        public static void setProperties(ParticleProperties propertiesV){
            properties = propertiesV;
        }


    }
}
