package binaris.ebwizardry.mixin;

import binaris.ebwizardry.effects.ICustomPotionParticles;
import binaris.ebwizardry.registry.WizardryEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Unique
    private final LivingEntity livingEntity = (LivingEntity) (Object) this;

    @Inject(at = @At("HEAD"), method = "jump")
    public void EBWIZARDRY$LivingEntityJump(CallbackInfo ci){
        if(livingEntity.hasStatusEffect(WizardryEffects.FROST)){
            if(livingEntity.getStatusEffect(WizardryEffects.FROST).getAmplifier() == 0){
                livingEntity.setVelocity(livingEntity.getVelocity().x, 0.5, livingEntity.getVelocity().z);
            } else {
                livingEntity.setVelocity(livingEntity.getVelocity().x, 0, livingEntity.getVelocity().y);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void EBWIZARDRY$LivingEntityTick(CallbackInfo ci){
        if (livingEntity.getWorld().isClient) {
            for (StatusEffectInstance effect : livingEntity.getStatusEffects()) {
                if (effect instanceof ICustomPotionParticles && effect.shouldShowParticles()) {
                    double x = livingEntity.getX()
                            + (livingEntity.getWorld().random.nextDouble() - 0.5) * livingEntity.getWidth();
                    double y = livingEntity.getY()
                            + livingEntity.getWorld().random.nextDouble() * livingEntity.getHeight();
                    double z = livingEntity.getZ()
                            + (livingEntity.getWorld().random.nextDouble() - 0.5) * livingEntity.getWidth();

                    ((ICustomPotionParticles) effect).spawnCustomParticle(livingEntity.getWorld(), x, y, z);
                }
            }
        }
    }
}
