package binaris.ebwizardry.spell;

import binaris.ebwizardry.entity.construct.EntityIceSpike;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.util.BlockUtils;
import binaris.ebwizardry.util.GeometryUtils;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class IceSpikes extends SpellConstructRanged<EntityIceSpike>{
    public static final String ICE_SPIKE_COUNT = "ice_spike_count";

    public IceSpikes() {
        super("ice_spikes", EntityIceSpike::new, true);
        this.ignoreUncollidables(true);
    }

    @Override
    protected boolean spawnConstruct(World world, double x, double y, double z, @Nullable Direction side, @Nullable LivingEntity caster, SpellModifiers modifiers) {
        if (side == null) return false;
        BlockPos blockHit = new BlockPos((int) x, (int) y, (int) z);

        if (side.getDirection() == Direction.AxisDirection.NEGATIVE) blockHit = blockHit.offset(side);

        if (world.getBlockState(blockHit).isFullCube(world, blockHit)) return false;

        Vec3d origin = new Vec3d(x, y, z);

        Vec3d pos = origin.add(new Vec3d(side.getOpposite().getUnitVector()));

        super.spawnConstruct(world, pos.x, pos.y, pos.z, side, caster, modifiers);

        int quantity = (int) (getFloatProperty(ICE_SPIKE_COUNT) * modifiers.get(WizardryItems.BLAST_UPGRADE)) - 1;

        float maxRadius = getFloatProperty(EFFECT_RADIUS) * modifiers.get(WizardryItems.BLAST_UPGRADE);

        for (int i = 0; i < quantity; i++) {
            double radius = 0.5 + world.random.nextDouble() * (maxRadius - 0.5);

            Vec3d offset = Vec3d.fromPolar(world.random.nextFloat() * 180 - 90, world.random.nextBoolean() ? 0 : 180)
                    .multiply(radius).rotateY(side.asRotation() * (float) Math.PI / 180).rotateX(GeometryUtils.getPitch(side) * (float) Math.PI / 180);

            if (side.getAxis().isHorizontal()) offset = offset.rotateY((float) Math.PI / 2);

            Integer surface = BlockUtils.getNearestSurface(world, new BlockPos(BlockUtils.vec3dToBlockPos(origin.add(offset))), side, (int) maxRadius, true, BlockUtils.SurfaceCriteria.basedOn(this::isCollisionShapeFullBlock));

            if (surface != null) {
                Vec3d vec = GeometryUtils.replaceComponent(origin.add(offset), side.getAxis(), surface).subtract(new Vec3d(side.getUnitVector()));
                super.spawnConstruct(world, vec.x, vec.y, vec.z, side, caster, modifiers);
            }
        }

        return true;
    }

    public boolean isCollisionShapeFullBlock(BlockView view, BlockPos pos) {
        return view.getBlockState(pos).isFullCube(view, pos);
    }

    @Override
    protected void addConstructExtras(EntityIceSpike construct, Direction side, @Nullable LivingEntity caster, SpellModifiers modifiers) {
        construct.lifetime = 30 + construct.getWorld().random.nextInt(15);
        construct.setFacing(side);
    }
}
