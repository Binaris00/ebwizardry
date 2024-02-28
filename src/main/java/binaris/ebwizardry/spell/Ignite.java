package binaris.ebwizardry.spell;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.util.BlockUtils;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Ignite extends SpellRay{
    public Ignite() {
        super("ignite", UseAction.NONE, false);
    }

    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        // Fire can damage armour stands, so this includes them
        if(target instanceof LivingEntity) {
            // TODO: MODIFIERS
            // target.setFire((int)(getProperty(BURN_DURATION).floatValue() * modifiers.get(WizardryItems.duration_upgrade)));
            target.setOnFireFor(Spells.IGNITE.getIntProperty(Spell.BURN_DURATION));
            return true;
        }

        return false;
    }

    @Override
    protected boolean onBlockHit(World world, BlockPos pos, Direction side, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        pos = pos.offset(side);
        if(world.isAir(pos)){
            if(!world.isClient && BlockUtils.canPlaceBlock(caster, world, pos)){
                world.setBlockState(pos, Blocks.FIRE.getDefaultState());
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
        world.addParticle(ParticleTypes.FLAME, x, y, z, 0,0,0);
    }
}
