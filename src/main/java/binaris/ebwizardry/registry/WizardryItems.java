package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.item.ItemSparkBomb;
import binaris.ebwizardry.item.ItemSpellBook;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public abstract class WizardryItems {
    public static Item SPELL_BOOK;
    public static Item SPARK_BOMB;


    public static void register(){
        SPELL_BOOK = registerItem("spell_book", new ItemSpellBook(new FabricItemSettings().maxCount(16)));
        SPARK_BOMB = registerItem("spark_bomb", new ItemSparkBomb(new FabricItemSettings().maxCount(16)));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Wizardry.MODID, name), item);
    }
}
