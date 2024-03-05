package binaris.ebwizardry.spell;

import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.ParticleBuilder;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class FrostAxe extends SpellConjuration{
    public FrostAxe() {
        super("frost_axe", WizardryItems.FROST_AXE);
    }

    @Override
    protected void spawnParticles(World world, LivingEntity caster, SpellModifiers modifiers) {
        for (int i = 0; i < 10; i++) {
            double x = caster.getX() + world.random.nextDouble() * 2 - 1;
            double y = caster.getY() + caster.getStandingEyeHeight() - 0.5 + world.random.nextDouble();
            double z = caster.getZ() + world.random.nextDouble() * 2 - 1;
            ParticleBuilder.create(WizardryParticles.SNOW).pos(x, y, z).spawn(world);
        }
    }
}
