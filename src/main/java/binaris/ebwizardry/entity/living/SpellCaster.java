package binaris.ebwizardry.entity.living;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.EntityUtil;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.world.Difficulty;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Deprecated
public interface SpellCaster {
    @NotNull
    List<Spell> getSpells();
    @NotNull
    default SpellModifiers getModifiers() {
        return new SpellModifiers();
    }
    @NotNull
    default Spell getContinuousSpell() {
        return Spells.NONE;
    }

    default void setContinuousSpell(Spell spell) {

    }

    default int getSpellCounter() {
        return 0;
    }

    default void setSpellCounter(int count) {

    }

    default int getAimingError(Difficulty difficulty) {
        return EntityUtil.getDefaultAimingError(difficulty);
    }
}
