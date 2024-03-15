package binaris.ebwizardry.spell;

import binaris.ebwizardry.screen.ArtificialCraftingScreenHandler;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class PocketWorkbench extends Spell{
    public PocketWorkbench() {
        super("pocket_workbench", UseAction.NONE, false);
    }

    @Override
    public boolean requiresPacket() {
        return false;
    }

    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers) {
        if(caster instanceof ServerPlayerEntity serverPlayerEntity) {
            serverPlayerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new ArtificialCraftingScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, player.getBlockPos())), Text.translatable("screen.ebwizardry.artificial_crafting")));

            serverPlayerEntity.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        }

        this.playSound(world, caster, ticksInUse, -1, modifiers);
        return true;
    }
}
