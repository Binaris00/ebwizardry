package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class EntitySpark extends EntityMagicProjectile{
    public EntitySpark(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntitySpark(World world) {
        super(WizardryEntities.ENTITY_SPARK, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if(hitResult.getType() == HitResult.Type.ENTITY){
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            float damage = Spells.HOMING_SPARK.getFloatProperty(Spell.DAMAGE) * damageMultiplier;
            Entity entity = entityHitResult.getEntity();
            entity.damage(entity.getDamageSources().indirectMagic(this, this.getOwner()), damage);
        }

        this.playSound(WizardrySounds.ENTITY_HOMING_SPARK_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));

        // Particle effect
        if(getWorld().isClient){
            for(int i = 0; i < 8; i++){
                double x = this.prevX + random.nextDouble() - 0.5;
                double y = this.prevY + this.getHeight() / 2 + random.nextDouble() - 0.5;
                double z = this.prevZ + random.nextDouble() - 0.5;
                // FIXME: SPARK PARTICLE
                ParticleBuilder.create(WizardryParticles.LIGHTNING).pos(x, y, z).spawn(getWorld());
            }
        }

        super.onCollision(hitResult);
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public int getLifeTime() {
        return 50;
    }

    @Override
    public float getSeekingStrength() {
        return Spells.HOMING_SPARK.getFloatProperty(Spell.SEEKING_STRENGTH);
    }
}
