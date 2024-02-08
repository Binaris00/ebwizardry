package binaris.ebwizardry.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public final class EntityUtil {

    public static List<LivingEntity> getLivingEntitiesInRange(World world, double x, double y, double z, double range){
        Box boundingBox = new Box(x - range, y - range, z - range, x + range, y + range, z + range);
        Predicate<LivingEntity> alwaysTrue = entity -> true;


        List<LivingEntity> livingEntities = world.getEntitiesByClass(LivingEntity.class, boundingBox, alwaysTrue);
        livingEntities.removeIf(livingEntity -> livingEntity.squaredDistanceTo(x, y, z) > range);
        return livingEntities;
    }

    /**
     * Returns true if the given entity is an EntityLivingBase and not an armour stand; makes the code a bit neater.
     * This was added because armour stands are a subclass of EntityLivingBase, but shouldn't necessarily be treated
     * as living entities - this depends on the situation. <i>The given entity can safely be cast to EntityLivingBase
     * if this method returns true.</i>
     */
    // In my opinion, it's a bad design choice to have armour stands extend EntityLivingBase directly - it would be
    // better to make a parent class which is extended by both armour stands and EntityLivingBase and contains only
    // the code required by both.
    public static boolean isLiving(Entity entity){
        return entity instanceof LivingEntity && !(entity instanceof ArmorStandEntity);
    }
}
