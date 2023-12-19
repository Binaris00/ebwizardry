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
    public int getFireTicks() {
        return -1;
    }


    @Override
    protected void onCollision(HitResult hitResult) {
        if(hitResult instanceof EntityHitResult entityHitResult){
            Entity entity = entityHitResult.getEntity();

            float damage = Spells.POISON_BOMB.getFloatProperty(Spell.DIRECT_DAMAGE) * damageMultiplier;
            entity.damage(entity.getDamageSources().indirectMagic(this, this.getOwner()), damage);

            if(entity instanceof LivingEntity livingEntity){
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, Spells.POISON_BOMB.getIntProperty(Spell.DIRECT_EFFECT_DURATION), Spells.POISON_BOMB.getIntProperty(Spell.DIRECT_EFFECT_STRENGTH)));
            }
        }

        /*
        * Omg particles :O
		if(world.isRemote){

			ParticleBuilder.create(Type.FLASH).pos(this.getPositionVector()).scale(5 * blastMultiplier)
			.clr(0.2f + rand.nextFloat() * 0.3f, 0.6f, 0.0f).spawn(world);

			for(int i = 0; i < 60 * blastMultiplier; i++){

				ParticleBuilder.create(Type.SPARKLE, rand, posX, posY, posZ, 2*blastMultiplier, false).time(35)
				.scale(2).clr(0.2f + rand.nextFloat() * 0.3f, 0.6f, 0.0f).spawn(world);

				ParticleBuilder.create(Type.DARK_MAGIC, rand, posX, posY, posZ, 2*blastMultiplier, false)
				.clr(0.2f + rand.nextFloat() * 0.2f, 0.8f, 0.0f).spawn(world);
			}
			// Spawning this after the other particles fixes the rendering colour bug. It's a bit of a cheat, but it
			// works pretty well.
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 0, 0, 0);
		}
        * */

        // Particles
        for(int i = 0; i < 60 * blastMultiplier; i++){
            ParticleBuilder.create(WizardryParticles.SPARKLE, random, getX(), getY(), getZ(), 2*blastMultiplier).spawn(getWorld());
        }


        if(!getWorld().isClient){
            this.playSound(WizardrySounds.ENTITY_POISON_BOMB_SMASH, 1.5F, random.nextFloat() * 0.4F + 0.6F);
            this.playSound(WizardrySounds.ENTITY_POISON_BOMB_POISON, 1.2F, 1.0f);
        }

        if (hitResult instanceof BlockHitResult) {
            double range = Spells.POISON_BOMB.getIntProperty(Spell.EFFECT_RADIUS);

            List<LivingEntity> livingEntities = EntityUtil.getLivingEntitiesInRange(getWorld(), getX(), getY(), getZ(), range);
            for(LivingEntity entity: livingEntities){
                if(entity != null && entity != this.getOwner()){
                    entity.damage(entity.getDamageSources().indirectMagic(this, this.getOwner()), Spells.POISON_BOMB.getFloatProperty(Spell.SPLASH_DAMAGE) * damageMultiplier);
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, Spells.POISON_BOMB.getIntProperty(Spell.SPLASH_EFFECT_DURATION), Spells.POISON_BOMB.getIntProperty(Spell.SPLASH_EFFECT_STRENGTH)));
                }
            }
        }


        super.onCollision(hitResult);
    }
}

