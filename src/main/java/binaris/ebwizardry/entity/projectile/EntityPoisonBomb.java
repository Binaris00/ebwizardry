package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.*;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.EntityUtil;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.List;

public class EntityPoisonBomb extends EntityBomb {
    public EntityPoisonBomb(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntityPoisonBomb(LivingEntity livingEntity, World world) {
        super(WizardryEntities.ENTITY_POISON_BOMB, livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {
        return WizardryItems.POISON_BOMB;
    }


    @Override
    protected void onCollision(HitResult hitResult) {
        // Direct hit damage
        if(hitResult instanceof EntityHitResult entityHitResult){
            Entity entity = entityHitResult.getEntity();

            float damage = Spells.FIRE_BOMB.getIntProperty(Spell.DIRECT_DAMAGE);
            entity.damage(entity.getDamageSources().indirectMagic(this, this.getOwner()), damage);
        }

        // Splash damage
        if(hitResult instanceof BlockHitResult){
            double range = Spells.POISON_BOMB.getIntProperty(Spell.EFFECT_RADIUS);
            List<LivingEntity> livingEntities = EntityUtil.getLivingEntitiesInRange(getWorld(), getX(), getY(), getZ(), range);
            for (LivingEntity entity : livingEntities) {
                if (entity != null && entity != this.getOwner()) {
                    entity.damage(entity.getDamageSources().indirectMagic(this, this.getOwner()), Spells.POISON_BOMB.getFloatProperty(Spell.SPLASH_DAMAGE) * damageMultiplier);
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, Spells.POISON_BOMB.getIntProperty(Spell.SPLASH_EFFECT_DURATION), Spells.POISON_BOMB.getIntProperty(Spell.SPLASH_EFFECT_STRENGTH)));
                }
            }
        }

        if(!getWorld().isClient()){
            // Sound
            this.playSound(WizardrySounds.ENTITY_POISON_BOMB_SMASH, 1.5F, random.nextFloat() * 0.4F + 0.6F);
            this.playSound(WizardrySounds.ENTITY_POISON_BOMB_POISON, 1.2F, 1.0f);

            // Particle stuff
            this.getWorld().sendEntityStatus(this, (byte) 3);
            this.discard();
        }
    }
    // This is for spawning particles, this is the only way I could get it to work.
    // I'm not sure if this is the best way to do it, but it works.
    // TODO: This is a good way to spawn particles?
    @Override
    public void handleStatus(byte status) {
        if(status == 3){
            for(int i = 0; i < 60 * blastMultiplier; i++){
                ParticleBuilder.create(WizardryParticles.SPARKLE, random, prevX, prevY, prevZ, 2 * blastMultiplier).scale(2)
                        .color(0.2f + random.nextFloat() * 0.3f, 0.6f, 0.0f).time(35).spawn(getWorld());

                ParticleBuilder.create(WizardryParticles.DARK_MAGIC, random, prevX, prevY, prevZ, 2 * blastMultiplier, false)
                        .color(0.2f + random.nextFloat() * 0.2f, 0.8f, 0.0f).spawn(getWorld());
            }
            getWorld().addParticle(ParticleTypes.EXPLOSION, prevX, prevY, prevZ, 0, 0, 0);
        }
        super.handleStatus(status);
    }
}

