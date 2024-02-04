package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.client.renderer.MagicArrowRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import binaris.ebwizardry.spell.SpellArrow;

/**
 * Originally copied from EntityArrow in 1.7.10 and updated to be more clean and efficient.
 * <p>
 * Now this class uses the base code from {@link PersistentProjectileEntity}
 * and adds some methods to make it easier to use with the mod.
 * This is used as a base class for all the magic arrows spelled by the {@link SpellArrow} class.
 * <p>
 * The methods {@link #aim(LivingEntity, float)} and {@link #aim(LivingEntity, Entity, float, float)} are used
 * to set the shooter of the projectile and aim it in the direction they are looking.
 * (Originally copied from Ebwizardry 1.12.2)
 * <p>
 * To register the renderer for this entity,
 * use {@link MagicArrowRenderer} and register the respective texture in {@link  EntityMagicArrow#getTexture()}.
 * */
public abstract class EntityMagicArrow extends PersistentProjectileEntity {
    public static final double LAUNCH_Y_OFFSET = 0.1;
    int ticksInGround;
    int ticksInAir;
    public EntityMagicArrow(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    /** Sets the shooter of the projectile to the given caster, positions the projectile at the given caster's eyes and
     * aims it in the direction they are looking with the given speed. */
    public void aim(LivingEntity caster, float speed){
        this.setOwner(caster);

        this.updatePositionAndAngles(caster.getX(), caster.getY() + caster.getStandingEyeHeight() - LAUNCH_Y_OFFSET, caster.getZ()
                , caster.getYaw(), caster.getPitch());

        this.prevX -= MathHelper.cos(this.getYaw() / 180.0F * (float)Math.PI) * 0.16F;
        this.prevY -= 0.10000000149011612D;
        this.prevZ -= MathHelper.cos(this.getYaw() / 180.0F * (float)Math.PI) * 0.16F;

        this.setPosition(prevX, prevY, prevZ);

        double motionX = -MathHelper.sin(this.getYaw() / 180.0F * (float)Math.PI)
                * MathHelper.cos(this.getPitch() / 180.0F * (float)Math.PI);
        double motionY = -MathHelper.sin(this.getPitch() / 180.0F * (float)Math.PI);
        double motionZ = MathHelper.cos(this.getYaw() / 180.0F * (float)Math.PI)
                * MathHelper.cos(this.getPitch() / 180.0F * (float)Math.PI);

        this.setVelocity(motionX, motionY, motionZ, speed * 1.5F, 1.0F);

    }

    /** Sets the shooter of the projectile to the given caster, positions the projectile at the given caster's eyes and
     * aims it at the given target with the given speed. The trajectory will be altered slightly by a random amount
     * determined by the aimingError parameter. For reference, skeletons set this to 10 on easy, 6 on normal and 2 on hard
     * difficulty. */
    public void aim(LivingEntity caster, Entity target, float speed, float aimingError){
        this.setOwner(caster);

        this.prevY = caster.prevY + (double)caster.getStandingEyeHeight() - LAUNCH_Y_OFFSET;
        double dx = target.prevX - caster.prevX;
        double dy = !this.hasNoGravity() ?
                target.prevY + (double)(target.getHeight() / 3.0f) - this.prevY
                : target.prevY + (double)(target.getHeight() / 2.0f) - this.prevY;
        double dz = target.prevZ - caster.prevZ;
        double horizontalDistance = MathHelper.sqrt((float) (dx * dx + dz * dz));

        if(horizontalDistance >= 1.0E-7D){
            float yaw = (float)(Math.atan2(dz, dx) * 180.0d / Math.PI) - 90.0f;
            float pitch = (float)(-(Math.atan2(dy, horizontalDistance) * 180.0d / Math.PI));
            double dxNormalised = dx / horizontalDistance;
            double dzNormalised = dz / horizontalDistance;
            this.updatePositionAndAngles(caster.prevX + dxNormalised, this.prevY, caster.prevZ + dzNormalised, yaw, pitch);

            float bulletDropCompensation = !this.hasNoGravity() ? (float)horizontalDistance * 0.2f : 0;
            this.setVelocity(dx, dy + (double)bulletDropCompensation, dz, speed, aimingError);
        }
    }


    // ======================= Property getters (to be overridden by subclasses) =======================

    /** Subclasses must override this to set their own base damage. */
    public abstract double getDamage();

    /** Returns the maximum flight time in ticks before this projectile disappears, or -1 if it can continue
     * indefinitely until it hits something. This should be constant. */
    public abstract int getLifetime();

    /**
     * This method is used to get the texture for the magic arrow.
     * The texture is represented by an Identifier object.
     * Subclasses of EntityMagicArrow must override this method to provide their own texture.
     *
     * @return Identifier object representing the texture of the magic arrow.
     */
    public abstract Identifier getTexture();

    /**
     * Override this to disable deceleration (generally speaking, this isn't noticeable unless gravity is turned off).
     * Returns true by default.
     */
    @Deprecated
    public boolean doDeceleration(){
        return true;
    }

    /**
     * Override this to allow the projectile to pass through mobs intact (the onEntityHit method will still be called
     * and damage will still be applied). Returns false by default.
     */
    @Deprecated
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

        if(inGround){
            ticksInGround++;
            tickInGround();
        } else {
            ticksInAir++;
            ticksInAir();
        }
    }

    public void tickInGround(){}
    public void ticksInAir(){}
}
