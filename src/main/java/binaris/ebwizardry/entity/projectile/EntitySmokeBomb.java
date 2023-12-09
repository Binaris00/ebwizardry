package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Spell;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class EntitySmokeBomb extends EntityBomb {
    public EntitySmokeBomb(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntitySmokeBomb(LivingEntity livingEntity, World world) {
        super(WizardryEntities.ENTITY_SMOKE_BOMB, livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {
        return WizardryItems.SMOKE_BOMB;
    }

    @Override
    public int getFireTicks() {
        return -1;
    }

    @Override
    protected void onCollision(HitResult hitResult) {

        this.playSound(WizardrySounds.ENTITY_SMOKE_BOMB_SMASH, 1.5F, random.nextFloat() * 0.4F + 0.6F);
        this.playSound(WizardrySounds.ENTITY_SMOKE_BOMB_SMOKE, 1.2F, 1.0f);

        int duration = Spells.SMOKE_BOMB.getIntProperty(Spell.EFFECT_DURATION);

        LivingEntity entity = getWorld().getClosestEntity(LivingEntity.class, TargetPredicate.DEFAULT, this.getControllingPassenger(), this.getX(), this.getY(), this.getZ(), this.getBoundingBox().expand(Spells.SPARK_BOMB.getIntProperty(Spell.EFFECT_RADIUS)));
        if(entity != null) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, duration, 0));
        }

        super.onCollision(hitResult);
    }
}
