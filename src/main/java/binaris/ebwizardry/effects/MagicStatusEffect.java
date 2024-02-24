package binaris.ebwizardry.effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

/**
 *<i></>Originally called PotionMagicEffect, now I present to you MagicStatusEffect</i>
 * Class used to create all the status effects for the mod.
 * **/
@Deprecated
public abstract class MagicStatusEffect extends StatusEffect implements ICustomPotionParticles{
    public MagicStatusEffect(StatusEffectCategory category, int color) {super(category, color);}
}
