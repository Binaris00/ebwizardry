package binaris.ebwizardry.block;

import binaris.ebwizardry.block.entity.BlockEntityTimer;
import binaris.ebwizardry.registry.WizardryBlocks;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.ParticleBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
// FIXME: Don't rendering like a glass block
@Deprecated
public class SpectralBlock extends BlockWithEntity {
    public SpectralBlock() {
        super(FabricBlockSettings.create().sounds(BlockSoundGroup.GLASS).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        BlockEntityTimer blockEntityTimer = new BlockEntityTimer(pos, state);
        blockEntityTimer.setMaxTime(1200);

        return blockEntityTimer;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, WizardryBlocks.BLOCK_TIMER_ENTITY, BlockEntityTimer::tick);
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0f;
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        for(int i=0; i<2; i++){
            ParticleBuilder.create(WizardryParticles.DUST)
                    .pos(pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble())
                    .time((int)(16.0D / (Math.random() * 0.8D + 0.2D)))
                    .color(0.4f + random.nextFloat() * 0.2f, 0.6f + random.nextFloat() * 0.4f, 0.6f + random.nextFloat() * 0.4f)
                    .shaded(true).spawn(world);
        }

        super.randomDisplayTick(state, world, pos, random);
    }
}
