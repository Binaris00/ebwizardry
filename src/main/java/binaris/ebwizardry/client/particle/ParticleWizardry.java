package binaris.ebwizardry.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class ParticleWizardry extends SpriteBillboardParticle {
    /** The sprite of the particle. */
    SpriteProvider spriteProvider;
    /**
     * Properties of the particle, this is a helper class that contains all the properties of the particle.
     * And is used to set the values of the particle.
     * **/
    ParticleProperties properties;
    public ParticleWizardry(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.spriteProvider = spriteProvider;
        this.setSprite(spriteProvider);
    }
    /**
     * Used to set the properties of the particle
     * And set all the values of the particle based on the properties given.
     * @param properties The properties of the particle
     * **/
    private void setProperties(ParticleProperties properties){
        this.properties = properties;
        this.setColor(properties.getRed(), properties.getGreen(), properties.getBlue());
        this.setMaxAge(properties.getMaxAge());
        this.scale(properties.getScale());
        this.setPos(properties.getX(), properties.getY(), properties.getZ());
    }
    // ------------------- Methods override from Particle -------------------
    // These methods are overridden so that the properties can be used to set the values of the particle.
    // If you use any particle from the mod without setting the properties, it will use the default values.
    // ----------------------------------------------------------------------
    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(spriteProvider);
    }

    @Nullable
    protected Entity entity = null;
    public void setVelocity(double vx, double vy, double vz){
        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;
    }
    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
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
            if(properties != null){
                particle.setProperties(properties);

                // Reset properties, so that they don't get used for other particles by accident (E.g. particle command)
                properties = null;
            }
            return particle;
        }
        public static void setProperties(ParticleProperties propertiesV){
            properties = propertiesV;
        }


    }
}
