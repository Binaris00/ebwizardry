package binaris.ebwizardry.spell;

import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WitherSkull extends Spell{
    public static final String ACCELERATION = "acceleration";

    public WitherSkull() {
        super("wither_skull", UseAction.NONE, false);
    }

    @Override
    public boolean requiresPacket() {
        return false;
    }

    @Override
    public boolean canBeCastBy(LivingEntity npc, boolean override) {
        return true;
    }

    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers) {
        Vec3d look = caster.getRotationVector();

        if (!world.isClient) {
            WitherSkullEntity witherSkull = new WitherSkullEntity(world, caster, 1, 1, 1);

            witherSkull.setPos(caster.getX() + look.x, caster.getY() + look.y + 1.3, caster.getZ() + look.z);

            double acceleration = getDoubleProperty(ACCELERATION) * modifiers.get(WizardryItems.RANGE_UPGRADE);

            witherSkull.powerX = look.x * acceleration;
            witherSkull.powerY = look.y * acceleration;
            witherSkull.powerZ = look.z * acceleration;

            witherSkull.setOwner(caster);

            world.spawnEntity(witherSkull);

            this.playSound(world, caster, ticksInUse, -1, modifiers);
        }
        caster.swingHand(hand);
        return true;
    }

    @Override
    public boolean cast(World world, LivingEntity caster, Hand hand, int ticksInUse, LivingEntity target, SpellModifiers modifiers) {
        if (target != null) {
            if (!world.isClient) {
                WitherSkullEntity witherSkull = new WitherSkullEntity(world, caster, 1, 1, 1);

                double dx = target.getX() - caster.getX();
                double dy = target.getY() + (double) (target.getHeight() / 2.0F) - (caster.getY() + (double) (caster.getHeight() / 2.0F));
                double dz = target.getZ() - caster.getZ();

                witherSkull.powerX = dx / caster.distanceTo(target) * 0.1;
                witherSkull.powerY = dy / caster.distanceTo(target) * 0.1;
                witherSkull.powerZ = dz / caster.distanceTo(target) * 0.1;

                witherSkull.setOwner(caster);
                witherSkull.setPos(caster.getX(), caster.getY() + caster.getStandingEyeHeight(), caster.getZ());

                world.spawnEntity(witherSkull);
                this.playSound(world, caster, ticksInUse, -1, modifiers);
            }

            caster.swingHand(hand);
            return true;
        }

        return false;
    }
}
