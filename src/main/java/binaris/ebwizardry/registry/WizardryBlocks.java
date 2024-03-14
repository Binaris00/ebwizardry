package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.block.BlockSnare;
import binaris.ebwizardry.block.SpectralBlock;
import binaris.ebwizardry.block.entity.BlockEntityTimer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

/**
 * Class responsible for defining, storing and registering all of wizardry's blocks. Also handles registry of the
 * Block entities
 *
 * @author Originaly by Electroblob, ported by Binaris
 */
public final class WizardryBlocks {

    public static Block BLOCK_SNARE;
    public static Block SPECTRAL_BLOCK;
    public static BlockEntityType<BlockEntityTimer> BLOCK_TIMER_ENTITY;


    public static void register(){
        BLOCK_SNARE = registerBlock("snare", new BlockSnare(FabricBlockSettings.create().breakInstantly().dropsNothing().sounds(BlockSoundGroup.GRASS).nonOpaque()));
        SPECTRAL_BLOCK = registerBlock("spectral_block", new SpectralBlock());

        BLOCK_TIMER_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Wizardry.MODID, "timer_block_entity"),
                FabricBlockEntityTypeBuilder.create(BlockEntityTimer::new, SPECTRAL_BLOCK).build());

    }
    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(Wizardry.MODID, name), block);
    }
    private static void registerBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, new Identifier(Wizardry.MODID, name), new BlockItem(block, new FabricItemSettings()));
    }
}
