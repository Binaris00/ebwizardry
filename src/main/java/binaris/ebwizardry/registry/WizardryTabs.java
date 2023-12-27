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
    public static final ItemGroup WIZARDRY_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Wizardry.MODID, "wizardry"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.wizardry"))
                    .icon(() -> new ItemStack(WizardryItems.SILK)).entries((displayContext, entries) -> {
                        entries.add(WizardryItems.SPELL_BOOK);
                        entries.add(WizardryItems.SCROLL);
                        entries.add(WizardryItems.SPARK_BOMB);
                        entries.add(WizardryItems.FIRE_BOMB);
                        entries.add(WizardryItems.POISON_BOMB);
                        entries.add(WizardryItems.SMOKE_BOMB);
                    }).build());
    public static final ItemGroup SPELLS_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Wizardry.MODID, "spells"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.spells"))
                    .icon(() -> new ItemStack(WizardryItems.SPELL_BOOK)).entries((displayContext, entries) -> {
                        entries.add(WizardryItems.SPELL_BOOK);
                        entries.add(WizardryItems.SCROLL);
                    }).build());

    public static final ItemGroup GEARS_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Wizardry.MODID, "gears"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.gears"))
                    .icon(() -> new ItemStack(WizardryItems.SPARK_BOMB)).entries((displayContext, entries) -> {
                        entries.add(WizardryItems.SPARK_BOMB);
                        entries.add(WizardryItems.FIRE_BOMB);
                        entries.add(WizardryItems.POISON_BOMB);
                        entries.add(WizardryItems.SMOKE_BOMB);
                        entries.add(WizardryItems.WIZARD_HAT);
                        entries.add(WizardryItems.WIZARD_ROBE);
                        entries.add(WizardryItems.WIZARD_LEGGINGS);
                        entries.add(WizardryItems.WIZARD_BOOTS);
                        entries.add(WizardryItems.WIZARD_HAT_FIRE);
                        entries.add(WizardryItems.WIZARD_ROBE_FIRE);
                        entries.add(WizardryItems.WIZARD_LEGGINGS_FIRE);
                        entries.add(WizardryItems.WIZARD_BOOTS_FIRE);
                        entries.add(WizardryItems.WIZARD_HAT_ICE);
                        entries.add(WizardryItems.WIZARD_ROBE_ICE);
                        entries.add(WizardryItems.WIZARD_LEGGINGS_ICE);
                        entries.add(WizardryItems.WIZARD_BOOTS_ICE);
                        entries.add(WizardryItems.WIZARD_HAT_LIGHTNING);
                        entries.add(WizardryItems.WIZARD_ROBE_LIGHTNING);
                        entries.add(WizardryItems.WIZARD_LEGGINGS_LIGHTNING);
                        entries.add(WizardryItems.WIZARD_BOOTS_LIGHTNING);
                        entries.add(WizardryItems.WIZARD_HAT_EARTH);
                        entries.add(WizardryItems.WIZARD_ROBE_EARTH);
                        entries.add(WizardryItems.WIZARD_LEGGINGS_EARTH);
                        entries.add(WizardryItems.WIZARD_BOOTS_EARTH);
                        entries.add(WizardryItems.WIZARD_HAT_SORCERY);
                        entries.add(WizardryItems.WIZARD_ROBE_SORCERY);
                        entries.add(WizardryItems.WIZARD_LEGGINGS_SORCERY);
                        entries.add(WizardryItems.WIZARD_BOOTS_SORCERY);
                        entries.add(WizardryItems.WIZARD_HAT_HEALING);
                        entries.add(WizardryItems.WIZARD_ROBE_HEALING);
                        entries.add(WizardryItems.WIZARD_LEGGINGS_HEALING);
                        entries.add(WizardryItems.WIZARD_BOOTS_HEALING);
                        entries.add(WizardryItems.WIZARD_HAT_NECROMANCY);
                        entries.add(WizardryItems.WIZARD_ROBE_NECROMANCY);
                        entries.add(WizardryItems.WIZARD_LEGGINGS_NECROMANCY);
                        entries.add(WizardryItems.WIZARD_BOOTS_NECROMANCY);
                    }).build());



    public static void use(){}
}
