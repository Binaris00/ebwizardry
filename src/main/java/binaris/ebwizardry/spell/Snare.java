package binaris.ebwizardry.spell;

import binaris.ebwizardry.registry.WizardryBlocks;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.BlockUtils;
import binaris.ebwizardry.util.ParticleBuilder;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Snare extends SpellRay{
    public Snare() {
        super("snare", UseAction.NONE, false);
        this.soundValues(1, 1.4f, 0.4f);
        this.ignoreLivingEntities(true);
    }

    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }

    @Override
    protected boolean onBlockHit(World world, BlockPos pos, Direction side, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if (side == Direction.UP && world.getBlockState(pos).isSideSolidFullSquare(world, pos, Direction.UP) && BlockUtils.canBlockBeReplaced(world, pos.up())) {
            if (!world.isClient) {
                world.setBlockState(pos.up(), WizardryBlocks.BLOCK_SNARE.getDefaultState());
            }
            return true;
        }

        return false;
    }

    @Override
    protected boolean onMiss(World world, @Nullable LivingEntity caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }

    @Override
    protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz) {
        float brightness = world.random.nextFloat() * 0.25f;
        ParticleBuilder.create(WizardryParticles.SPARKLE).pos(x, y, z).time(20 + world.random.nextInt(8))
                .color(brightness, brightness + 0.1f, 0).spawn(world);
        ParticleBuilder.create(WizardryParticles.LEAF).pos(x, y, z).velocity(0, -0.01, 0).time(40 + world.random.nextInt(10)).spawn(world);
    }
}
