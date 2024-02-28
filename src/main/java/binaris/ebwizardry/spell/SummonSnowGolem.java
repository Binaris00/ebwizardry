package binaris.ebwizardry.spell;

import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.BlockUtils;
import binaris.ebwizardry.util.ParticleBuilder;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SummonSnowGolem extends Spell{
    public SummonSnowGolem() {
        super("summon_snow_golem", UseAction.NONE, false);
        this.soundValues(1, 1, 0.4f);
    }

    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers) {
        BlockPos pos = BlockUtils.findNearbyFloorSpace(caster, 2, 4);
        if (pos == null) return false;

        if (!world.isClient) {
            SnowGolemEntity snowman = new SnowGolemEntity(EntityType.SNOW_GOLEM, world);
            snowman.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            world.spawnEntity(snowman);

        } else {
            for (int i = 0; i < 10; i++) {
                double x = pos.getX() + world.random.nextDouble() * 2 - 1;
                double y = pos.getY() + 0.5 + world.random.nextDouble();
                double z = pos.getZ() + world.random.nextDouble() * 2 - 1;
                ParticleBuilder.create(WizardryParticles.SPARKLE).pos(x, y, z).velocity(0, 0.1, 0).color(0.6f, 0.6f, 1).spawn(world);
            }
        }

        playSound(world, caster, ticksInUse, -1, modifiers);
        return true;
    }
}
