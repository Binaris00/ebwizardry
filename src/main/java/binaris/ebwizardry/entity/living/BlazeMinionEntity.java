package binaris.ebwizardry.entity.living;

import binaris.ebwizardry.config.WizardryConfig;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.util.DrawingUtils;
import binaris.ebwizardry.util.EntityUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
@Deprecated
public class BlazeMinionEntity extends BlazeEntity implements SummonedEntity{
    private int lifetime = -1;
    private UUID casterUUID;

    public BlazeMinionEntity(EntityType<? extends BlazeEntity> entityType, World world) {
        super(entityType, world);
    }
    public BlazeMinionEntity(World world) {
        this(WizardryEntities.ENTITY_BLAZE_MINION, world);
    }

    @Override
    public int getLifetime() {
        return lifetime;
    }

    @Override
    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    @Override
    public @Nullable UUID getOwnerId() {
        return casterUUID;
    }

    @Override
    public void setOwnerId(UUID uuid) {
        this.casterUUID = uuid;
        if (!this.getWorld().isClient && EntityUtil.getEntityByUUID(this.getWorld(), uuid) != null) {
            this.setCustomName(Text.translatable(NAMEPLATE_TRANSLATION_KEY, EntityUtil.getEntityByUUID(getWorld(), uuid).getName(), this.getName()));
        }
        this.casterUUID = uuid;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.targetSelector.getGoals().clear();
        this.targetSelector.add(1, new RevengeGoal(this));
        targetSelector.add(2, new ActiveTargetGoal<>(this, LivingEntity.class, 0, false, true, this.getTargetSelector()));
    }

    @Override
    public void setAttacker(@Nullable LivingEntity attacker) {
        if(this.shouldRevengeTarget(attacker)) super.setAttacker(attacker);
    }

    @Override
    public void tick() {
        super.tick();
        this.updateDelegate();
    }

    @Override
    public void onSpawn() {
        this.spawnParticleEffect();
    }

    @Override
    public void onDespawn() {
        this.spawnParticleEffect();
    }

    protected void spawnParticleEffect() {
        if (this.getWorld().isClient) {
            for (int i = 0; i < 15; i++) {
                this.getWorld().addParticle(ParticleTypes.FLAME, this.getX() + this.random.nextFloat() - 0.5f, this.getY() + this.random.nextFloat() * getHeight(), this.getZ() + this.random.nextFloat() - 0.5f, 0, 0, 0);
            }
        }
    }

    @Override
    public boolean isExperienceDroppingDisabled() {
        return true;
    }

    @Override
    public boolean hasParticleEffect() {
        return false;
    }

    @Override
    public int getAnimationColour(float animationProgress) {
        return DrawingUtils.mix(0xffdd4d, 0xff6600, animationProgress);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        return this.interactDelegate(player, hand) == ActionResult.FAIL ? super.interactMob(player, hand) : this.interactDelegate(player, hand);
    }

    @Override
    public boolean canPickUpLoot() {
        return false;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return getCaster() == null && getOwnerId() == null;
    }

    @Override
    public boolean canTarget(LivingEntity entity) {
        return true;
    }

    @Override
    public boolean isCustomNameVisible() {
        return true;
    }
    @Override
    public boolean hasCustomName() {
        return WizardryConfig.summonedCreatureNames && getCaster() != null;
    }
}
