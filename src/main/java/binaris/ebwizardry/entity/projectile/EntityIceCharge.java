package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.*;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.BlockUtils;
import binaris.ebwizardry.util.EntityUtil;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class EntityIceCharge extends EntityBomb {
    public final static String ICE_SHARDS = "ice_shards";
    public EntityIceCharge(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntityIceCharge(World world) {
        super(WizardryEntities.ENTITY_ICE_CHARGE, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        Entity entity = hitResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) hitResult).getEntity() : null;

        if (entity != null) {
            float damage = Spells.ICE_CHARGE.getFloatProperty(Spell.DAMAGE) * damageMultiplier;

            entity.damage(entity.getDamageSources().indirectMagic(this, this.getOwner()), damage);

            if (entity instanceof LivingEntity livingEntity)
                livingEntity.addStatusEffect(new StatusEffectInstance(WizardryEffects.FROST, Spells.ICE_CHARGE.getIntProperty(Spell.DIRECT_EFFECT_DURATION), Spells.ICE_CHARGE.getIntProperty(Spell.DIRECT_EFFECT_STRENGTH)));
        }

        if (getWorld().isClient) {
            this.getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            for (int i = 0; i < 30 * blastMultiplier; i++) {
                ParticleBuilder.create(WizardryParticles.ICE, random, this.getX(), this.getY(), this.getZ(), 2 * blastMultiplier, false)
                        .time(35).gravity(true).spawn(getWorld());

                float brightness = 0.4f + random.nextFloat() * 0.5f;
                ParticleBuilder.create(WizardryParticles.DARK_MAGIC, random, this.getX(), this.getY(), this.getZ(), 2 * blastMultiplier, false)
                        .color(brightness, brightness + 0.1f, 1.0f).spawn(getWorld());
            }
        }

        if (!this.getWorld().isClient) {
            this.playSound(WizardrySounds.ENTITY_ICE_CHARGE_SMASH, 1.5f, random.nextFloat() * 0.4f + 0.6f);
            this.playSound(WizardrySounds.ENTITY_ICE_CHARGE_ICE, 1.2f, random.nextFloat() * 0.4f + 1.2f);

            double radius = Spells.ICE_CHARGE.getFloatProperty(Spell.EFFECT_RADIUS) * blastMultiplier;

            List<LivingEntity> targets = EntityUtil.getLivingEntitiesInRange(getWorld(), this.getX(), this.getY(), this.getZ(), radius);

            for (LivingEntity target : targets) {
                if (target != entity && target != this.getOwner()) {
                    target.addStatusEffect(new StatusEffectInstance(WizardryEffects.FROST, Spells.ICE_CHARGE.getIntProperty(Spell.SPLASH_EFFECT_DURATION), Spells.ICE_CHARGE.getIntProperty(Spell.SPLASH_EFFECT_STRENGTH)));
                }
            }

            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    BlockPos pos = new BlockPos((int) (this.getX() + i), (int) this.getY(), (int) (this.getZ() + j));

                    Integer y = BlockUtils.getNearestSurface(getWorld(), pos, Direction.UP, 7, true, BlockUtils.SurfaceCriteria.SOLID_LIQUID_TO_AIR);

                    if (y != null) {
                        pos = new BlockPos(pos.getX(), y, pos.getZ());

                        double dist = this.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ());

                        if (random.nextInt((int) dist * 2 + 1) < 1 && dist < 2) {
                            if (getWorld().getBlockState(pos.down()).getBlock() == Blocks.WATER) {
                                getWorld().setBlockState(pos.down(), Blocks.ICE.getDefaultState());
                            } else {
                                getWorld().setBlockState(pos, Blocks.SNOW.getDefaultState());
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < Spells.ICE_CHARGE.getIntProperty(ICE_SHARDS); i++) {
                double dx = random.nextDouble() - 0.5;
                double dy = random.nextDouble() - 0.5;
                double dz = random.nextDouble() - 0.5;
                EntityIceShard iceShard = new EntityIceShard(getWorld());
                iceShard.setPos(this.getX() + dx, this.getY() + dy, this.getZ() + dz);
                iceShard.setVelocity(dx * 1.5, dy * 1.5, dz * 1.5);
                getWorld().spawnEntity(iceShard);
            }

            this.discard();
        }
    }
}
