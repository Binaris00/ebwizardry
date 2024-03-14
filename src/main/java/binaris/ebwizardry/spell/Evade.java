package binaris.ebwizardry.spell;

import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Evade extends Spell{
    public static final String EVADE_VELOCITY = "evade_velocity";

    private static final float UPWARD_VELOCITY = 0.25f;
    public Evade() {
        super("evade", UseAction.NONE, false);
    }

    @Override
    public boolean requiresPacket() {
        return false;
    }

    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers) {
        if (!caster.isOnGround()) return false;

        Vec3d look = caster.getRotationVector();

        look = look.subtract(0, look.y, 0).normalize();

        Vec3d evadeDirection;
        if (caster.sidewaysSpeed == 0) {
            evadeDirection = look.rotateY(world.random.nextBoolean() ? (float) Math.PI / 2f : (float) -Math.PI / 2f);
        } else {
            evadeDirection = look.rotateY(Math.signum(caster.sidewaysSpeed) * (float) Math.PI / 2f);
        }

        evadeDirection = evadeDirection.multiply(getFloatProperty(EVADE_VELOCITY) * modifiers.get(SpellModifiers.POTENCY));
        caster.addVelocity(evadeDirection.x, UPWARD_VELOCITY, evadeDirection.z);

        return true;
    }
}
