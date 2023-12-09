package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class WizardryTabs {
    public static final ItemGroup SPELLS_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Wizardry.MODID, "spells"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.spells"))
                    .icon(() -> new ItemStack(WizardryItems.SPELL_BOOK)).entries((displayContext, entries) -> {
                        entries.add(WizardryItems.SPELL_BOOK);
                    }).build());

    public static final ItemGroup GEARS_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Wizardry.MODID, "gears"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.gears"))
                    .icon(() -> new ItemStack(WizardryItems.SPARK_BOMB)).entries((displayContext, entries) -> {
                        entries.add(WizardryItems.SPARK_BOMB);
                        entries.add(WizardryItems.FIRE_BOMB);
                        entries.add(WizardryItems.POISON_BOMB);
                        entries.add(WizardryItems.SMOKE_BOMB);
                    }).build());



    public static void use(){}
}
