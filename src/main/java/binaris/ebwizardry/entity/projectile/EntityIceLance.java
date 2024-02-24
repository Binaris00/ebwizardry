package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.*;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class EntityIceLance extends EntityMagicArrow{
    public EntityIceLance(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntityIceLance(World world){
        super(WizardryEntities.ENTITY_ICE_LANCE, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    @Override
    public double getDamage() {
        return Spells.ICE_LANCE.getFloatProperty(Spell.DAMAGE);
    }

    @Override
    public int getLifetime() {
        return -1;
    }

    @Override
    public Identifier getTexture() {
        return new Identifier("ebwizardry", "textures/entity/ice_lance.png");
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if(entity instanceof LivingEntity livingEntity){
            livingEntity.addStatusEffect(new StatusEffectInstance(WizardryEffects.FROST, Spells.ICE_LANCE.getIntProperty(Spell.EFFECT_DURATION),
                    Spells.ICE_LANCE.getIntProperty(Spell.EFFECT_STRENGTH)));
        }
        this.playSound(WizardrySounds.ENTITY_ICE_LANCE_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if(getWorld().isClient){
            for(int j = 0; j < 10; j++){
                ParticleBuilder.create(WizardryParticles.ICE, this.random, this.getX(), this.getY(), this.getZ(), 0.5, true)
                        .time(20 + random.nextInt(10)).gravity(true).spawn(getWorld());
            }
        }
        this.playSound(WizardrySounds.ENTITY_ICE_LANCE_SMASH, 1.0F, random.nextFloat() * 0.4F + 1.2F);
        super.onBlockHit(blockHitResult);
    }
}
