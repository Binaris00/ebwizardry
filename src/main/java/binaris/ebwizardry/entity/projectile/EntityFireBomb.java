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

    public EntityFireBomb(World world) {
        super(WizardryEntities.ENTITY_FIRE_BOMB, world);
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

        if(!getWorld().isClient()){
            // Sound
            this.playSound(WizardrySounds.ENTITY_FIREBOMB_SMASH, 1.5F, random.nextFloat() * 0.4F + 0.6F);
            this.playSound(WizardrySounds.ENTITY_FIREBOMB_FIRE, 1, 1);

            // Spawn particles
            this.getWorld().sendEntityStatus(this, (byte) 3);
            this.discard();
        }
    }
    @Override
    public void handleStatus(byte status) {
        if(status == 3){
            ParticleBuilder.create(WizardryParticles.FLASH).pos(this.getPos()).scale(5 * blastMultiplier).color(1, 0.6f, 0).spawn(getWorld());

            for (int i = 0; i < 60 * blastMultiplier; i++) {

                ParticleBuilder.create(WizardryParticles.MAGIC_FIRE, random, prevX, prevY, prevZ, 2 * blastMultiplier, false)
                        .time(10 + random.nextInt(4)).scale(2 + random.nextFloat()).spawn(getWorld());

                ParticleBuilder.create(WizardryParticles.DARK_MAGIC, random, prevX, prevY, prevZ, 2 * blastMultiplier, false)
                        .color(1.0f, 0.2f + random.nextFloat() * 0.4f, 0.0f).spawn(getWorld());
            }
            getWorld().addParticle(ParticleTypes.EXPLOSION, prevX, prevY, prevZ, 0, 0, 0);
        }
    }
}
