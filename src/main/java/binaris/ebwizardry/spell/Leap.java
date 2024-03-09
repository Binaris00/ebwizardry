package binaris.ebwizardry.spell;

import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class Leap extends Spell{
    public static final String HORIZONTAL_SPEED = "horizontal_speed";
    public static final String VERTICAL_SPEED = "vertical_speed";

    public Leap() {
        super("leap", UseAction.NONE, false);
    }

    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers) {
        if(caster.isOnGround()){
            caster.setVelocity(caster.getVelocity().x, getFloatProperty(VERTICAL_SPEED) * modifiers.get(SpellModifiers.POTENCY), caster.getVelocity().z);
            double horizontalSpeed = getFloatProperty(HORIZONTAL_SPEED);
            caster.addVelocity(caster.getRotationVector().x * horizontalSpeed, 0, caster.getRotationVector().z * horizontalSpeed);

            if(world.isClient){
                for(int i = 0; i < 10; i++){
                    double x = caster.getX() + world.random.nextFloat() - 0.5F;
                    double y = caster.getY();
                    double z = caster.getZ() + world.random.nextFloat() - 0.5F;
                    world.addParticle(ParticleTypes.CLOUD, x, y, z, 0, 0, 0);
                }
            }

            this.playSound(world, caster, ticksInUse, -1, modifiers);
            caster.swingHand(hand);
            return true;
        }

        return false;
    }
}
