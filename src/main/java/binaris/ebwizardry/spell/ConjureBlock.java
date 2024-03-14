package binaris.ebwizardry.spell;

import binaris.ebwizardry.block.entity.BlockEntityTimer;
import binaris.ebwizardry.registry.WizardryBlocks;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.BlockUtils;
import binaris.ebwizardry.util.ParticleBuilder;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ConjureBlock extends SpellRay{
    public static final String BLOCK_LIFETIME = "block_lifetime";
    public ConjureBlock() {
        super("conjure_block", UseAction.NONE, false);
        this.ignoreLivingEntities(true);
    }

    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }

    @Override
    protected boolean onBlockHit(World world, BlockPos pos, Direction side, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if(caster != null && caster.isSneaking() && world.getBlockState(pos).getBlock() == WizardryBlocks.SPECTRAL_BLOCK){
            if(!world.isClient){
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            }else{
                ParticleBuilder.create(WizardryParticles.FLASH).pos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5).scale(3)
                        .color(0.75f, 1, 0.85f).spawn(world);
            }
            return true;
        }

        pos = pos.offset(side);

        if(world.isClient){
            ParticleBuilder.create(WizardryParticles.FLASH).pos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5).scale(3)
                    .color(0.75f, 1, 0.85f).spawn(world);
        }

        if(BlockUtils.canBlockBeReplaced(world, pos)){
            if(!world.isClient){
                world.setBlockState(pos, WizardryBlocks.SPECTRAL_BLOCK.getDefaultState());

                if(world.getBlockEntity(pos) instanceof BlockEntityTimer entityTimer){
                    entityTimer.setMaxTime((int) getFloatProperty(BLOCK_LIFETIME) * (int) modifiers.get(WizardryItems.DURATION_UPGRADE));
                }
            }

            return true;
        }

        return false;
    }

    @Override
    protected boolean onMiss(World world, @Nullable LivingEntity caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }
}
