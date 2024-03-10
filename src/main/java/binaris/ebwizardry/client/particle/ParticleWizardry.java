package binaris.ebwizardry.client.particle;

import binaris.ebwizardry.entity.ICustomHitbox;
import binaris.ebwizardry.util.EntityUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

/**
 * Abstract superclass for all of wizardry's particles.
 * <p></p>
 * The new system is as follows:
 * <p></p>
 * - All particle classes have a single constructor which takes a world and a position only.<br>
 * - Each particle class defines any relevant default values in its constructor, including velocity.<br>
 * - The particle builder then overwrites any other values that were set during building.
 * <p></p>
 *
 * @see ParticleBuilder
 */
@Environment(EnvType.CLIENT)
public abstract class ParticleWizardry extends SpriteBillboardParticle {

    /** The sprite of the particle. */
    SpriteProvider spriteProvider;

    // ------------------------- Some field properties -------------------------------- //
    /** A long value used by the renderer as a random number seed, ensuring anything that is randomized remains the
     * same across multiple frames. For example, lightning particles use this to keep their shape across ticks.
     * This value can also be set during particle creation, allowing users to keep randomized properties the same
     * even across multiple particles. If unspecified, the seed is chosen at random. */
    protected long seed;

    protected Random random = new Random(); // If we're not using a seed, this defaults to any old seed

    /** True if the particle is shaded, false if the particle always renders at full brightness. Defaults to false. */
    protected boolean shaded = false;

    protected float initialRed;
    protected float initialGreen;
    protected float initialBlue;

    protected float fadeRed = 0;
    protected float fadeGreen = 0;
    protected float fadeBlue = 0;

    protected float angle;
    protected double radius = 0;
    protected double speed = 0;

    /** The entity this particle is linked to. The particle will move with this entity. */
    @Nullable
    protected Entity entity = null;
    /** Coordinates of this particle relative to the linked entity. If the linked entity is null, these are used as
     * the absolute coordinates of the centre of rotation for particles with spin. If the particle has neither a
     * linked entity nor spin, these are not used. */
    protected double relativeX, relativeY, relativeZ;
    /** Velocity of this particle relative to the linked entity. If the linked entity is null, these are not used. */
    protected double relativeMotionX, relativeMotionY, relativeMotionZ;
    /** The yaw angle this particle is facing, or {@code NaN} if this particle always faces the viewer (default behaviour). */
    protected float yaw = Float.NaN;
    /** The pitch angle this particle is facing, or {@code NaN} if this particle always faces the viewer (default behaviour). */
    protected float pitch = Float.NaN;

    /** The fraction of the impact velocity that should be the maximum spread speed added on impact. */
    private static final double SPREAD_FACTOR = 0.2;
    /** Lateral velocity is reduced by this factor on impact, before adding random spread velocity. */
    private static final double IMPACT_FRICTION = 0.2;

    /** Previous-tick velocity, used in collision detection. */
    private double prevVelX, prevVelY, prevVelZ;
    private boolean adjustQuadSize;


    public ParticleWizardry(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.spriteProvider = spriteProvider; //Sets the sprite provider from above to the sprite provider in the constructor parameters
        this.relativeX = this.x;
        this.relativeY = this.y;
        this.relativeZ = this.z;

        this.setSpriteForAge(spriteProvider);
    }


    // ============================================== Parameter Setters ==============================================

    // Setters for parameters that affect all particles - these are implemented in this class (although they may be
    // reimplemented in subclasses)

    /** Sets the seed for this particle's randomly generated values and resets {@link ParticleWizardry#random} to use
     * that seed. Implementations will differ between particle types; for example, ParticleLightning has an update
     * period which changes the seed every few ticks, whereas ParticleVine simply retains the same seed for its entire
     * lifetime. */
    public void setSeed(long seed){
        this.seed = seed;
        this.random = new Random(seed);
    }

    /** Sets whether the particle should render at full brightness or not. True if the particle is shaded, false if
     * the particle always renders at full brightness. Defaults to false.*/
    public void setShaded(boolean shaded){
        this.shaded = shaded;
    }

    /** Sets this particle's gravity. True to enable gravity, false to disable. Defaults to false.*/
    public void setGravity(boolean gravity){
        this.gravityStrength = gravity ? 1 : 0;
    }

    /** Sets this particle's collisions. True to enable block collisions, false to disable. Defaults to false.*/
    public void setCollisions(boolean canCollide){
        this.collidesWithWorld = canCollide;
    }

    /**
     * Sets the velocity of the particle.
     * @param vx The x velocity
     * @param vy The y velocity
     * @param vz The z velocity
     */
    public void setVelocity(double vx, double vy, double vz){
        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityY = vz;
    }

    /**
     * Sets the spin parameters of the particle.
     * @param radius The spin radius
     * @param speed The spin speed in rotations per tick
     */
    public void setSpin(double radius, double speed){
        this.radius = radius;
        this.speed = speed * 2 * Math.PI; // Converts rotations per tick into radians per tick for the trig functions
        this.angle = this.random.nextFloat() * (float)Math.PI * 2; // Random start angle
        this.x = relativeX - radius * MathHelper.cos(angle);
        this.z = relativeZ + radius * MathHelper.sin(angle);

        // Set these to the correct values
        this.relativeMotionX = velocityX;
        this.relativeMotionY = velocityY;
        this.relativeMotionZ = velocityZ;
    }

    /**
     * Links this particle to the given entity. This will cause its position and velocity to be relative to the entity.
     * @param entity The entity to link to.
     */
    public void setEntity(Entity entity){
        this.entity = entity;
        // Set these to the correct values
        if(entity != null){
            this.setPos(this.entity.prevX + relativeX, this.entity.prevY
                    + relativeY, this.entity.prevZ + relativeZ);
            this.prevPosX = this.x;
            this.prevPosY = this.y;
            this.prevPosZ = this.z;
            // Set these to the correct values
            this.relativeMotionX = velocityX;
            this.relativeMotionY = velocityY;
            this.relativeMotionZ = velocityZ;
        }
    }

    /**
     * Sets the base color of the particle. <i>Note that this also sets the fade colour so that particles without a
     * fade colour do not change colour at all; as such fade colour must be set <b>after</b> calling this method.</i>
     * @param red The red color component
     * @param green The green color component
     * @param blue The blue colour component
     */
    @Override
    public void setColor(float red, float green, float blue) {
        super.setColor(red, green, blue);
        initialRed = red;
        initialGreen = green;
        initialBlue = blue;
        // If fade color is not specified, it defaults to the main color - this method is always called first
        setFadeColour(red, green, blue);
    }

    /**
     * Sets the fade color of the particle.
     * @param r The red color component
     * @param g The green color component
     * @param b The blue colour component
     */
    public void setFadeColour(float r, float g, float b){
        this.fadeRed = r;
        this.fadeGreen = g;
        this.fadeBlue = b;
    }

    /**
     * Sets the direction this particle faces. This will cause the particle to render facing the given direction.
     * @param yaw The yaw angle of this particle in degrees, where 0 is south.
     * @param pitch The pitch angle of this particle in degrees, where 0 is horizontal.
     */
    public void setFacing(float yaw, float pitch){
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Sets the target position for this particle. This will cause it to stretch to touch the given position,
     * if supported.
     * @param x The x-coordinate of the target position.
     * @param y The y-coordinate of the target position.
     * @param z The z-coordinate of the target position.
     */
    public void setTargetPosition(double x, double y, double z){
        // Does nothing for normal particles since normal particles always render at a single point
    }

    /**
     * Sets the target point velocity for this particle. This will cause the position it stretches to touch to move
     * at the given velocity. Has no effect unless {@link ParticleWizardry#setTargetVelocity(double, double, double)}
     * is also used.
     * @param vx The x velocity of the target point.
     * @param vy The y velocity of the target point.
     * @param vz The z velocity of the target point.
     */
    public void setTargetVelocity(double vx, double vy, double vz){
        // Does nothing for normal particles since normal particles always render at a single point
    }

    /**
     * Links this particle to the given target. This will cause it to stretch to touch the target, if supported.
     * @param target The target to link to.
     */
    public void setTargetEntity(Entity target){
        // Does nothing for normal particles since normal particles always render at a single point
    }

    /**
     * Sets the length of this particle. This will cause it to stretch to touch a point this distance along its
     * linked entity's line of sight.
     * @param length The length to set.
     */
    public void setLength(double length){
        // Does nothing for normal particles since normal particles always render at a single point
    }

    // ============================================== Method Overrides ==============================================
    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected int getBrightness(float tint) {
        return shaded ? super.getBrightness(tint) : 15728880;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Entity viewer = camera.getFocusedEntity();

        updateEntityLinking(viewer, tickDelta);

        if (Float.isNaN(this.yaw) || Float.isNaN(this.pitch)) {
            super.buildGeometry(vertexConsumer, camera, tickDelta);
        } else {
            float degToRadFactor = 0.017453292f;

            float rotationX = MathHelper.cos(yaw * degToRadFactor);
            float rotationZ = MathHelper.sin(yaw * degToRadFactor);
            float rotationY = MathHelper.cos(pitch * degToRadFactor);
            float rotationYZ = -rotationZ * MathHelper.sin(pitch * degToRadFactor);
            float rotationXY = rotationX * MathHelper.sin(pitch * degToRadFactor);

            drawParticle(vertexConsumer, camera, tickDelta, rotationX, rotationY, rotationZ, rotationYZ, rotationXY);
        }
    }

    protected void drawParticle(VertexConsumer buffer, Camera camera, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ){
        Vec3d vec3 = camera.getPos();

        float s = this.adjustQuadSize ? 0.1f : 1;
        float f4 = s * this.getSize(partialTicks);

        float f = this.getMinU();
        float f1 = this.getMaxU();
        float f2 = this.getMinV();
        float f3 = this.getMaxV();

        float f5 = (float) (MathHelper.lerp(partialTicks, this.prevPosX, this.x) - vec3.getX());
        float f6 = (float) (MathHelper.lerp(partialTicks, this.prevPosY, this.y) - vec3.getY());
        float f7 = (float) (MathHelper.lerp(partialTicks, this.prevPosY, this.z) - vec3.getZ());

        int i = this.getBrightness(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;

        Vec3d[] avec3d = new Vec3d[]{new Vec3d(-rotationX * f4 - rotationXY * f4, -rotationZ * f4, -rotationYZ * f4 - rotationX * f4),
                new Vec3d(-rotationX * f4 + rotationXY * f4, rotationZ * f4, -rotationYZ * f4 + rotationXZ * f4),
                new Vec3d(rotationX * f4 + rotationXY * f4, rotationZ * f4, rotationYZ * f4 + rotationXZ * f4),
                new Vec3d(rotationX * f4 - rotationXY * f4, -rotationZ * f4, rotationYZ * f4 - rotationXZ * f4)};

        if (this.angle != 0.0F) {
            float f8 = this.angle + (this.angle - this.prevAngle) * partialTicks;
            float f9 = MathHelper.cos(f8 * 0.5F);
            float f10 = MathHelper.sin(f8 * 0.5F) * camera.getRotation().x();
            float f11 = MathHelper.sin(f8 * 0.5F) * camera.getRotation().y();
            float f12 = MathHelper.sin(f8 * 0.5F) * camera.getRotation().z();
            Vec3d vec3d = new Vec3d(f10, f11, f12);

            for (int l = 0; l < 4; ++l) {
                avec3d[l] = vec3d.multiply(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].multiply((double) (f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).multiply(2.0F * f9));
            }
        }

        buffer.vertex((double) f5 + avec3d[0].x, (double) f6 + avec3d[0].y, (double) f7 + avec3d[0].z).texture(f1, f3).color(this.red, this.green, this.blue, this.alpha).light(j, k).next();
        buffer.vertex((double) f5 + avec3d[1].x, (double) f6 + avec3d[1].y, (double) f7 + avec3d[1].z).texture(f1, f2).color(this.red, this.green, this.blue, this.alpha).light(j, k).next();
        buffer.vertex((double) f5 + avec3d[2].x, (double) f6 + avec3d[2].y, (double) f7 + avec3d[2].z).texture(f, f2).color(this.red, this.green, this.blue, this.alpha).light(j, k).next();
        buffer.vertex((double) f5 + avec3d[3].x, (double) f6 + avec3d[3].y, (double) f7 + avec3d[3].z).texture(f, f3).color(this.red, this.green, this.blue, this.alpha).light(j, k).next();
    }


    protected void updateEntityLinking(Entity viewer, float partialTicks) {
        if (this.entity != null) {
            x = x + entity.prevX - entity.getX() - relativeMotionX * (1 - partialTicks);
            y = y + entity.prevY - entity.getY() - relativeMotionY * (1 - partialTicks);
            z = z + entity.prevZ - entity.getZ() - relativeMotionZ * (1 - partialTicks);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.collidesWithWorld && this.onGround) {
            this.velocityX /= 0.699999988079071D;
            this.velocityZ /= 0.699999988079071D;
        }

        if (entity != null || radius > 0) {
            double x = relativeX;
            double y = relativeY;
            double z = relativeZ;

            if (this.entity != null) {
                if (!this.entity.isAlive()) {
                    this.markDead();
                } else {
                    x += this.entity.getX();
                    y += this.entity.getY();
                    z += this.entity.getZ();
                }
            }

            if (radius > 0) {
                angle += speed;
                x += radius * -MathHelper.cos(angle);
                z += radius * MathHelper.sin(angle);
            }

            this.setPos(x, y, z);

            this.relativeX += relativeMotionX;
            this.relativeY += relativeMotionY;
            this.relativeZ += relativeMotionZ;
        }

        float ageFraction = (float) this.age / (float) this.maxAge;

        this.red = this.initialRed + (this.fadeRed - this.initialRed) * ageFraction;
        this.green = this.initialGreen + (this.fadeGreen - this.initialGreen) * ageFraction;
        this.blue = this.initialBlue + (this.fadeBlue - this.initialBlue) * ageFraction;

        if (collidesWithWorld) {
            if (this.velocityX == 0 && this.prevVelX != 0) {
                this.velocityY *= IMPACT_FRICTION;
                this.velocityZ *= IMPACT_FRICTION;

                this.velocityY += (random.nextDouble() * 2 - 1) * this.prevVelX * SPREAD_FACTOR;
                this.velocityZ += (random.nextDouble() * 2 - 1) * this.prevVelX * SPREAD_FACTOR;
            }

            if (this.velocityY == 0 && this.prevVelY != 0) {
                this.velocityX *= IMPACT_FRICTION;
                this.velocityZ *= IMPACT_FRICTION;

                this.velocityX += (random.nextDouble() * 2 - 1) * this.prevVelY * SPREAD_FACTOR;
                this.velocityZ += (random.nextDouble() * 2 - 1) * this.prevVelY * SPREAD_FACTOR;
            }

            if (this.velocityZ == 0 && this.prevVelZ != 0) {
                this.velocityX *= IMPACT_FRICTION;
                this.velocityY *= IMPACT_FRICTION;

                this.velocityX += (random.nextDouble() * 2 - 1) * this.prevVelZ * SPREAD_FACTOR;
                this.velocityY += (random.nextDouble() * 2 - 1) * this.prevVelZ * SPREAD_FACTOR;
            }

            double searchRadius = 20;

            List<Entity> nearbyEntities = EntityUtil.getEntitiesWithinRadius(searchRadius, this.x, this.y, this.z, world, Entity.class);

            if (nearbyEntities.stream().anyMatch(e -> e instanceof ICustomHitbox && ((ICustomHitbox) e).calculateIntercept(new Vec3d(x, y, z), new Vec3d(x, y, z), 0) != null))
                this.markDead();
        }

        this.prevVelX = velocityX;
        this.prevVelY = velocityY;
        this.prevVelZ = velocityZ;
    }

    // Overridden and copied to fix the collision behavior
    @Override
    public void move(double dx, double dy, double dz) {
        double d0 = dx;
        double d1 = dy;
        double d2 = dz;
        if (this.collidesWithWorld && (dx != 0.0D || dy != 0.0D || dz != 0.0D) && dx * dx + dy * dy + dz * dz < MathHelper.square(100.0D)) {
            Vec3d vec3 = Entity.adjustMovementForCollisions(null, new Vec3d(dx, dy, dz), this.getBoundingBox(), this.world, List.of());
            dx = vec3.x;
            dy = vec3.y;
            dz = vec3.z;
        }

        if (dx != 0.0D || dy != 0.0D || dz != 0.0D) {
            this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
            this.repositionFromBoundingBox();
        }

        this.onGround = d1 != dy && d1 < 0.0D;

        if (d0 != dx) {
            this.velocityX = 0.0D;
        }

        if (d1 != dy) {
            this.velocityY = 0.0D;
        }

        if (d2 != dz) {
            this.velocityZ = 0.0D;
        }
    }

    public abstract ParticleWizardry getParticle(ClientWorld world, double x, double y, double z);
}
