package binaris.ebwizardry.entity.construct;

import binaris.ebwizardry.registry.*;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.EntityUtil;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityFrostSigil extends EntityScaledConstruct{
    public EntityFrostSigil(EntityType<?> type, World world) {
        super(type, world);
    }

    public EntityFrostSigil(World world) {
        super(WizardryEntities.FROST_SIGIL, world);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.changing(Spells.FROST_SIGIL.getFloatProperty(Spell.EFFECT_RADIUS) * 2, 0.2f);
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            List<LivingEntity> targets = EntityUtil.getLivingEntitiesInRange(getWorld(), getX(), getY(), getZ(), getWidth() / 2);

            for (LivingEntity target : targets) {
                if (this.isValidTarget(target)) {
                    EntityUtil.attackEntityWithoutKnockback(target, target.getDamageSources().indirectMagic(this.getCaster(), target), Spells.FROST_SIGIL.getFloatProperty(Spell.DAMAGE) * damageMultiplier);

                    target.addStatusEffect(new StatusEffectInstance(WizardryEffects.FROST, Spells.FROST_SIGIL.getIntProperty(Spell.EFFECT_DURATION), Spells.FROST_SIGIL.getIntProperty(Spell.EFFECT_STRENGTH)));
                    this.playSound(WizardrySounds.ENTITY_FROST_SIGIL_TRIGGER, 1.0f, 1.0f);

                    this.discard();
                }
            }
        } else if (this.random.nextInt(15) == 0) {
            double radius = (0.5 + random.nextDouble() * 0.3) * getWidth() / 2;
            float angle = random.nextFloat() * (float) Math.PI * 2;
            ParticleBuilder.create(WizardryParticles.SNOW).pos(this.getX() + radius * MathHelper.cos(angle), this.getY() + 0.1, this.getZ() + radius * MathHelper.sin(angle)).velocity(0, 0, 0).spawn(getWorld());
        }
    }
}
