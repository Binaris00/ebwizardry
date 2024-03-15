package binaris.ebwizardry.mixin;

import binaris.ebwizardry.effects.ICustomPotionParticles;
import binaris.ebwizardry.registry.WizardryEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Inject(at = @At("RETURN"), method = "modifyAppliedDamage", cancellable = true)
    public void EBWIZARDRY$LivingEntityModifyApplyDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir){
        if(livingEntity.hasStatusEffect(WizardryEffects.WARD)){
            // reduces if indirect magic damage
            if(source == livingEntity.getDamageSources().magic() || source == livingEntity.getDamageSources().indirectMagic(source.getSource(), source.getAttacker())){
                amount *= Math.max(0, 1 - 0.2f * (1 + livingEntity.getStatusEffect(WizardryEffects.WARD).getAmplifier()));
                cir.setReturnValue(amount);
            }

        }
    }
}
