package binaris.ebwizardry.block;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.spell.Spell;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockSnare extends Block implements BlockEntityProvider {

    public BlockSnare(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if(entity instanceof LivingEntity){
            entity.damage(entity.getDamageSources().cactus(), Spells.SNARE.getFloatProperty(Spell.DAMAGE));

            ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,
                    Spells.SNARE.getIntProperty(Spell.EFFECT_DURATION),
                    Spells.SNARE.getIntProperty(Spell.EFFECT_STRENGTH)));

            if(!world.isClient){world.removeBlock(pos, false);}
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 1.0f, 0.0625f, 1.0f);
    }
    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        return null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if(world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP)){
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }
}
