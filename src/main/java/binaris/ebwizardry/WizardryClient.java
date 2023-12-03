package binaris.ebwizardry;

import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.ExplosionSmokeParticle;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class WizardryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //Client
        EntityRendererRegistry.register(WizardryEntities.entitySparkBomb, FlyingItemEntityRenderer::new);
        ParticleFactoryRegistry.getInstance().register(WizardryParticles.LIGHTNING, ExplosionSmokeParticle.Factory::new);
    }
}
