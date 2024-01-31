package binaris.ebwizardry.client.particle;

import binaris.ebwizardry.Wizardry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
@Environment(EnvType.CLIENT)
public class ParticleCloud extends ParticleWizardry {
    public ParticleCloud(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider);
    }


    @Override
    public void tick() {
        super.tick();
        // Fading
        float fadeTime = this.maxAge * 0.3f;
        this.setAlpha(MathHelper.clamp(Math.min(this.age / fadeTime, (this.maxAge - this.age) / fadeTime), 0, 1));

        Wizardry.LOGGER.info("ParticleCloud ticked!");
    }


    @Environment(EnvType.CLIENT)
    public static class FactoryCloud extends WizardryFactory {
        SpriteProvider spriteProvider;
        private static ParticleProperties properties;

        public FactoryCloud(SpriteProvider spriteProvider) {
            super(spriteProvider);
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            ParticleCloud particle = new ParticleCloud(world, x, y, z, spriteProvider);
            if(properties != null){
                particle.setProperties(properties);

                // Reset properties, so that they don't get used for other particles by accident (E.g. particle command)
                properties = null;
            }

            return particle;
        }
    }

}
