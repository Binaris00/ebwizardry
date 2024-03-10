package binaris.ebwizardry.util;

import binaris.ebwizardry.config.WizardryConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public final class EntityUtil {
    public static List<LivingEntity> getLivingEntitiesInRange(World world, double x, double y, double z, double range) {
        return getEntitiesInRange(world, x, y, z, range, LivingEntity.class);
    }
    public static <T extends Entity> List<T> getEntitiesInRange(World world, double x, double y, double z, double range, Class<T> entityClass){
        Box boundingBox = new Box(x - range, y - range, z - range, x + range, y + range, z + range);
        Predicate<T> alwaysTrue = entity -> true;

        List<T> entities = world.getEntitiesByClass(entityClass, boundingBox, alwaysTrue);
        entities.removeIf(livingEntity -> livingEntity.squaredDistanceTo(x, y, z) > range);
        return entities;
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

    /**
     * Gets an entity from its UUID. If the UUID is known to belong to an {@code PlayerEntity}, use the more efficient
     * {@link World#getPlayerByUuid(UUID)} instead.
     *
     * @param world The world the entity is in
     * @param id The entity's UUID
     * @return The Entity that has the given UUID or null if no such entity exists in the specified world.
     */
    @Nullable
    public static Entity getEntityByUUID(World world, @Nullable UUID id){
        if(id == null) return null; // It would return null eventually, but there's no point even looking

        if(world instanceof ServerWorld serverWorld){
            for (Entity entity : serverWorld.iterateEntities()) {
                if(entity.getUuid().equals(id)) return entity;
            }
        }
        return null;
    }

    public static boolean attackEntityWithoutKnockback(Entity entity, DamageSource source, float amount) {
        Vec3d originalVec = entity.getVelocity();
        boolean succeeded = entity.damage(source, amount);
        entity.setVelocity(originalVec);
        return succeeded;
    }


    public static int getDefaultAimingError(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 10;
            case NORMAL -> 6;
            case HARD -> 2;
            default -> 10;
        };
    }
    public static <T extends Entity> List<T> getEntitiesWithinRadius(double radius, double x, double y, double z, World world, Class<T> entityType) {
        Box box = new Box(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
        List<T> entityList = world.getNonSpectatingEntities(entityType, box);
        for (int i = 0; i < entityList.size(); i++) {
            if (entityList.get(i).squaredDistanceTo(x, y, z) > radius) {
                entityList.remove(i);
                break;
            }
        }
        return entityList;
    }
    public static boolean canDamageBlocks(@Nullable Entity entity, World world) {
        if (entity == null) return WizardryConfig.dispenserBlockDamage;
        else if (entity instanceof PlayerEntity) return ((PlayerEntity) entity).canModifyBlocks() && WizardryConfig.playerBlockDamage;
        // TODO: Forge Event Factory
        //return ForgeEventFactory.getMobGriefingEvent(world, entity);
        return false;
    }
}
