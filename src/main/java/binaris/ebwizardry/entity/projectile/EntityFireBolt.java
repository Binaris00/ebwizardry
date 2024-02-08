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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class EntityFireBolt extends EntityMagicProjectile {
    public EntityFireBolt(World world) {
        super(WizardryEntities.ENTITY_FIRE_BOLT, world);
    }
    public EntityFireBolt(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if(hitResult.getType() == HitResult.Type.ENTITY){
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            Entity entity = entityHitResult.getEntity();

            float damage = Spells.FIRE_BOLT.getFloatProperty(Spell.DAMAGE) * damageMultiplier;
            entity.damage(entity.getDamageSources().indirectMagic(this, this.getOwner()), damage);

            entity.setOnFireFor(Spells.FIRE_BOLT.getIntProperty(Spell.BURN_DURATION));
        }

        this.playSound(WizardrySounds.ENTITY_FIREBOLT_HIT, 2, 0.8f + random.nextFloat() * 0.3f);
        if(getWorld().isClient){
            for(int i = 0; i < 8; i++){
                getWorld().addParticle(ParticleTypes.LAVA, getX() + random.nextFloat() - 0.5, getY() + getHeight() / 2 + random.nextFloat() - 0.5, getZ() + random.nextFloat() - 0.5, 0, 0, 0);
            }
        }
        this.discard();
        super.onCollision(hitResult);
    }

    @Override
    public void tick() {
        super.tick();
        if(getWorld().isClient){
            ParticleBuilder.create(WizardryParticles.MAGIC_FIRE, this).time(14).spawn(getWorld());

            if(this.age > 1){
                double x = prevX - getVelocity().x / 2 + random.nextFloat() * 0.2 - 0.1;
                double y = prevY + getHeight() / 2 - getVelocity().y / 2 + random.nextFloat() * 0.2 - 0.1;
                double z = prevZ - getVelocity().z / 2 + random.nextFloat() * 0.2 - 0.1;
                ParticleBuilder.create(WizardryParticles.MAGIC_FIRE).pos(x, y, z).time(14).spawn(getWorld());
            }
        }
    }

    @Override
    public int getLifeTime() {
        return 6;
    }

    @Override
    public boolean hasNoGravity(){
        return true;
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }
}
