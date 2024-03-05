package binaris.ebwizardry.spell;

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

public class HealAlly extends SpellRay{
    public HealAlly() {
        super("heal_ally", UseAction.NONE, false);
        this.soundValues(0.7f, 1.2f, 0.4f);
    }

    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if (target instanceof LivingEntity livingTarget) {

            if (livingTarget.getHealth() < livingTarget.getMaxHealth() && livingTarget.getHealth() > 0) {
                livingTarget.heal(getFloatProperty(HEALTH) * modifiers.get(SpellModifiers.POTENCY));

                if (world.isClient) ParticleBuilder.spawnHealParticles(world, livingTarget);
                playSound(world, livingTarget, ticksInUse, -1, modifiers);
            }

            return true;
        }

        return false;
    }

    @Override
    protected boolean onBlockHit(World world, BlockPos pos, Direction side, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }

    @Override
    protected boolean onMiss(World world, @Nullable LivingEntity caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }
}
