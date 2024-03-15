package binaris.ebwizardry.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
/**
 * This class is a CraftingScreenHandler that allows the player to craft items without the need of a crafting table.
 * It is used by the PocketWorkbench spell to open a crafting screen.
 * {@link binaris.ebwizardry.spell.PocketWorkbench PocketWorkbench}
 *
 * @author Binaris
 */
public class ArtificialCraftingScreenHandler extends CraftingScreenHandler {
    public ArtificialCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(syncId, playerInventory);
    }

    public ArtificialCraftingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(syncId, playerInventory, context);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
