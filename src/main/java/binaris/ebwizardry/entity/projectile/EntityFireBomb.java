package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Spell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class EntityFireBomb extends EntityBomb{
    public EntityFireBomb(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntityFireBomb(LivingEntity livingEntity, World world) {
        super(WizardryEntities.ENTITY_FIRE_BOMB, livingEntity, world);
    }

    @Override
    public int getFireTicks() {
        return -1;
    }

    @Override
    protected Item getDefaultItem() {
        return WizardryItems.FIRE_BOMB;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        // Direct hit damage
        if(hitResult instanceof EntityHitResult entityHitResult){
            Entity entity = entityHitResult.getEntity();

            float damage = Spells.FIRE_BOMB.getIntProperty(Spell.DIRECT_DAMAGE);
            entity.damage(entity.getDamageSources().indirectMagic(this, this.getOwner()), damage);
        }

        /* Yop, insert particles here~
            * 		if(world.isRemote){

			ParticleBuilder.create(Type.FLASH).pos(this.getPositionVector()).scale(5 * blastMultiplier).clr(1, 0.6f, 0)
			.spawn(world);

			for(int i = 0; i < 60 * blastMultiplier; i++){

				ParticleBuilder.create(Type.MAGIC_FIRE, rand, posX, posY, posZ, 2*blastMultiplier, false)
				.time(10 + rand.nextInt(4)).scale(2 + rand.nextFloat()).spawn(world);

				ParticleBuilder.create(Type.DARK_MAGIC, rand, posX, posY, posZ, 2*blastMultiplier, false)
				.clr(1.0f, 0.2f + rand.nextFloat() * 0.4f, 0.0f).spawn(world);
			}

			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 0, 0, 0);
		}
         * */

        this.playSound(WizardrySounds.ENTITY_FIREBOMB_SMASH, 1.5F, random.nextFloat() * 0.4F + 0.6F);
        this.playSound(WizardrySounds.ENTITY_FIREBOMB_FIRE, 1, 1);

        if(hitResult instanceof BlockHitResult){
            LivingEntity entity = getWorld().getClosestEntity(LivingEntity.class, TargetPredicate.DEFAULT, this.getControllingPassenger(), this.getX(), this.getY(), this.getZ(), this.getBoundingBox().expand(Spells.SPARK_BOMB.getIntProperty(Spell.EFFECT_RADIUS)));

            if(entity != null){
                entity.damage(entity.getDamageSources().indirectMagic(this, this.getOwner()), Spells.FIRE_BOMB.getFloatProperty(Spell.SPLASH_DAMAGE) * damageMultiplier);
                entity.setOnFireFor(Spells.FIRE_BOMB.getIntProperty(Spell.BURN_DURATION));
            }
        }

        super.onCollision(hitResult);
    }
}
