package binaris.ebwizardry.spell;

import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.BlockUtils;
import binaris.ebwizardry.util.ParticleBuilder;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Banish extends SpellRay{
    public static final String MINIMUM_TELEPORT_DISTANCE = "minimum_teleport_distance";
    public static final String MAXIMUM_TELEPORT_DISTANCE = "maximum_teleport_distance";

    public Banish() {
        super("banish", UseAction.NONE, false);
    }

    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if (target instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) target;

            double minRadius = getDoubleProperty(MINIMUM_TELEPORT_DISTANCE);
            double maxRadius = getDoubleProperty(MAXIMUM_TELEPORT_DISTANCE);
            double radius = (minRadius + world.random.nextDouble() * maxRadius - minRadius) * modifiers.get(WizardryItems.BLAST_UPGRADE);

            teleport(entity, world, radius);
        }

        return true;
    }

    @Override
    protected boolean onBlockHit(World world, BlockPos pos, Direction side, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }

    @Override
    protected boolean onMiss(World world, @Nullable LivingEntity caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
        return true;
    }


    @Override
    protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz) {
        world.addParticle(ParticleTypes.PORTAL, x, y - 0.5, z, 0, 0, 0);
        ParticleBuilder.create(WizardryParticles.DARK_MAGIC).pos(x, y, z).color(0.2f, 0, 0.2f).spawn(world);
    }

    public boolean teleport(LivingEntity entity, World world, double radius) {
        float angle = world.random.nextFloat() * (float) Math.PI * 2;

        int x = MathHelper.floor(entity.getX() + MathHelper.sin(angle) * radius);
        int z = MathHelper.floor(entity.getZ() - MathHelper.cos(angle) * radius);
        Integer y = BlockUtils.getNearestFloor(world, new BlockPos(x, (int) entity.getY(), z), (int) radius);

        if (world.isClient) {
            for (int i = 0; i < 10; i++) {
                double dx1 = entity.getX();
                double dy1 = entity.getY() + entity.getHeight() * world.random.nextFloat();
                double dz1 = entity.getZ();
                world.addParticle(ParticleTypes.PORTAL, dx1, dy1, dz1, world.random.nextDouble() - 0.5, world.random.nextDouble() - 0.5, world.random.nextDouble() - 0.5);
            }
            // TODO: (Shader) Blink Effect
            //if (entity instanceof PlayerEntity player) Wizardry.proxy.playBlinkEffect((Player) entity);
        }

        if (y != null) {
            if (!world.getBlockState(new BlockPos(x, y, z)).blocksMovement()) {
                y--;
            }

            if (world.getBlockState(new BlockPos(x, y + 1, z)).blocksMovement() || world.getBlockState(new BlockPos(x, y + 2, z)).blocksMovement()) {
                return false;
            }

            if (!world.isClient) {
                entity.refreshPositionAfterTeleport(x + 0.5, y + 1, z + 0.5);
            }

            this.playSound(world, entity, 0, -1, new SpellModifiers());
        }

        return true;
    }
}
