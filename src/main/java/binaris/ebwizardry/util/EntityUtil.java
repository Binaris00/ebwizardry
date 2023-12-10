package binaris.ebwizardry.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public abstract class EntityUtil {

    public static List<LivingEntity> getLivingEntitiesInRange(World world, double x, double y, double z, double range){
        Box boundingBox = new Box(x - range, y - range, z - range, x + range, y + range, z + range);
        Predicate<LivingEntity> alwaysTrue = entity -> true;


        List<LivingEntity> livingEntities = world.getEntitiesByClass(LivingEntity.class, boundingBox, alwaysTrue);
        livingEntities.removeIf(livingEntity -> livingEntity.squaredDistanceTo(x, y, z) > range);
        return livingEntities;
    }
}
