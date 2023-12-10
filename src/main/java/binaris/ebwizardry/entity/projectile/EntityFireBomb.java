package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.*;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.EntityUtil;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.List;

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

        // Spawn particles
        for(int i = 0; i < 60; i++){
            ParticleBuilder.create(WizardryParticles.MAGIC_FIRE, random, getX(), getY(), getZ(), 2*blastMultiplier, false).spawn(getWorld());
        }

        getWorld().addParticle(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 0, 0, 0);

        // Play sounds
        this.playSound(WizardrySounds.ENTITY_FIREBOMB_SMASH, 1.5F, random.nextFloat() * 0.4F + 0.6F);
        this.playSound(WizardrySounds.ENTITY_FIREBOMB_FIRE, 1, 1);

        // Splash damage
        if(hitResult instanceof BlockHitResult){
            List<LivingEntity> livingEntities = EntityUtil.getLivingEntitiesInRange(getWorld(), getX(), getY(), getZ(), Spells.FIRE_BOMB.getFloatProperty(Spell.RANGE));

            for(LivingEntity entity: livingEntities){
                if(entity != null && entity != this.getOwner()){
                    entity.damage(entity.getDamageSources().indirectMagic(this, this.getOwner()), Spells.FIRE_BOMB.getFloatProperty(Spell.SPLASH_DAMAGE) * damageMultiplier);
                    entity.setOnFireFor(Spells.FIRE_BOMB.getIntProperty(Spell.BURN_DURATION));
                }
            }
        }

        // Schedule a task to remove the entity after a delay
        if(this.age > 40){
            this.discard();
        }
    }
}
