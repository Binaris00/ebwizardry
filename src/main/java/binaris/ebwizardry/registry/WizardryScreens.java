package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.screen.ArtificialCraftingScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public final class WizardryScreens {

    public static ScreenHandlerType<ArtificialCraftingScreenHandler> ARTIFICIAL_CRAFTING_SCREEN_HANDLER;

    public static void registering(){
        ARTIFICIAL_CRAFTING_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, new Identifier(Wizardry.MODID, "convertor"),
                new ExtendedScreenHandlerType<>((int syncId, PlayerInventory playerInventory, PacketByteBuf inventory) -> new ArtificialCraftingScreenHandler(syncId, playerInventory)));
    }
}
