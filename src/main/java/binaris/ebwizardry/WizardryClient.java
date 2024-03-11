package binaris.ebwizardry;

import binaris.ebwizardry.client.particle.ParticleDarkMagic;
import binaris.ebwizardry.item.IConjuredItem;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.registry.WizardryParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class WizardryClient implements ClientModInitializer {

    public static List<Item> conjuredItems = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        WizardryEntities.registerClient();

        WizardryParticles.registryParticlesClient();

        for(Item item : conjuredItems) {
            if(item instanceof IConjuredItem) {
                ((IConjuredItem) item).addAnimationPropertyOverrides();
            }
        }

        ModelPredicateProviderRegistry.register(WizardryItems.FLAMECATCHER, new Identifier("pull"), (stack, clientWorld, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? 0.0F : (float) (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
            }
        });
        ModelPredicateProviderRegistry.register(WizardryItems.FLAMECATCHER, new Identifier("pulling"), (stack, clientWorld, entity, seed) ->
                entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);

        ModelPredicateProviderRegistry.register(WizardryItems.SPECTRAL_BOW, new Identifier("pull"), (stack, clientWorld, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? 0.0F : (float) (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
            }
        });
        ModelPredicateProviderRegistry.register(WizardryItems.SPECTRAL_BOW, new Identifier("pulling"), (stack, clientWorld, entity, seed) ->
                entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);
    }


    public static void addConjuredItem(Item item) {
        conjuredItems.add(item);
    }
}
