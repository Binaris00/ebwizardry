package binaris.ebwizardry.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.*;

public final class GeometryUtils {

    public static Vec3d getCentre(BlockPos pos) {
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 0.5, 0.5);
    }

    public static Vec3d getCentre(Box box) {
        return new Vec3d(box.minX + (box.maxX - box.minX) * 0.5, box.minY + (box.maxY - box.minY) * 0.5, box.minZ + (box.maxZ - box.minZ) * 0.5);
    }

    public static Vec3d getCentre(Entity entity) {
        return new Vec3d(entity.getX(), entity.getY() + entity.getHeight() / 2, entity.getZ());
    }

    public static Vec3d getFaceCentre(BlockPos pos, Direction face) {
        return getCentre(pos).add(new Vec3d(face.getUnitVector()).multiply(0.5));
    }

    public static double component(Vec3d vec, Direction.Axis axis) {
        return new double[]{vec.x, vec.y, vec.z}[axis.ordinal()];
    }

    public static int component(Vec3i vec, Direction.Axis axis) {
        return new int[]{vec.getX(), vec.getY(), vec.getZ()}[axis.ordinal()];
    }


}
