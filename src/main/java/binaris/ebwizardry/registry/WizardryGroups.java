package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.SpellUtils;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class WizardryGroups {
    public static final RegistryKey<ItemGroup> WIZARDRY = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(Wizardry.MODID, "wizardry"));
    public static final RegistryKey<ItemGroup> SPELLS = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(Wizardry.MODID, "spells"));
    public static final RegistryKey<ItemGroup> GEARS = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(Wizardry.MODID, "gears"));

    public static void use(){
        Registry.register(Registries.ITEM_GROUP, WIZARDRY, FabricItemGroup.builder()
                .displayName(Text.translatable("itemgroup.ebwizardry.wizardry"))
                .icon(() -> new ItemStack(WizardryItems.SILK))
                .build());

        Registry.register(Registries.ITEM_GROUP, SPELLS, FabricItemGroup.builder()
                .displayName(Text.translatable("itemgroup.ebwizardry.spells"))
                .icon(() -> new ItemStack(WizardryItems.SPELL_BOOK))
                        .entries((displayContext, entries) -> displayContext.lookup().getOptionalWrapper(Wizardry.REGISTRY_KEY).ifPresent((wrapper) -> {
                            addToItem(entries, wrapper, WizardryItems.SPELL_BOOK);
                            addToItem(entries, wrapper, WizardryItems.SCROLL);
                        }))


                .build());

        Registry.register(Registries.ITEM_GROUP, GEARS, FabricItemGroup.builder()
                .displayName(Text.translatable("itemgroup.ebwizardry.gears"))
                .icon(() -> new ItemStack(WizardryItems.SPARK_BOMB))
                .build());
    }



    private static void addToItem(ItemGroup.Entries entries, RegistryWrapper<Spell> registryWrapper, Item item){
        registryWrapper.streamEntries().map((entry) -> SpellUtils.setSpell(new ItemStack(item), entry.value())).forEach(entries::add);
    }
}
