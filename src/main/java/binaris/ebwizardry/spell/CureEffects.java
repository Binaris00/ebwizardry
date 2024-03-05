package binaris.ebwizardry.spell;


import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.LivingEntity;

public class CureEffects extends SpellBuff{
    public CureEffects() {
        super("cure_effects", 0.8f, 0.8f, 1);
        this.soundValues(0.7f, 1.2f, 0.4f);
    }

    @Override
    protected boolean applyEffects(LivingEntity caster, SpellModifiers modifiers) {
        if (!caster.getStatusEffects().isEmpty()) {
                caster.clearStatusEffects();
                return true;
        }

        return false;
    }
}
