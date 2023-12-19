package binaris.ebwizardry;

import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class WizardryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        WizardryEntities.registerClient();
        WizardryParticles.registryParticlesClient();
    }
}
