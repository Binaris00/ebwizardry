package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityIceShard extends EntityMagicArrow{
    public EntityIceShard(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
    public EntityIceShard(World world) {
        super(WizardryEntities.ENTITY_ICE_SHARD, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    @Override
    public double getDamage() {
        return Spells.ICE_SHARD.getFloatProperty(Spell.DAMAGE);
    }

    @Override
    public int getLifetime() {
        return -1;
    }

    @Override
    public Identifier getTexture() {
        return new Identifier("ebwizardry", "textures/entity/ice_shard.png");
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        // Adds a freeze effect to the target.
        if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, Spells.ICE_SHARD.getIntProperty(Spell.EFFECT_DURATION), Spells.ICE_SHARD.getIntProperty(Spell.EFFECT_STRENGTH), false, false));
        }

        this.playSound(WizardrySounds.ENTITY_ICE_SHARD_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if(getWorld().isClient){
            Vec3d vec3d = blockHitResult.getPos();
            ParticleBuilder.create(WizardryParticles.FLASH).pos(vec3d).color(0.75f, 1, 1).spawn(getWorld());

            for(int i = 0; i < 8; i++){
                ParticleBuilder.create(WizardryParticles.ICE, this.random, this.getX(), this.getY(), this.getZ(), 0.5, true)
                        .time(20 + this.random.nextInt(10)).gravity(true).spawn(this.getWorld());
            }

        }

        // Parameters for sound: sound event name, volume, pitch.
        this.playSound(WizardrySounds.ENTITY_ICE_SHARD_SMASH, 1.0F, random.nextFloat() * 0.4F + 1.2F);


        super.onBlockHit(blockHitResult);
    }

    @Override
    public void tickInGround() {
        if(this.ticksInGround > 40){
            this.discard();
        }
    }
}
