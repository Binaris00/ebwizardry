package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.*;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.EntityUtil;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.List;

public class EntitySmokeBomb extends EntityBomb {
    public EntitySmokeBomb(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntitySmokeBomb(LivingEntity livingEntity, World world) {
        super(WizardryEntities.ENTITY_SMOKE_BOMB, livingEntity, world);
    }

    public EntitySmokeBomb(World world) {
        super(WizardryEntities.ENTITY_SMOKE_BOMB, world);
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
        int duration = Spells.SMOKE_BOMB.getIntProperty(Spell.EFFECT_DURATION);

        // Effect
        List<LivingEntity> livingEntities = EntityUtil.getLivingEntitiesInRange(getWorld(), getX(), getY(), getZ(), Spells.FIRE_BOMB.getFloatProperty(Spell.RANGE));

        for(LivingEntity entity: livingEntities){
            if(entity != null && entity != this.getOwner()){
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, duration, 0));
            }
        }
        if(hitResult instanceof BlockHitResult){
            if(!getWorld().isClient()){
                // Sound
                this.playSound(WizardrySounds.ENTITY_SMOKE_BOMB_SMASH, 1.5F, random.nextFloat() * 0.4F + 0.6F);
                this.playSound(WizardrySounds.ENTITY_SMOKE_BOMB_SMOKE, 1.2F, 1.0f);

                // Particle stuff
                this.getWorld().sendEntityStatus(this, (byte) 3);
                this.discard();
            }
        }


        super.onCollision(hitResult);
    }

    @Override
    public void handleStatus(byte status) {
        if(status == 3){
            getWorld().addParticle(ParticleTypes.EXPLOSION, this.prevX, this.prevY, this.prevZ, 0, 0, 0);

            for(int i = 0; i < 60 * blastMultiplier; i++){
                float brightness = random.nextFloat() * 0.1f + 0.1f;
                ParticleBuilder.create(WizardryParticles.CLOUD, random, prevX, prevY, prevZ, 2*blastMultiplier, false)
                        .color(brightness, brightness, brightness).time(80 + this.random.nextInt(12)).shaded(true).scale(5).spawn(getWorld());

                brightness = random.nextFloat() * 0.3f;
                ParticleBuilder.create(WizardryParticles.DARK_MAGIC, random, prevX, prevY, prevZ, 2*blastMultiplier, false)
                        .color(brightness, brightness, brightness).spawn(getWorld());
            }
        }

        super.handleStatus(status);
    }
}
