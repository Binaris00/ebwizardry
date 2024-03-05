package binaris.ebwizardry.spell;

import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class Flamecatcher extends SpellConjuration{
    public static final String SHOT_COUNT = "shot_count";
    public static final String SHOTS_REMAINING_NBT_KEY = "shotsRemaining";
    public Flamecatcher() {
     super("flamecatcher", WizardryItems.FLAMECATCHER);
    }

    @Override
    protected void addItemExtras(PlayerEntity caster, ItemStack stack, SpellModifiers modifiers) {
        stack.getOrCreateNbt().putInt(SHOTS_REMAINING_NBT_KEY, (int) (getIntProperty(SHOT_COUNT) * modifiers.get(SpellModifiers.POTENCY)));
    }

    @Override
    protected void spawnParticles(World world, LivingEntity caster, SpellModifiers modifiers) {
        // TODO: BUFF PARTICLE
        //ParticleBuilder.create(WizardryParticles.BUFF).entity(caster).clr(0xff6d00).spawn(world);

        for (int i = 0; i < 10; i++) {
            double x = caster.getX() + world.random.nextDouble() * 2 - 1;
            double y = caster.getY() + caster.getStandingEyeHeight() - 0.5 + world.random.nextDouble();
            double z = caster.getZ() + world.random.nextDouble() * 2 - 1;
            world.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
        }
    }
}
