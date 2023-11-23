package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.block.BlockSnare;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

/**
 * Class responsible for defining, storing and registering all of wizardry's blocks. Also handles registry of the
 * tile entities.
 *
 * @author Originaly by Electroblob, ported by Binaris
 * @since Wizardry ???
 */
public abstract class WizardryBlocks {

    public static Block BLOCK_SNARE;



    public static void register(){
        BLOCK_SNARE = registerBlock("snare", new BlockSnare(FabricBlockSettings.create().breakInstantly().dropsNothing().sounds(BlockSoundGroup.GRASS).nonOpaque()));

    }
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Wizardry.MODID, name), block);
    }
    private static void registerBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, new Identifier(Wizardry.MODID, name), new BlockItem(block, new FabricItemSettings()));
    }
}
