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
import net.minecraft.world.World;

import java.util.List;

public class EntityBlizzard extends EntityScaledConstruct{
    public EntityBlizzard(EntityType<?> type, World world) {
        super(type, world);
    }

    public EntityBlizzard(World world) {
        super(WizardryEntities.BLIZZARD, world);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.changing(Spells.BLIZZARD.getFloatProperty(Spell.EFFECT_RADIUS) * 2, 3);
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }

    @Override
    public void tick() {
        if (this.age % 120 == 1) {
            this.playSound(WizardrySounds.ENTITY_BLIZZARD_AMBIENT, 1.0f, 1.0f);
        }

        super.tick();

        double radius = Spells.BLIZZARD.getDoubleProperty(Spell.EFFECT_RADIUS) * sizeMultiplier;

        if (!this.getWorld().isClient) {
            List<LivingEntity> targets = EntityUtil.getLivingEntitiesInRange(getWorld(), this.getX(), this.getY(), this.getZ(), radius);


            for (LivingEntity target : targets) {
                if (this.isValidTarget(target)) {
                    if (this.getCaster() != null) {
                        EntityUtil.attackEntityWithoutKnockback(target, target.getDamageSources().indirectMagic(this.getCaster(), target), 1 * damageMultiplier);
                    } else {
                        EntityUtil.attackEntityWithoutKnockback(target, target.getDamageSources().indirectMagic(this, target), 1 * damageMultiplier);
                    }
                }
                target.addStatusEffect(new StatusEffectInstance(WizardryEffects.FROST, 20, 0));
            }

        } else {
            for (int i = 0; i < 6; i++) {
                double speed = (random.nextBoolean() ? 1 : -1) * (0.1 + 0.05 * random.nextDouble());
                ParticleBuilder.create(WizardryParticles.SNOW).pos(this.getX(), this.getY() + random.nextDouble() * getHeight(), this.getZ()).velocity(0, 0, 0)
                        .time(100).scale(2).spin(random.nextDouble() * (radius - 0.5) + 0.5, speed).shaded(true).spawn(getWorld());
            }

            for (int i = 0; i < 3; i++) {
                double speed = (random.nextBoolean() ? 1 : -1) * (0.05 + 0.02 * random.nextDouble());
                ParticleBuilder.create(WizardryParticles.CLOUD).pos(this.getX(), this.getY() + random.nextDouble() * (getHeight() - 0.5), this.getZ()).color(0xffffff)
                        .shaded(true).spin(random.nextDouble() * (radius - 1) + 0.5, speed).scale(2).spawn(getWorld());
            }
        }
    }
}
