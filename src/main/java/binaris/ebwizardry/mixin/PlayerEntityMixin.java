package binaris.ebwizardry.mixin;

import binaris.ebwizardry.constant.Constants;
import binaris.ebwizardry.registry.WizardryEffects;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Unique
    private final PlayerEntity player = (PlayerEntity) (Object) this;

    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    private void EBWIZARDRY$BreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        if(player.hasStatusEffect(WizardryEffects.FROST)){
            cir.setReturnValue(cir.getReturnValue() * (1 - Constants.FROST_FATIGUE_PER_LEVEL * player.getStatusEffect(WizardryEffects.FROST).getAmplifier() + 1));
        }
    }


}
