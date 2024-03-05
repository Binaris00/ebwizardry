package binaris.ebwizardry.spell;

import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class FlamingAxe extends SpellConjuration{
    public FlamingAxe() {
        super("flaming_axe", WizardryItems.FLAMING_AXE);
    }

    @Override
    protected void spawnParticles(World world, LivingEntity caster, SpellModifiers modifiers) {
        for (int i = 0; i < 10; i++) {
            double x = caster.getX() + world.random.nextDouble() * 2 - 1;
            double y = caster.getY() + caster.getStandingEyeHeight() - 0.5 + world.random.nextDouble();
            double z = caster.getZ() + world.random.nextDouble() * 2 - 1;
            world.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
        }
    }
}
