package binaris.ebwizardry.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Originally copied from EntityArrow in 1.7.10 and updated to be more clean and efficient.
 *
 *
 * */
public abstract class EntityMagicArrow extends PersistentProjectileEntity {
    public static final double LAUNCH_Y_OFFSET = 0.1;
    public static final int SEEKING_TIME = 15;
    private int blockX = -1;
    private int blockY = -1;
    private int blockZ = -1;
    public EntityMagicArrow(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    /** Sets the shooter of the projectile to the given caster, positions the projectile at the given caster's eyes and
     * aims it in the direction they are looking with the given speed. */
    public void aim(LivingEntity caster, float speed){
        this.setOwner(caster);

        this.setPosition(caster.getX(), caster.getY() + caster.getStandingEyeHeight() - LAUNCH_Y_OFFSET, caster.getZ());
        this.setRotation(caster.getPitch(), caster.getYaw());

        this.prevX -= MathHelper.cos(this.getYaw() / 180.0F * (float)Math.PI) * 0.16F;
        this.prevY -= 0.10000000149011612D;
        this.prevZ -= MathHelper.cos(this.getYaw() / 180.0F * (float)Math.PI) * 0.16F;

        this.setPosition(this.getX(), this.getY(), this.getZ());

    }

    /** Sets the shooter of the projectile to the given caster, positions the projectile at the given caster's eyes and
     * aims it at the given target with the given speed. The trajectory will be altered slightly by a random amount
     * determined by the aimingError parameter. For reference, skeletons set this to 10 on easy, 6 on normal and 2 on hard
     * difficulty. */
    public void aim(LivingEntity caster, Entity target, float speed, float aimingError){
        this.setOwner(caster);

        this.prevY = caster.prevY + (double)caster.getStandingEyeHeight() - LAUNCH_Y_OFFSET;
        double dx = target.prevX - caster.prevX;
        double dy = this.doGravity() ? target.prevY + (double)(target.getHeight() / 3.0f) - this.prevY
                : target.prevY + (double)(target.getHeight() / 2.0f) - this.prevY;
        double dz = target.prevZ - caster.prevZ;
        double horizontalDistance = MathHelper.sqrt((float) (dx * dx + dz * dz));

        if(horizontalDistance >= 1.0E-7D){
            float yaw = (float)(Math.atan2(dz, dx) * 180.0d / Math.PI) - 90.0f;
            float pitch = (float)(-(Math.atan2(dy, horizontalDistance) * 180.0d / Math.PI));
            double dxNormalised = dx / horizontalDistance;
            double dzNormalised = dz / horizontalDistance;

            this.setPosition(this.prevX + dxNormalised, this.prevY, this.prevZ + dzNormalised);
            this.setRotation(yaw, pitch);
        }
    }


    // ======================= Property getters (to be overridden by subclasses) =======================

    /** Subclasses must override this to set their own base damage. */
    public abstract double getDamage();

    /** Returns the maximum flight time in ticks before this projectile disappears, or -1 if it can continue
     * indefinitely until it hits something. This should be constant. */
    public abstract int getLifetime();

    /** Override this to disable gravity. Returns true by default. */
    public boolean doGravity(){
        return true;
    }

    /**
     * Override this to disable deceleration (generally speaking, this isn't noticeable unless gravity is turned off).
     * Returns true by default.
     */
    public boolean doDeceleration(){
        return true;
    }

    /**
     * Override this to allow the projectile to pass through mobs intact (the onEntityHit method will still be called
     * and damage will still be applied). Returns false by default.
     */
    public boolean doOverpenetration(){
        return false;
    }

    /**
     * Returns the seeking strength of this projectile, or the maximum distance from a target the projectile can be
     * heading for that will make it curve towards that target. By default, this is 2 if the caster is wearing a ring
     * of attraction, otherwise it is 0.
     */
    public float getSeekingStrength(){
        // TODO: Ring of seeking here
        // return getOwner() instanceof PlayerEntity && ItemArtefact.isArtefactActive((EntityPlayer)getCaster(),
        //        WizardryItems.ring_seeking) ? 2 : 0;
        return 0;
    }

    @Override
    public void tick() {
        super.tick();

        if(getLifetime() >= 0 && this.age >= getLifetime()){
            this.discard();
        }
    }

    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        super.setVelocity(x, y, z, speed, divergence);
    }
}
