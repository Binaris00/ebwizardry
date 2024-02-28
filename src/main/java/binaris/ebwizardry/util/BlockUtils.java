package binaris.ebwizardry.util;

import binaris.ebwizardry.Wizardry;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public final class BlockUtils {

    /**
     * Returns true if the block at the given position is a tree block (or other 'solid' vegetation, such as cacti).
     * Used for structure generation.
     * @param world The world the block is in
     * @param pos The position of the block to be tested
     * @return True if the given block is a tree block, false if not.
     */
    public static boolean isTreeBlock(World world, BlockPos pos) {
        BlockState block = world.getBlockState(pos);
        return block.isIn(BlockTags.LOGS) || block.getBlock() instanceof CactusBlock;
    }
    @Nullable
    public static Integer getNearestSurface(World world, BlockPos pos, Direction direction, int range, boolean doubleSided, SurfaceCriteria criteria) {
        Integer surface = null;
        int currentBest = Integer.MAX_VALUE;

        for (int i = doubleSided ? -range : 0; i <= range && i < currentBest; i++) {
            BlockPos testPos = pos.offset(direction, i);

            if (criteria.test(world, testPos, direction)) {
                surface = (int) GeometryUtils.component(GeometryUtils.getFaceCentre(testPos, direction), direction.getAxis());
                currentBest = Math.abs(i);
            }
        }

        return surface;
    }

    @Nullable
    public static Integer getNearestFloor(World world, BlockPos pos, int range) {
        return getNearestSurface(world, pos, Direction.UP, range, true, SurfaceCriteria.COLLIDABLE);
    }

    @Nullable
    public static BlockPos findNearbyFloorSpace(Entity entity, int horizontalRange, int verticalRange) {
        World world = entity.getWorld();
        BlockPos origin = new BlockPos(entity.getBlockPos());
        return findNearbyFloorSpace(world, origin, horizontalRange, verticalRange);
    }

    @Nullable
    public static BlockPos findNearbyFloorSpace(World world, BlockPos origin, int horizontalRange, int verticalRange) {
        return findNearbyFloorSpace(world, origin, horizontalRange, verticalRange, true);
    }

    public static boolean canPlaceBlock(@Nullable Entity placer, World world, BlockPos pos) {
        if (world.isClient) {
            Wizardry.LOGGER.warn("BlockUtils#canPlaceBlock called from the client side! Blocks should be modified server-side only");
            return true;
        }

        if (!EntityUtil.canDamageBlocks(placer, world)) return false;

        if (world.isOutOfHeightLimit(pos)) return false;

        return !(placer instanceof PlayerEntity) || world.canPlayerModifyAt((PlayerEntity) placer, pos);
    }

    @Nullable
    public static BlockPos findNearbyFloorSpace(World world, BlockPos origin, int horizontalRange, int verticalRange, boolean lineOfSight) {
        List<BlockPos> possibleLocations = new ArrayList<>();

        final Vec3d centre = GeometryUtils.getCentre(origin);

        for (int x = -horizontalRange; x <= horizontalRange; x++) {
            for (int z = -horizontalRange; z <= horizontalRange; z++) {
                Integer y = getNearestFloor(world, origin.add(x, 0, z), verticalRange);

                if (y != null) {
                    BlockPos location = new BlockPos(origin.getX() + x, y, origin.getZ() + z);

                    possibleLocations.add(location);
                }
            }
        }

        if (possibleLocations.isEmpty()) {
            return null;
        } else {
            return possibleLocations.get(world.random.nextInt(possibleLocations.size()));
        }
    }

    public static boolean canBlockBeReplaced(World world, BlockPos pos, boolean excludeLiquids) {
        return (world.isAir(new BlockPos(pos)) || world.getBlockState(pos).isReplaceable()) && (!excludeLiquids || !world.getBlockState(pos).isLiquid());
    }

    public static boolean canBlockBeReplaced(World world, BlockPos pos) {
        return canBlockBeReplaced(world, pos, false);
    }

    public static boolean isWaterSource(BlockState state) {
        return state.getBlock() == Blocks.WATER && (state.getBlock() == Blocks.WATER) && state.get(FluidBlock.LEVEL) == 0;
    }

    public static boolean isLavaSource(BlockState state) {
        return state.getBlock() == Blocks.LAVA && (state.getBlock() == Blocks.LAVA) && state.get(FluidBlock.LEVEL) == 0;
    }

    public static boolean freeze(World world, BlockPos pos, boolean freezeLava) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (isWaterSource(state)) {
            world.setBlockState(pos, Blocks.ICE.getDefaultState());
        } else if (freezeLava && isLavaSource(state)) {
            world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
        } else if (freezeLava && (block == Blocks.LAVA)) {
            world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
        } else if (canBlockBeReplaced(world, pos.up()) && Blocks.SNOW.getDefaultState().canPlaceAt(world, pos.up())) {
            world.setBlockState(pos.up(), Blocks.SNOW.getDefaultState());
        } else {
            return false;
        }

        return true;
    }

    @FunctionalInterface
    public interface SurfaceCriteria {

        boolean test(World world, BlockPos pos, Direction side);

        default SurfaceCriteria flip() {
            return (world, pos, side) -> this.test(world, pos.offset(side), side.getOpposite());
        }

        static SurfaceCriteria basedOn(BiPredicate<World, BlockPos> condition) {
            return (world, pos, side) -> condition.test(world, pos) && !condition.test(world, pos.offset(side));
        }

        static SurfaceCriteria basedOn(Predicate<BlockState> condition) {
            return (world, pos, side) -> condition.test(world.getBlockState(pos)) && !condition.test(world.getBlockState(pos.offset(side)));
        }

        SurfaceCriteria COLLIDABLE = basedOn(b -> b.blocksMovement());

        /**
         * Surface criterion which defines a surface as the boundary between a block that is solid on the required side and
         * a block that is replaceable. This means the surface can be built on.
         */
        SurfaceCriteria BUILDABLE = (world, pos, side) -> world.getBlockState(pos).isSideSolidFullSquare(world, pos, side) && world.getBlockState(pos.offset(side)).isReplaceable();


        SurfaceCriteria SOLID_LIQUID_TO_AIR = (world, pos, side) -> (world.getBlockState(pos).isLiquid() || world.getBlockState(pos).isSideSolidFullSquare(world, pos, side) && world.isAir(pos.offset(side)));

        SurfaceCriteria NOT_AIR_TO_AIR = basedOn(World::isAir).flip();

        SurfaceCriteria COLLIDABLE_IGNORING_TREES = basedOn((world, pos) -> world.getBlockState(pos).blocksMovement() && !isTreeBlock(world, pos));
    }
}
