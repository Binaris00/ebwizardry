package binaris.ebwizardry.spell;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.entity.construct.EntityMagicConstruct;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.util.BlockUtils;
import binaris.ebwizardry.util.RayTracer;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.function.Function;

public class SpellConstructRanged<T extends EntityMagicConstruct> extends SpellConstruct<T>{
    protected boolean hitLiquids = false;

    protected boolean ignoreUncollidables = false;

    public SpellConstructRanged(String name, Function<World, T> constructFactory, boolean permanent){
        this(Wizardry.MODID, name, constructFactory, permanent);
    }
    public SpellConstructRanged(String modID, String name, Function<World, T> constructFactory, boolean permanent) {
        super(modID, name, UseAction.NONE, constructFactory, permanent);
        this.npcSelector((e, o) -> true);
    }
    public Spell hitLiquids(boolean hitLiquids) {
        this.hitLiquids = hitLiquids;
        return this;
    }

    public Spell ignoreUncollidables(boolean ignoreUncollidables) {
        this.ignoreUncollidables = ignoreUncollidables;
        return this;
    }

    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers) {
        double range = getDoubleProperty(RANGE) * modifiers.get(WizardryItems.RANGE_UPGRADE);
        HitResult rayTrace = RayTracer.standardBlockRayTrace(world, caster, range, hitLiquids, ignoreUncollidables, false);

        if (rayTrace != null && rayTrace.getType() == HitResult.Type.BLOCK && (((BlockHitResult) rayTrace).getSide() == Direction.UP || !requiresFloor)) {
            if (!world.isClient) {
                double x = rayTrace.getPos().x;
                double y = rayTrace.getPos().y;
                double z = rayTrace.getPos().z;

                if (!spawnConstruct(world, x, y, z, ((BlockHitResult) rayTrace).getSide(), caster, modifiers))
                    return false;
            }
        } else if (!requiresFloor) {
            if (!world.isClient) {
                Vec3d look = caster.getRotationVector();

                double x = caster.getX() + look.x * range;
                double y = caster.getY() + caster.getStandingEyeHeight() + look.y * range;
                double z = caster.getZ() + look.z * range;

                if (!spawnConstruct(world, x, y, z, null, caster, modifiers)) return false;
            }
        } else {
            return false;
        }

        this.playSound(world, caster, ticksInUse, -1, modifiers);
        return true;
    }

    @Override
    public boolean cast(World world, LivingEntity caster, Hand hand, int ticksInUse, LivingEntity target, SpellModifiers modifiers) {
        double range = getDoubleProperty(RANGE) * modifiers.get(WizardryItems.RANGE_UPGRADE);
        Vec3d origin = caster.getCameraPosVec(1);

        if (target != null && caster.distanceTo(target) <= range) {
            if (!world.isClient) {
                double x = target.getX();
                double y = target.getY();
                double z = target.getZ();

                HitResult hit = world.raycast(new RaycastContext(origin, new Vec3d(x, y, z), RaycastContext.ShapeType.COLLIDER, hitLiquids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, caster));

                if (hit != null && hit.getType() == HitResult.Type.BLOCK && !((BlockHitResult) hit).getBlockPos().equals(new BlockPos((int) x, (int) y, (int) z))) {
                    return false;
                }

                Direction side = null;

                if (!target.isOnGround() && requiresFloor) {
                    Integer floor = BlockUtils.getNearestFloor(world, new BlockPos((int) x, (int) y, (int) z), 3);
                    if (floor == null) return false;
                    y = floor;
                    side = Direction.UP;
                }

                if (!spawnConstruct(world, x, y, z, side, caster, modifiers)) return false;
            }

            caster.swingHand(hand);
            this.playSound(world, caster, ticksInUse, -1, modifiers);
            return true;
        }

        return false;
    }

    @Override
    public boolean cast(World world, double x, double y, double z, Direction direction, int ticksInUse, int duration, SpellModifiers modifiers) {
        double range = getDoubleProperty(RANGE) * modifiers.get(WizardryItems.RANGE_UPGRADE);
        Vec3d origin = new Vec3d(x, y, z);
        Vec3d endpoint = origin.add(new Vec3d(direction.getUnitVector()).multiply(range));
        HitResult rayTrace = world.raycast(new RaycastContext(origin, endpoint, RaycastContext.ShapeType.COLLIDER, hitLiquids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, null));

        if (rayTrace != null && rayTrace.getType() == HitResult.Type.BLOCK && (((BlockHitResult) rayTrace).getSide() == Direction.UP || !requiresFloor)) {
            if (!world.isClient) {
                double x1 = rayTrace.getPos().x;
                double y1 = rayTrace.getPos().y;
                double z1 = rayTrace.getPos().z;

                if (!spawnConstruct(world, x1, y1, z1, ((BlockHitResult) rayTrace).getSide(), null, modifiers))
                    return false;
            }
        } else if (!requiresFloor) {
            if (!world.isClient) {
                if (!spawnConstruct(world, endpoint.x, endpoint.y, endpoint.z, null, null, modifiers)) return false;
            }
        } else {
            return false;
        }

        this.playSound(world, x - direction.getOffsetX(), y - direction.getOffsetY(), z - direction.getOffsetZ(), ticksInUse, duration, modifiers);
        return true;
    }
}
