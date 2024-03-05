package binaris.ebwizardry.entity.living;

import binaris.ebwizardry.config.WizardryConfig;
import binaris.ebwizardry.util.EntityUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EntitySkeletonMinion extends SkeletonEntity implements SummonedEntity {
    private int lifetime = -1;
    private UUID casterUUID;

    public EntitySkeletonMinion(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntitySkeletonMinion(World world) {
        this(EntityType.SKELETON, world);
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
        if (!this.getWorld().isClient && EntityUtil.getEntityByUUID(this.getWorld(), uuid) != null) {
            this.setCustomName(Text.translatable(NAMEPLATE_TRANSLATION_KEY, EntityUtil.getEntityByUUID(this.getWorld(), uuid).getName(), this.getName()));
        }
        this.casterUUID = uuid;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.targetSelector.getGoals().clear();
        this.goalSelector.add(1, new RevengeGoal(this));
        this.goalSelector.add(2, new ActiveTargetGoal<>(this, LivingEntity.class, 0, false, true, this.getTargetSelector()));
    }

    @Override
    public void tick() {
        super.tick();
        this.updateDelegate();
    }

    @Override
    public void onSpawn() {
        // TODO: Item artifact to put helmet to undead mobs
        spawnParticleEffect();
    }

    @Override
    public void onDespawn() {
        this.spawnParticleEffect();
    }

    private void spawnParticleEffect() {
        if (this.getWorld().isClient) {
            for (int i = 0; i < 15; i++) {
                this.getWorld().addParticle(ParticleTypes.LARGE_SMOKE, this.getX() + this.random.nextFloat() - 0.5f, this.getY() + this.random.nextFloat() * 2, this.getZ() + this.random.nextFloat() - 0.5f, 0, 0, 0);
            }
        }
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        // No equipment...
    }

    @Override
    protected boolean shouldDropLoot() {
        // TODO: Maybe a option to drop loot?
        return false;
    }
    @Override
    public boolean hasParticleEffect() {
        return true;
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
    public boolean canTarget(LivingEntity target) {
        return !(target instanceof FlyingEntity)
                // FIXME: Temporary fix for the issue that the minion can't attack the caster.
                && !(target == getCaster());
    }

    // Name
    @Override
    public boolean isCustomNameVisible() {
        return true;
    }
    @Override
    public boolean shouldRenderName() {
        return true;
    }
    @Override
    public boolean hasCustomName() {
        return WizardryConfig.summonedCreatureNames && getCaster() != null;
    }
}

