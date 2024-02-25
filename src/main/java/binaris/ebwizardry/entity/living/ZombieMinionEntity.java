package binaris.ebwizardry.entity.living;

import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
// Deprecated because I don't test it yet
@Deprecated
public class ZombieMinionEntity extends ZombieEntity implements SummonedEntity{
    private int lifetime = -1;
    private UUID casterUUID;

    public ZombieMinionEntity(World world) {
        this(WizardryEntities.ENTITY_ZOMBIE_MINION, world);
    }
    public ZombieMinionEntity(EntityType<? extends ZombieEntity> entityEntityType, World world) {
        super(entityEntityType, world);
    }


    // Setter + getter implementations
    @Override
    public int getLifetime(){
        return lifetime;
    }
    @Override
    public void setLifetime(int lifetime){
        this.lifetime = lifetime;
    }
    @Override
    public UUID getOwnerId(){
        return casterUUID;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(6, new MoveThroughVillageGoal(this, 1.0, false, 4, this::canBreakDoors));
        this.goalSelector.add(2, new ActiveTargetGoal<>(this, LivingEntity.class, 0, false, true, this.getTargetSelector()));

        super.initGoals();
    }

    @Override
    public void setAttacker(@Nullable LivingEntity attacker) {
        if (this.shouldRevengeTarget(attacker)) super.setAttacker(attacker);
    }

    @Override
    public void onSpawn() {
        // TODO: Item artifact to put helmet to undead mobs
        spawnParticleEffect();
    }

    @Override
    public void onDespawn() {
        spawnParticleEffect();
    }

    @Override
    public boolean hasParticleEffect() {
        return true;
    }

    @Override
    public void setOwnerId(UUID uuid){
        this.casterUUID = uuid;
        if (!this.getWorld().isClient && EntityUtil.getEntityByUUID(this.getWorld(), uuid) != null) {
            this.setCustomName(Text.translatable(NAMEPLATE_TRANSLATION_KEY, EntityUtil.getEntityByUUID(getWorld(), uuid).getName(), this.getName()));
        }
        this.casterUUID = uuid;
    }

    @Override
    public @Nullable Entity getOwner() {
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        this.updateDelegate();
    }

    @Override
    public boolean canTarget(EntityType<?> type) {
        return true;
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
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        return this.interactDelegate(player, hand) == ActionResult.FAIL ? super.interactMob(player, hand) : this.interactDelegate(player, hand);
    }
    @Override
    public boolean isBaby() {
        return false;
    } // No baby...
    @Override
    public boolean isExperienceDroppingDisabled() {
        return true;
    }
    @Override
    public boolean isHolding(Item item) {
        return false;
    }
    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
    @Override
    public boolean canPickUpLoot() {
        return false;
    }
    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return getCaster() == null && getOwnerId() == null;
    }

    // Name

    @Override
    public boolean shouldRenderName() {
        return true;
    }
    @Override public boolean hasCustomName() { return true; }

}
