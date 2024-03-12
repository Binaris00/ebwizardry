package binaris.ebwizardry.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ParticleSphere extends ParticleWizardry{
    public ParticleSphere(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, false);
        this.setColor(1, 1, 1);
        this.maxAge = 5;
        this.alpha = 0.8f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        MatrixStack stack = new MatrixStack();

        updateEntityLinking(camera.getFocusedEntity(), tickDelta);

        float x = (float) (this.prevPosX + (this.x - this.prevPosX) * (double) tickDelta);
        float y = (float) (this.prevPosY + (this.y - this.prevPosY) * (double) tickDelta);
        float z = (float) (this.prevPosZ + (this.z - this.prevPosZ) * (double) tickDelta);

        stack.push();
        stack.translate(x - camera.getPos().x, y - camera.getPos().y, z - camera.getPos().z);

        RenderSystem.enableBlend();
        RenderSystem.enableCull();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);

        float latStep = (float) Math.PI / 20;
        float longStep = (float) Math.PI / 20;

        float sphereRadius = this.scale * (this.age + tickDelta - 1) / this.maxAge;
        float alpha = this.alpha * (1 - (this.age + tickDelta - 1) / this.maxAge);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();
        drawSphere(stack, tess, buffer, sphereRadius, latStep, longStep, true, red, green, blue, alpha);
        drawSphere(stack, tess, buffer, sphereRadius, latStep, longStep, false, red, green, blue, alpha);

        RenderSystem.disableCull();
        RenderSystem.disableBlend();

        stack.push();
    }

    private static void drawSphere(MatrixStack stack, Tessellator tessellator, BufferBuilder buffer, float radius, float latStep, float longStep, boolean inside, float r, float g, float b, float a) {
        buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

        boolean goingUp = inside;

        buffer.vertex(stack.peek().getPositionMatrix(), 0, goingUp ? -radius : radius, 0).color(r, g, b, a).next();

        for (float longitude = -(float) Math.PI; longitude <= (float) Math.PI; longitude += longStep) {
            for (float theta = (float) Math.PI / 2 - latStep; theta >= -(float) Math.PI / 2 + latStep; theta -= latStep) {
                float latitude = goingUp ? -theta : theta;

                float hRadius = radius * MathHelper.cos(latitude);
                float vy = radius * MathHelper.sin(latitude);
                float vx = hRadius * MathHelper.sin(longitude);
                float vz = hRadius * MathHelper.cos(longitude);

                buffer.vertex(stack.peek().getPositionMatrix(), vx, vy, vz).color(r, g, b, a).next();

                vx = hRadius * MathHelper.sin(longitude + longStep);
                vz = hRadius * MathHelper.cos(longitude + longStep);

                buffer.vertex(stack.peek().getPositionMatrix(), vx, vy, vz).color(r, g, b, a).next();
            }

            buffer.vertex(stack.peek().getPositionMatrix(), 0, goingUp ? radius : -radius, 0).color(r, g, b, a).next();

            goingUp = !goingUp;
        }

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    public static class SphereFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public SphereFactory(SpriteProvider sprite) {
            spriteProvider = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleSphere(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld clientWorld, Vec3d vec3d) {
            return new ParticleSphere(clientWorld, vec3d.x, vec3d.y, vec3d.z, spriteProvider);
        }
    }
}
