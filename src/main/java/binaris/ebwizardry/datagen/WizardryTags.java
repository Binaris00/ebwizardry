package binaris.ebwizardry.datagen;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.spell.Spell;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class WizardryTags extends FabricTagProvider {
    public static final TagKey<Spell> SPELL_TAG_KEY = TagKey.of(RegistryKey.ofRegistry(new Identifier(Wizardry.MODID, "spells")), new Identifier(Wizardry.MODID, "spells"));


    /**
     * Constructs a new {@link FabricTagProvider} with the default computed path.
     *
     * <p>Common implementations of this class are provided.
     *
     * @param output           the {@link FabricDataOutput} instance
     * @param registryKey
     * @param registriesFuture the backing registry for the tag type
     */
    public WizardryTags(FabricDataOutput output, RegistryKey registryKey, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registryKey, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(SPELL_TAG_KEY)
                .add(Spells.NONE);
    }
}
