package binaris.ebwizardry.entity.living;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.util.ParticleBuilder;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.EnumSet;
@Deprecated
public class IceWraithEntity extends BlazeMinionEntity{
    public IceWraithEntity(EntityType<? extends BlazeEntity> entityType, World world) {
        super(entityType, world);
    }
    public IceWraithEntity(World world) {
        super(WizardryEntities.ENTITY_ICE_WRAITH, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.getGoals().clear();
        this.goalSelector.add(4, new AIIceShardAttack(this));
        this.goalSelector.add(5, new GoToWalkTargetGoal(this, 1.0D));
        this.goalSelector.add(7, new WanderAroundGoal(this, 1.0D));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    protected void spawnParticleEffect() {
        if (this.getWorld().isClient) {
            for (int i = 0; i < 15; i++) {
                float brightness = 0.5f + (random.nextFloat() / 2);
                ParticleBuilder.create(WizardryParticles.SPARKLE, this)
                        .velocity(0, 0.05f, 0)
                        .time(20 + random.nextInt(10))
                        .color(brightness, brightness + 0.1f, 1.0f)
                        .spawn(getWorld());
            }
        }
    }

    @Override
    public void tickMovement() {
        if (!this.isOnGround() && this.getVelocity().y < 0.0D) {
            this.setVelocity(this.getVelocity().multiply(1, 0.6D, 1));
        }

        if (this.random.nextInt(24) == 0) {
            this.playSound(WizardrySounds.ENTITY_ICE_WRAITH_AMBIENT, 0.3F + this.random.nextFloat() / 4, this.random.nextFloat() * 0.7F + 1.4F);
        }

        if (this.getWorld().isClient) {
            for (int i = 0; i < 2; ++i) {
                this.getWorld().addParticle(ParticleTypes.CLOUD,
                        this.getX() + (this.random.nextDouble() - 0.5D) * (double) this.getHeight(),
                        this.getY() + this.random.nextDouble() * (double) this.getHeight(),
                        this.getZ() + (this.random.nextDouble() - 0.5D) * (double) this.getHeight(), 0.0D, 0.0D, 0.0D);
            }
        }

        super.tickMovement();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.isIn(DamageTypeTags.IS_DROWNING)  && (this.getAir() > 0 || this.hasStatusEffect(StatusEffects.WATER_BREATHING))) {
            return false;
        } else {
            return super.damage(source, amount);
        }
    }

    @Override
    public boolean isOnFire() {
        return this.getFlag(0);
    }

    static class AIIceShardAttack extends Goal {
        private final BlazeEntity blaze;
        private int attackStep;
        private int attackTime;

        public AIIceShardAttack(BlazeEntity blazeIn) {
            this.blaze = blazeIn;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }

        public boolean canStart() {
            LivingEntity livingentity = this.blaze.getTarget();
            return livingentity != null && livingentity.isAlive();
        }

        @Override
        public void start() {
            this.attackStep = 0;
        }
        @Override
        public void stop() {
            this.blaze.setOnFire(false);
        }
        @Override
        public void tick() {
            --this.attackTime;
            LivingEntity livingEntity = this.blaze.getTarget();
            if (livingEntity == null) return;
            double d0 = this.blaze.squaredDistanceTo(livingEntity);

            if (d0 < 4.0D) {
                if (this.attackTime <= 0) {
                    this.attackTime = 20;
                    this.blaze.tryAttack(livingEntity);
                }
                this.blaze.getMoveControl().moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0D);
            } else if (d0 < 256.0D) {
                if (this.attackTime <= 0) {
                    ++this.attackStep;

                    if (this.attackStep == 1) {
                        this.attackTime = 60;
                        this.blaze.setOnFire(true);
                    } else if (this.attackStep <= 4) {
                        this.attackTime = 6;
                    } else {
                        this.attackTime = 100;
                        this.attackStep = 0;
                        this.blaze.setOnFire(false);
                    }

                    if (this.attackStep > 1) {
                        Spells.ICE_SHARD.cast(this.blaze.getWorld(), this.blaze, Hand.MAIN_HAND, 0, livingEntity, new SpellModifiers());
                    }
                }
                this.blaze.getLookControl().lookAt(livingEntity, 10.0F, 10.0F);
            } else {
                this.blaze.getNavigation().stop();
                this.blaze.getMoveControl().moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0D);
            }
            super.tick();
        }
    }
}
