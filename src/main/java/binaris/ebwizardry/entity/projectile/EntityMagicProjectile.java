package binaris.ebwizardry.entity.projectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntityMagicProjectile extends ThrownItemEntity {
    public static final double LAUNCH_Y_OFFSET = 0.1;
    public static final int SEEKING_TIME = 15;

    public float damageMultiplier = 1.0f;

    public EntityMagicProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntityMagicProjectile(EntityType<? extends ThrownItemEntity> entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
    }

    @Override
    public void tick() {
        super.tick();

        if(this.getLifeTime() >= 0 && this.age > this.getLifeTime()){
            this.discard();
        }
    }


    // Initialiser methods

    /** Sets the shooter of the projectile to the given caster, positions the projectile at the given caster's eyes and
     * aims it in the direction they are looking with the given speed. */
    public void aim(LivingEntity caster, float speed){
        this.setPosition(caster.prevX, caster.prevY + (double)caster.getStandingEyeHeight() - LAUNCH_Y_OFFSET, caster.prevZ);
        // This is the standard set of parameters for this method, used by snowballs and ender pearls amongst others.

        this.setVelocity(caster, caster.getPitch(), caster.getYaw(), 0.0f, speed, 1.0f);
        this.setOwner(caster);
    }

    /** Sets the shooter of the projectile to the given caster, positions the projectile at the given caster's eyes and
     * aims it at the given target with the given speed. The trajectory will be altered slightly by a random amount
     * determined by the aimingError parameter. For reference, skeletons set this to 10 on easy, 6 on normal and 2 on hard
     * difficulty. */
    public void aim(LivingEntity caster, Entity target, float speed, float aimingError){
        this.setOwner(caster);

        this.prevY = caster.prevX + (double)caster.getStandingEyeHeight() - LAUNCH_Y_OFFSET;
        double dx = target.prevX - caster.prevX;
        double dy = !this.hasNoGravity() ? target.prevY + (double)(target.getHeight() / 3.0f) - this.prevY
                : target.prevY + (double)(target.getHeight() / 2.0f) - this.prevY;
        double dz = target.prevZ - caster.prevX;
        double horizontalDistance = MathHelper.sqrt((float) (dx * dx + dz * dz));

        if(horizontalDistance >= 1.0E-7D){

            double dxNormalised = dx / horizontalDistance;
            double dzNormalised = dz / horizontalDistance;
            this.setPosition(caster.prevX + dxNormalised, this.prevY, caster.prevZ + dzNormalised);

            // Depends on the horizontal distance between the two entities and accounts for a bullet drop,
            // but, if gravity is ignored, this should be 0 since there is no bullet drop.
            float bulletDropCompensation = !this.hasNoGravity() ? (float)horizontalDistance * 0.2f : 0;

            // It turns out that this method normalizes the input (x, y, z) anyway
            this.setVelocity(dx, dy + (double)bulletDropCompensation, dz, speed, aimingError);
        }
    }


    /**
     * Returns the seeking strength of this projectile, or the maximum distance from a target the projectile can be
     * heading for that will make it curve towards that target. By default, this is 2 if the caster is wearing a ring
     * of attraction, otherwise it is 0.
     */
    public float getSeekingStrength(){
        // TODO: Ring of attraction here...
        // return getOwner() instanceof PlayerEntity && ItemArtefact.isArtefactActive((EntityPlayer)getThrower(), WizardryItems.ring_seeking) ? 2 : 0;
        return 0;
    }

    @Override
    protected Item getDefaultItem() {
        return null;
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        damageMultiplier = nbt.getFloat("damageMultiplier");

    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putFloat("damageMultiplier", damageMultiplier);
        return super.writeNbt(nbt);
    }

    public int getLifeTime() {
        return -1;
    }
}
