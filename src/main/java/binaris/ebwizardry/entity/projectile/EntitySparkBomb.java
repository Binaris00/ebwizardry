package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.*;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
/**
 * EntitySparkBomb is a class that represents the Spark Bomb entity.
 * */
public class EntitySparkBomb extends EntityBomb{
    public EntitySparkBomb(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public int getFireTicks() {
        return -1;
    }

    public EntitySparkBomb(LivingEntity livingEntity, World world) {
        super(WizardryEntities.entitySparkBomb, livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {
        return WizardryItems.SPARK_BOMB;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.playSound(WizardrySounds.ENTITY_SPARK_BOMB_HIT_BLOCK, 0.5f, 0.5f);
        super.onBlockHit(blockHitResult);
        LivingEntity entity = getWorld().getClosestEntity(LivingEntity.class, TargetPredicate.DEFAULT, this.getControllingPassenger(), this.getX(), this.getY(), this.getZ(), this.getBoundingBox().expand(Spells.SPARK_BOMB.getIntProperty(Spell.EFFECT_RADIUS)));

        if(entity != null){entity.damage(entity.getDamageSources().indirectMagic(this, this.getOwner()), Spells.SPARK_BOMB.getIntProperty(Spell.SPLASH_DAMAGE));}

        ParticleBuilder.spawnShockParticles(this.getWorld(), this.getX(), this.getY() + this.getHeight()/2, this.getZ());



    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        // Direct hit damage
        float damage = Spells.SPARK_BOMB.getIntProperty(Spell.DIRECT_DAMAGE);
        entityHitResult.getEntity().damage(entityHitResult.getEntity().getDamageSources().indirectMagic(this, this.getOwner()), damage);
        this.playSound(WizardrySounds.ENTITY_SPARK_BOMB_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        ParticleBuilder.spawnShockParticles(this.getWorld(), this.getX(), this.getY() + this.getHeight()/2, this.getZ());

        super.onEntityHit(entityHitResult);



    }
}
