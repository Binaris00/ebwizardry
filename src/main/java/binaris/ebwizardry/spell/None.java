package binaris.ebwizardry.spell;

import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

/**
 * This class represents a blank spell used to fill empty slots on wands. It is unobtainable in-game, except via
 * commands, and does nothing when the player attempts to cast it. Its instance can be referenced directly using
 * {@link binaris.ebwizardry.registry.Spells#NONE Spells.NONE}
 */
public class None extends Spell{

    public None(){
        super("none", UseAction.NONE, false);
    }

    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers) {
        return false;
    }
}
