package binaris.ebwizardry;

import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ExplosionSmokeParticle;

@Environment(EnvType.CLIENT)
public class WizardryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        WizardryEntities.registerClient();
        ParticleFactoryRegistry.getInstance().register(WizardryParticles.LIGHTNING, ExplosionSmokeParticle.Factory::new);
    }
}
