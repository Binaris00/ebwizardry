package binaris.ebwizardry.block.entity;

import binaris.ebwizardry.registry.WizardryBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEntityTimer extends BlockEntity{
    /** The timer of the block */
    public int timer = 0;
    /** The max time of the timer */
    public int maxTimer;
    public BlockEntityTimer(BlockPos pos, BlockState state) {
        super(WizardryBlocks.BLOCK_TIMER_ENTITY, pos, state);
    }

    /**
     * Internal method to handle the ticking of the timer, use it in your {@link net.minecraft.block.BlockEntityProvider#getTicker(World, BlockState, BlockEntityType)}
     * Check to see if the timer has reached its max time, and if so, remove the block
     *
     * @throws IllegalStateException If the timer has not been initialized with a max time
     * **/
    public static void tick(World world, BlockPos pos, BlockState state, BlockEntityTimer entityTimer) {
        if(entityTimer.maxTimer == 0) throw new IllegalStateException(entityTimer + " has not been initialized with a max time");

        entityTimer.timer++;
        if(entityTimer.maxTimer > 0 && entityTimer.timer > entityTimer.maxTimer && !world.isClient){
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    /**
     * Sets the max time of the timer
     * @param time The time to set the timer to
     */
    public void setMaxTime(int time){
        this.maxTimer = time;
    }

    /**
     * Gets the max time of the timer
     * @return The current time of the timer
     */
    public int getMaxTime(){
        return maxTimer;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("timer", timer);
        nbt.putInt("maxTimer", maxTimer);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        timer = nbt.getInt("timer");
        maxTimer = nbt.getInt("maxTimer");
    }

    // TODO: Packets and stuff
}
