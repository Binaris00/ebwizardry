package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
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
                .build());

        Registry.register(Registries.ITEM_GROUP, GEARS, FabricItemGroup.builder()
                .displayName(Text.translatable("itemgroup.ebwizardry.gears"))
                .icon(() -> new ItemStack(WizardryItems.SPARK_BOMB))
                .build());
    }
}
