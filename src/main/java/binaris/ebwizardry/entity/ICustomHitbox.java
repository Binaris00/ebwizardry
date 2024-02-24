package binaris.ebwizardry.entity;

import net.minecraft.util.math.Vec3d;
@Deprecated
public interface ICustomHitbox {
    Vec3d calculateIntercept(Vec3d origin, Vec3d endpoint, float fuzziness);

    boolean contains(Vec3d point);
}
