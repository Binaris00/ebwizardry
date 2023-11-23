package binaris.ebwizardry.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.util.math.BlockPos;
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
            entity.damage(entity.getDamageSources().cactus(), 10);
            // Damage
            // entity.attackEntityFrom(source, Spells.snare.getProperty(Spell.DAMAGE).floatValue());

            //  Effect
            // ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,
            //        Spells.snare.getProperty(Spell.EFFECT_DURATION).intValue(),
            //        Spells.snare.getProperty(Spell.EFFECT_STRENGTH).intValue()));

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

}
