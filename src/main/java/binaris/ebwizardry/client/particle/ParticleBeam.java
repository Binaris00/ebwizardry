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
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ParticleBeam extends ParticleTargeted{
    private static final float THICKNESS = 0.1f;

    public ParticleBeam(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, false);
        this.setColor(1, 1, 1);
        this.setMaxAge(0);
        this.scale = 1;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

    @Override
    protected void draw(MatrixStack stack, Tessellator tessellator, float length, float tickDelta) {
        float scale = this.scale;

        if (this.maxAge > 0) {
            float ageFraction = (age + tickDelta - 1) / maxAge;
            scale = this.scale * (1 - ageFraction * ageFraction);
        }

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);

        for (int layer = 0; layer < 3; layer++) {
            drawSegment(stack, tessellator, layer, 0, 0, 0, 0, 0, length, THICKNESS * scale);
        }

        RenderSystem.disableBlend();
    }

    private void drawSegment(MatrixStack stack, Tessellator tessellator, int layer, float x1, float y1, float z1, float x2, float y2, float z2, float thickness) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

        switch (layer) {
            case 0:
                drawShearedBox(stack, buffer, x1, y1, z1, x2, y2, z2, 0.25f * thickness, 1, 1, 1, 1);
                break;

            case 1:
                drawShearedBox(stack, buffer, x1, y1, z1, x2, y2, z2, 0.6f * thickness, (red + 1) / 2, (green + 1) / 2, (blue + 1) / 2, 0.65f);
                break;

            case 2:
                drawShearedBox(stack, buffer, x1, y1, z1, x2, y2, z2, thickness, red, green, blue, 0.3f);
                break;
        }

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    private void drawShearedBox(MatrixStack stack, BufferBuilder buffer, float x1, float y1, float z1, float x2, float y2, float z2, float width, float r, float g, float b, float a) {
        buffer.vertex(stack.peek().getPositionMatrix(), x1 - width, y1 - width, z1).color(r, g, b, a).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x2 - width, y2 - width, z2).color(r, g, b, a).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x1 - width, y1 + width, z1).color(r, g, b, a).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x2 - width, y2 + width, z2).color(r, g, b, a).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x1 + width, y1 + width, z1).color(r, g, b, a).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x2 + width, y2 + width, z2).color(r, g, b, a).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x1 + width, y1 - width, z1).color(r, g, b, a).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x2 + width, y2 - width, z2).color(r, g, b, a).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x1 - width, y1 - width, z1).color(r, g, b, a).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x2 - width, y2 - width, z2).color(r, g, b, a).next();
    }

    @Deprecated
    public static class BeamFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public BeamFactory(SpriteProvider sprite) {
            spriteProvider = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleBeam(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld clientWorld, Vec3d vec3d) {
            return new ParticleBeam(clientWorld, vec3d.x, vec3d.y, vec3d.z, spriteProvider);
        }
    }
}
