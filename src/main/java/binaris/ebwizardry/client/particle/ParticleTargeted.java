package binaris.ebwizardry.client.particle;

import binaris.ebwizardry.Wizardry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public abstract class ParticleTargeted extends ParticleWizardry{
    private static final double THIRD_PERSON_AXIAL_OFFSET = 1.2;

    protected double targetX;
    protected double targetY;
    protected double targetZ;
    protected double targetVelX;
    protected double targetVelY;
    protected double targetVelZ;

    protected double length;

    @Nullable
    protected Entity target = null;

    public ParticleTargeted(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, boolean updateTextureOnTick) {
        super(world, x, y, z, spriteProvider, updateTextureOnTick);
    }

    @Override
    public void setTargetPosition(double x, double y, double z) {
        this.targetX = x;
        this.targetY = y;
        this.targetZ = z;
    }

    @Override
    public void setTargetVelocity(double vx, double vy, double vz) {
        this.targetVelX = vx;
        this.targetVelY = vy;
        this.targetVelZ = vz;
    }

    @Override
    public void setTargetEntity(Entity target) {
        this.target = target;
    }

    @Override
    public void setLength(double length) {
        this.length = length;
    }

    @Override
    public void tick() {
        super.tick();

        if (!Double.isNaN(targetVelX) && !Double.isNaN(targetVelY) && !Double.isNaN(targetVelZ)) {
            this.targetX += this.targetVelX;
            this.targetY += this.targetVelY;
            this.targetZ += this.targetVelZ;
        }
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Entity viewer = camera.getFocusedEntity();
        MatrixStack stack = new MatrixStack();
        updateEntityLinking(camera.getFocusedEntity(), tickDelta);

        float x = (float) (this.prevPosX + (this.x - this.prevPosX) * (double) tickDelta);
        float y = (float) (this.prevPosY + (this.y - this.prevPosY) * (double) tickDelta);
        float z = (float) (this.prevPosZ + (this.z - this.prevPosZ) * (double) tickDelta);

        if (this.entity != null && this.shouldApplyOriginOffset()) {
            if (this.entity != viewer || MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON) {
                Vec3d look = entity.getRotationVec(tickDelta).multiply(THIRD_PERSON_AXIAL_OFFSET);
                x += (float) look.x;
                y += (float) look.y;
                z += (float) look.z;
            }
        }

        if (this.target != null) {
            this.targetX = this.target.prevX + (this.target.getX() - this.target.prevX) * tickDelta;
            double correction = this.target.getY() - this.target.prevY;
            this.targetY = this.target.prevY + (this.target.getY() - this.target.prevY) * tickDelta + target.getHeight() / 2 + correction;
            this.targetZ = this.target.prevZ + (this.target.getZ() - this.target.prevZ) * tickDelta;
        } else if (this.entity != null && this.length > 0) {
            Vec3d look = entity.getRotationVec(tickDelta).multiply(length);
            this.targetX = x + look.x;
            this.targetY = y + look.y;
            this.targetZ = z + look.z;
        }

        if (Double.isNaN(targetX) || Double.isNaN(targetY) || Double.isNaN(targetZ)) {
            Wizardry.LOGGER.warn("Attempted to render a targeted particle, but neither its target entity nor target" + "position was set, and it either had no length assigned or was not linked to an entity!");
            return;
        }

        stack.push();

        stack.translate(x - camera.getPos().x, y - camera.getPos().y, z - camera.getPos().z);

        double dx = this.targetX - x;
        double dy = this.targetY - y;
        double dz = this.targetZ - z;

        if (!Double.isNaN(targetVelX) && !Double.isNaN(targetVelY) && !Double.isNaN(targetVelZ)) {
            dx += tickDelta * this.targetVelX;
            dy += tickDelta * this.targetVelY;
            dz += tickDelta * this.targetVelZ;
        }

        float length = MathHelper.sqrt((float) (dx * dx + dy * dy + dz * dz));

        float yaw = (float) (180d / Math.PI * Math.atan2(dx, dz));
        float pitch = (float) (180f / (float) Math.PI * Math.atan(-dy / Math.sqrt(dz * dz + dx * dx)));

        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
        stack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(pitch));

        Tessellator tessellator = Tessellator.getInstance();

        this.draw(stack, tessellator, length, tickDelta);

        stack.pop();
    }

    protected boolean shouldApplyOriginOffset() {
        return true;
    }

    protected abstract void draw(MatrixStack stack, Tessellator tessellator, float length, float tickDelta);
}
