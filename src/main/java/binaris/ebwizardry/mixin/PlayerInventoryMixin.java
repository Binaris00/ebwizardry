package binaris.ebwizardry.mixin;

import binaris.ebwizardry.item.IConjuredItem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Unique
    private final PlayerInventory playerInventory = (PlayerInventory) (Object) this;

    @Inject(at = @At("HEAD"), method = "removeStack*", cancellable = true)
    public void EBWIZARDRY$RemoveStack(int slot, int amount, CallbackInfoReturnable<ItemStack> cir){
        if(playerInventory.getStack(slot).getItem() instanceof IConjuredItem){
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}
