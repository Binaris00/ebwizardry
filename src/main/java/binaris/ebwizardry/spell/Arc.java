package binaris.ebwizardry.spell;

import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.EntityUtil;
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
@Deprecated
public class Arc extends SpellRay{
    public Arc() {
        super("arc", UseAction.NONE, false);
        this.aimAssist(0.6f);
        this.soundValues(1, 1.7f, 0.2f);

    }

    @Override
    protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable LivingEntity caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
        if(EntityUtil.isLiving(target)){
            if(world.isClient){
                ParticleBuilder.create(WizardryParticles.LIGHTNING).entity(caster).pos(caster != null ? origin.subtract(caster.getPos()) : origin).target(target).spawn(world);
                ParticleBuilder.spawnShockParticles(world, target.getX(), target.getY() + target.getHeight() / 2, target.getZ());
            }

            target.damage(target.getDamageSources().indirectMagic(caster, target), getFloatProperty(DAMAGE) * modifiers.get(SpellModifiers.POTENCY));
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
