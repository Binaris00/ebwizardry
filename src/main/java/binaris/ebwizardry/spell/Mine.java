package binaris.ebwizardry.spell;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.constant.Constants;
import binaris.ebwizardry.item.SpellCastingItem;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.util.BlockUtils;
import binaris.ebwizardry.util.EntityUtil;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
// TODO: Problems
public class Mine extends SpellRay{
    public Mine() {
        super("mine", UseAction.NONE, false);
        this.ignoreLivingEntities(true);
        this.particleSpacing(0.5);
    }

    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }

    @Override
    protected boolean onBlockHit(World world, BlockPos pos, Direction side, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if(caster instanceof PlayerEntity){
            if(caster.getActiveItem().getItem() instanceof SpellCastingItem){
                caster.swingHand(Hand.MAIN_HAND);
            }else if(caster.getOffHandStack().getItem() instanceof SpellCastingItem){
                caster.swingHand(Hand.OFF_HAND);
            }
        }

        if(!world.isClient){
            if(BlockUtils.isBlockUnbreakable(world, pos)) return false;
            if(!EntityUtil.canDamageBlocks(caster, world)) return false;

            BlockState state = world.getBlockState(pos);
            int harvestLevel = (int)((modifiers.get(SpellModifiers.POTENCY) - 1) / Constants.POTENCY_INCREASE_PER_TIER + 0.5f);
            if(harvestLevel > 0) harvestLevel--;

            Wizardry.LOGGER.info("No unbreakable block found");

            if(state.getBlock().getHardness() <= harvestLevel || harvestLevel >= 3){
                boolean flag = false;
                int blastUpgradeCount = (int)((modifiers.get(WizardryItems.BLAST_UPGRADE) - 1) / Constants.BLAST_RADIUS_INCREASE_PER_LEVEL + 0.5f);
                float radius = 0.5f + 0.73f * blastUpgradeCount;
                List<BlockPos> sphere = BlockUtils.getBlockSphere(pos, radius);

                for(BlockPos pos1 : sphere){
                    Wizardry.LOGGER.info("Blockpos: " + pos1);
                    if(BlockUtils.isBlockUnbreakable(world, pos1)) continue;
                    BlockState state1 = world.getBlockState(pos1);

                    if(state1.getBlock().getHardness() <= harvestLevel || harvestLevel >= 3){
                        if(caster instanceof ServerPlayerEntity serverPlayerEntity){
                            //TODO ITEMARTIFACTS
                            //boolean silkTouch = state1.canHarvestBlock(world, pos1, (Player)caster)
                             //       && ItemArtefact.isArtefactActive((Player)caster, WizardryItems.CHARM_SILK_TOUCH.get());
                            boolean silkTouch = false;

                            if(silkTouch){
                                flag = world.breakBlock(pos1, false);
                                if(flag){
                                    ItemStack stack = getSilkTouchDrop(state1);
                                    if(stack != null) Block.dropStack(world, pos1, stack);
                                }
                            }else{
                                flag = world.breakBlock(pos1, true);
                                //if(flag) state1.getBlock().dropExperience((ServerLevel) world, pos1, xp);
                            }

                        }else if(BlockUtils.canBreakBlock(caster, world, pos)){
                            flag = world.breakBlock(pos1, true) || flag;
                        }
                    }
                }

                return flag;
            }
        }else{
            return true;
        }
        Wizardry.LOGGER.info("Mine spell casted");
        return false;
    }

    @Override
    protected boolean onMiss(World world, @Nullable LivingEntity caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }

    @Override
    protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz) {
        // TODO: DUST PARTICLE
        //ParticleBuilder.create(WizardryParticles.DUST).pos(x, y, z).time(20 + world.random.nextInt(5)).clr(0.9f, 0.95f, 1)
        //        .shaded(false).spawn(world);
    }

    private ItemStack getSilkTouchDrop(BlockState state){
        Block block = state.getBlock();
        return block.getPickStack(null, null, null);
    }
}
