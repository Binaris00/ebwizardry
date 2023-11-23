package binaris.ebwizardry;

import binaris.ebwizardry.registry.WizardryEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class WizardryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //Client
        EntityRendererRegistry.register(WizardryEntities.entitySparkBomb, FlyingItemEntityRenderer::new);

    }
}
