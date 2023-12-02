package binaris.ebwizardry.spell;

import net.minecraft.util.UseAction;
/**
 * This class represents a blank spell used to fill empty slots on wands. It is unobtainable in-game, except via
 * commands, and does nothing when the player attempts to cast it. Its instance can be referenced directly using
 * {@link binaris.ebwizardry.registry.Spells#NONE Spells.NONE}
 */
public class None extends Spell{

    public None(){
        super("none", UseAction.NONE, false);
    }
}
