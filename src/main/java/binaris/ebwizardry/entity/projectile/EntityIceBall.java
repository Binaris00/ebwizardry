package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.*;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.BlockUtils;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class EntityIceBall extends EntityMagicProjectile {
    public EntityIceBall(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntityIceBall(World world) {
        super(WizardryEntities.ICE_BALL, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if(!getWorld().isClient){
            Entity entity = entityHitResult.getEntity();

            if(entity != null) {
                float damage = Spells.ICE_BALL.getFloatProperty(Spell.DAMAGE) * damageMultiplier;

                entity.damage(entity.getDamageSources().indirectMagic(this, this.getOwner()), damage);

                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.addStatusEffect(new StatusEffectInstance(WizardryEffects.FROST,
                            Spells.ICE_BALL.getIntProperty(Spell.EFFECT_DURATION),
                            Spells.ICE_BALL.getIntProperty(Spell.EFFECT_STRENGTH)));
                }
            }
        }

        this.playSound(WizardrySounds.ENTITY_ICEBALL_HIT, 2, 0.8f + random.nextFloat() * 0.3f);
        this.discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        BlockPos pos = blockHitResult.getBlockPos();

        if(blockHitResult.getSide() == Direction.UP && !getWorld().isClient && getWorld().isDirectionSolid(pos, this, Direction.UP)
                && BlockUtils.canBlockBeReplaced(getWorld(), pos.up()) && BlockUtils.canPlaceBlock(getOwner(), getWorld(), pos)){
            getWorld().setBlockState(new BlockPos(pos.up()), Blocks.SNOW.getDefaultState());
        }

        this.playSound(WizardrySounds.ENTITY_ICEBALL_HIT, 2, 0.8f + random.nextFloat() * 0.3f);
        this.discard();

        super.onBlockHit(blockHitResult);
    }

    @Override
    public void tick() {
        super.tick();

        if(!getWorld().isClient){
            // Particle stuff
            this.getWorld().sendEntityStatus(this, (byte) 3);
        }
    }

    @Override
    public void handleStatus(byte status) {
        if(status == 3){
            for (int i=0; i<5; i++){
                ParticleBuilder.create(WizardryParticles.SNOW, random, prevX, prevY, prevZ, 0.4, false).scale(2)
                                .time(8 + random.nextInt(4)).spawn(getWorld());
            }
        }
        super.handleStatus(status);
    }

    @Override
    public int getLifeTime() {
        return 16;
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
