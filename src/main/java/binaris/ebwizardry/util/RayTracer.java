package binaris.ebwizardry.util;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.entity.ICustomHitbox;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Contains a number of static methods that perform raytracing and related functions. This was split off from
 * {@code WizardryUtilities} as of wizardry 4.2 in an effort to make the code easier to navigate.
 */
public final class RayTracer {
    private RayTracer(){} // No instances!

    /**
     * Performs a ray trace for blocks and entities, starting at the given origin and finishing at the given endpoint.
     * As of wizardry 4.2, the ray tracing methods have been rewritten to be more user-friendly and implement proper
     * aim assist.
     * <p></p>
     * <i>N.B. It is possible to ignore entities entirely by passing in a {@code Predicate} that is always false;
     * however, in this specific case it is more efficient to use
     * {@link World#rayTraceBlocks(Vec3d, Vec3d, boolean, boolean, boolean)} or one of its overloads.</i>
     *
     * @param world The world in which to perform the ray trace.
     * @param origin A vector representing the coordinates of the start point of the ray trace.
     * @param endpoint A vector representing the coordinates of the finish point of the ray trace.
     * @param aimAssist In addition to direct hits, the ray trace will also hit entities that are up to this distance
     * from its path. For a normal ray trace, this should be 0. Values greater than 0 will give an 'aim assist' effect.
     * @param hitLiquids True to return hits on the surfaces of liquids, false to ignore liquid blocks as if they were
     * not there. {@code ignoreUncollidables} must be set to false for this setting to have an effect.
     * @param ignoreUncollidables Whether blocks with no collisions should be ignored
     * @param returnLastUncollidable If blocks with no collisions are ignored, whether to return the last one (useful if,
     *                               for example, you want to replace snow layers or tall grass)
     * @param entityType The class of entities to include; all other entities will be ignored.
     * @param filter A {@link Predicate} which filters out entities that can be ignored; often used to exclude the
     * player that is performing the ray trace.
     *
     * @return A {@link RayTraceResult} representing the object that was hit, which may be an entity, a block or
     * nothing. Returns {@code null} only if the origin and endpoint are within the same block and no entity was hit.
     *
     * @see RayTracer#standardEntityRayTrace(World, Entity, double, boolean)
     * @see RayTracer#standardBlockRayTrace(World, EntityLivingBase, double, boolean)
     */
    @Nullable
    public static HitResult rayTrace(World world, PlayerEntity caster, Vec3d origin, Vec3d endpoint, float aimAssist,
                                     boolean hitLiquids, Class<? extends Entity> entityType, Predicate<? super Entity> filter){
        HitResult result = world.raycast(new RaycastContext(origin, endpoint, RaycastContext.ShapeType.COLLIDER, hitLiquids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, caster));

        if (result != null) {
            endpoint = result.getPos();
        }
        float borderSize = 1 + aimAssist;
        Box searchVolume = new Box(origin.x, origin.y, origin.z, endpoint.x, endpoint.y, endpoint.z).expand(borderSize, borderSize, borderSize);
        List<? extends Entity> entities = world.getNonSpectatingEntities(entityType, searchVolume);
        entities.removeIf(filter);

        Entity closestHitEntity = null;
        Vec3d closestHitPosition = endpoint;
        Box entityBounds;
        Vec3d intercept = null;

        for (Entity entity : entities) {
            float fuzziness = EntityUtil.isLiving(entity) ? aimAssist : 0;

            if (entity instanceof ICustomHitbox) {
                intercept = ((ICustomHitbox) entity).calculateIntercept(origin, endpoint, fuzziness);
            } else {
                entityBounds = entity.getBoundingBox();

                if (entityBounds != null) {
                    float entityBorderSize = entity.getTargetingMargin();
                    if (entityBorderSize != 0)
                        entityBounds = entityBounds.expand(entityBorderSize, entityBorderSize, entityBorderSize);

                    if (fuzziness != 0) entityBounds = entityBounds.expand(fuzziness, fuzziness, fuzziness);

                    Optional<Vec3d> hit = entityBounds.raycast(origin, endpoint);
                    if (hit.isPresent()) {
                        intercept = hit.get();
                    }
                }
            }

            if (intercept != null) {
                float currentHitDistance = (float) intercept.distanceTo(origin);
                float closestHitDistance = (float) closestHitPosition.distanceTo(origin);
                if (currentHitDistance < closestHitDistance) {
                    closestHitEntity = entity;
                    closestHitPosition = intercept;
                }
            }
        }

        if (closestHitEntity != null) {
            result = new EntityHitResult(closestHitEntity, closestHitPosition);
        }

        return result;
    }

    public static Predicate<Entity> ignoreEntityFilter(Entity entity) {
        return e -> e == entity || (e instanceof LivingEntity && ((LivingEntity) e).deathTime > 0);
    }
}
