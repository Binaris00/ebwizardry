package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityThunderbolt extends EntityMagicProjectile{
    public EntityThunderbolt(World world) {
        super(WizardryEntities.ENTITY_THUNDERBOLT, world);
    }

    public EntityThunderbolt(EntityType<EntityThunderbolt> entityThunderboltEntityType, World world) {
        super(entityThunderboltEntityType, world);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }
    @Override
    public boolean doesRenderOnFire() {
        return false;
    }


    @Override
    protected void onCollision(HitResult hitResult) {
        if(hitResult instanceof EntityHitResult entityHitResult){
            Entity entity = entityHitResult.getEntity();
            float damage = Spells.THUNDERBOLT.getFloatProperty(Spell.DAMAGE) * damageMultiplier;

            entity.damage(entity.getDamageSources().indirectMagic(this, this.getOwner()), damage);
            float knockbackStrength = Spells.THUNDERBOLT.getFloatProperty(Spell.KNOCKBACK_STRENGTH);

            // Set the knockback
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).takeKnockback(knockbackStrength * 0.5F, MathHelper.sin(this.getYaw() * 0.017453292F), -MathHelper.cos(this.getYaw() * 0.017453292F));
            }

        }

        this.playSound(WizardrySounds.ENTITY_THUNDERBOLT_HIT, 1.4F, 0.5F + this.random.nextFloat() * 0.1F);
        if(!this.getWorld().isClient()){
            getWorld().sendEntityStatus(this, (byte)2);
            this.discard();
        }

        super.onCollision(hitResult);
    }

    @Override
    public void tick() {
        super.tick();

        if(!getWorld().isClient()){
            getWorld().sendEntityStatus(this, (byte)3);
        }
    }

    @Override
    public void handleStatus(byte status) {
        // Initial particle
        if(status == 2){
            getWorld().addParticle(ParticleTypes.EXPLOSION, prevX, prevY, prevZ, 0, 0, 0);
        }

        // Update
        if(status == 3){
            ParticleBuilder.create(WizardryParticles.SPARK, random, prevX, prevY + getHeight()/2, prevZ, 0.1, false).spawn(getWorld());
            for(int i = 0; i < 4; i++) {
                getWorld().addParticle(ParticleTypes.LARGE_SMOKE, prevX + random.nextFloat() * 0.2 - 0.1,
                        prevY + getHeight() / 2 + random.nextFloat() * 0.2 - 0.1,
                        prevZ + random.nextFloat() * 0.2 - 0.1, 0, 0, 0);
            }
        }
        super.handleStatus(status);
    }

    @Override
    public int getLifeTime() {
        return 8;
    }
}
