package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.effects.FireSkin;
import binaris.ebwizardry.effects.Frost;
import binaris.ebwizardry.effects.MagicStatusEffect;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public final class WizardryEffects {
    public static StatusEffect FROST;
    public static StatusEffect TRANSIENCE;
    public static StatusEffect FIRE_SKIN;
    public static StatusEffect ICE_SHROUD;
    public static StatusEffect STATIC_AURA;
    public static StatusEffect MIND_CONTROL;
    public static StatusEffect WARD;
    public static void registerEffects() {
        FROST = registerEffect(new Frost(), "frost");
        FIRE_SKIN = registerEffect(new FireSkin(), "fire_skin");
        TRANSIENCE = registerEffect(new MagicStatusEffect(StatusEffectCategory.BENEFICIAL, 0) {
            @Override
            public void spawnCustomParticle(World world, double x, double y, double z) {
                ParticleBuilder.create(WizardryParticles.DUST).pos(x, y, z).color(0.8f, 0.8f, 1.0f).shaded(true).spawn(world);
            }
        }, "transience");

        ICE_SHROUD = registerEffect(new MagicStatusEffect(StatusEffectCategory.BENEFICIAL, 0) {
            @Override
            public void spawnCustomParticle(World world, double x, double y, double z) {
                float brightness = 0.5f + (world.random.nextFloat() / 2);
                ParticleBuilder.create(WizardryParticles.SPARKLE).pos(x, y, z).color(brightness, brightness + 0.1f, 1.0f).gravity(true).spawn(world);
                ParticleBuilder.create(WizardryParticles.SNOW).pos(x, y, z).spawn(world);
            }
        }, "ice_shroud");

        STATIC_AURA = registerEffect(new MagicStatusEffect(StatusEffectCategory.BENEFICIAL, 0) {
            @Override
            public void spawnCustomParticle(World world, double x, double y, double z) {
                ParticleBuilder.create(WizardryParticles.SPARK).pos(x, y, z).spawn(world);
            }
        }, "static_aura");

        WARD = registerEffect(new MagicStatusEffect(StatusEffectCategory.BENEFICIAL, 0xc991d0) {
            @Override
            public void spawnCustomParticle(World world, double x, double y, double z) {
            }
        }, "ward");
    }

    private static StatusEffect registerEffect(StatusEffect effect, String name) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(Wizardry.MODID, name), effect);
    }
}
