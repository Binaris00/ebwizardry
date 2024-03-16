package binaris.ebwizardry.entity.construct;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.item.SpellCastingItem;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.util.AllyDesignationSystem;
import binaris.ebwizardry.util.EntityUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * This class is for all inanimate magical constructs that are not projectiles. Generally speaking, subclasses of this
 * class are areas of effect which deal with damage or apply effects over time, including black hole, blizzard, tornado and
 * a few others. The caster UUID, lifetime and damage multiplier are stored here, and lifetime is also synced here.
 */
public abstract class EntityMagicConstruct extends Entity implements Tameable {
    /** The UUID of the caster. As of Wizardry 4.3, this <b>is</b> synced, and rather than storing the caster
     * instance via a weak reference, it is fetched from the UUID each time it is needed in
     * {@link EntityMagicConstruct#getCaster()}. */
    private UUID casterUUID;

    /** The time in ticks this magical construct lasts for; defaults to 600 (30 seconds). If this is -1 the construct
     * doesn't despawn. */
    public int lifetime = 600;

    /** The damage multiplier for this construct, determined by the wand with which it was cast. */
    public float damageMultiplier = 1.0f;
    public EntityMagicConstruct(EntityType<?> type, World world) {
        super(type, world);
        this.noClip = true;
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        if(lifetime == -1 && getCaster() == player && player.isSneaking() && player.getStackInHand(hand).getItem() instanceof SpellCastingItem){
            this.despawn();
            return ActionResult.SUCCESS;
        }

        return super.interactAt(player, hitPos, hand);
    }

    /**
     * Defaults to just setDead() in EntityMagicConstruct, but is provided to allow subclasses to override this e.g.
     * bubble uses it to dismount the entity inside it and play the 'pop' sound before calling super(). You should
     * always call super() when overriding this method, in case it changes. There is no need, therefore, to call
     * setDead() when overriding.
     */
    public void despawn() {
        this.discard();
    }


    @Override
    public void tick() {
        if (this.age > lifetime && lifetime != -1) {
            this.discard();
        }

        super.tick();
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if(nbt.getUuid("casterUUID") != null) casterUUID = nbt.getUuid("casterUUID");
        lifetime = nbt.getInt("lifetime");
        damageMultiplier = nbt.getFloat("damageMultiplier");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if(casterUUID != null){
            nbt.putUuid("casterUUID", casterUUID);
        }
        nbt.putInt("lifetime", lifetime);
        nbt.putFloat("damageMultiplier", damageMultiplier);
    }

    @Nullable
    @Override
    public UUID getOwnerUuid() {
        return casterUUID;
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return getCaster();
    }

    @Override
    public EntityView method_48926() {
        return null;
    }

    @Nullable
    public LivingEntity getCaster() {
        Entity entity = EntityUtil.getEntityByUUID(getWorld(), getOwnerUuid());

        if (entity != null && !(entity instanceof LivingEntity)) {
            Wizardry.LOGGER.warn("{} has a non-living owner!", this);
            entity = null;
        }

        return (LivingEntity) entity;
    }

    public void setCaster(@Nullable LivingEntity caster) {
        this.casterUUID = caster == null ? null : caster.getUuid();
    }

    public boolean isValidTarget(Entity target) {
        return AllyDesignationSystem.isValidTarget(this.getCaster(), target);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return WizardrySounds.SPELLS;
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }
}
