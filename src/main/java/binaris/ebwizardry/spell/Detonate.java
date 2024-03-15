package binaris.ebwizardry.spell;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.util.EntityUtil;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Detonate extends SpellRay{
    public static final String MAX_DAMAGE = "max_damage";

    public Detonate() {
        super("detonate", UseAction.NONE, false);
        this.soundValues(4, 0.7f, 0.14f);
        this.ignoreLivingEntities(true);
    }

    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }

    @Override
    protected boolean onBlockHit(World world, BlockPos pos, Direction side, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if (!world.isClient) {
            List<LivingEntity> targets = EntityUtil.getLivingEntitiesInRange(world, pos.getX(), pos.getY(), pos.getZ(),getDoubleProperty(BLAST_RADIUS) * modifiers.get(WizardryItems.BLAST_UPGRADE));
            Wizardry.LOGGER.info("Detonate: " + targets.size() + " targets found");
            for (LivingEntity target : targets) {

                target.damage(target.getDamageSources().indirectMagic(caster, target), Math.max(getFloatProperty(MAX_DAMAGE) - (float) target.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) * 4, 0) * modifiers.get(SpellModifiers.POTENCY));
            }
        } else {
            world.addParticle(ParticleTypes.EXPLOSION_EMITTER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
        }

        return true;
    }

    @Override
    protected boolean onMiss(World world, @Nullable LivingEntity caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }

    @Override
    protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz) {
        world.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
    }
}
