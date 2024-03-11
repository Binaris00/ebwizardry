package binaris.ebwizardry.client.particle;

import binaris.ebwizardry.Wizardry;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class ParticleBuff extends ParticleWizardry{
    private static final Identifier TEXTURE = new Identifier(Wizardry.MODID, "textures/particle/buff.png");
    private final boolean mirror;


    public ParticleBuff(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, false);
        this.setVelocity(0, 0.162, 0);
        this.mirror = random.nextBoolean();
        this.setMaxAge(15);
        this.setGravity(false);
        this.collidesWithWorld = false;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        MatrixStack stack = RenderSystem.getModelViewStack();
        updateEntityLinking(camera.getFocusedEntity(), tickDelta);

        stack.push();

        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);


        stack.translate((this.age + tickDelta) / (float) this.maxAge * -2, 0, 0);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderTexture(0, TEXTURE);

        buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_TEXTURE_COLOR);

        float x = (float) (this.prevPosX + (this.x - this.prevPosX) * (double) tickDelta);
        float y = (float) (this.prevPosY + (this.y - this.prevPosY) * (double) tickDelta);
        float z = (float) (this.prevPosZ + (this.z - this.prevPosZ) * (double) tickDelta);

        float f = 0.875f - 0.125f * MathHelper.floor((float) this.age / (float) this.maxAge * 8 - 0.000001f);
        float g = f + 0.125f;
        float hrepeat = 1;
        float scale = 0.6f;
        float yScale = 0.7f * scale;
        float dx = mirror ? -scale : scale;

        buffer.vertex(stack.peek().getPositionMatrix(), x - dx, y - yScale, z - scale).texture(0, g).color(red, green, blue, alpha).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x - dx, y + yScale, z - scale).texture(0, f).color(red, green, blue, alpha).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x + dx, y - yScale, z - scale).texture(0.25F * hrepeat, g).color(red, green, blue, alpha).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x + dx, y + yScale, z - scale).texture(0.25F * hrepeat, f).color(red, green, blue, alpha).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x + dx, y - yScale, z + scale).texture(0.5F * hrepeat, g).color(red, green, blue, alpha).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x + dx, y + yScale, z + scale).texture(0.5F * hrepeat, f).color(red, green, blue, alpha).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x - dx, y - yScale, z + scale).texture(0.75F * hrepeat, g).color(red, green, blue, alpha).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x - dx, y + yScale, z + scale).texture(0.75F * hrepeat, f).color(red, green, blue, alpha).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x - dx, y - yScale, z - scale).texture(hrepeat, g).color(red, green, blue, alpha).next();
        buffer.vertex(stack.peek().getPositionMatrix(), x - dx, y + yScale, z - scale).texture(hrepeat, f).color(red, green, blue, alpha).next();

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.disableBlend();
        RenderSystem.enableCull();

        stack.pop();
    }

    @Deprecated
    public static class BuffFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public BuffFactory(SpriteProvider sprite) {
            spriteProvider = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleBuff(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld world, double x, double y, double z) {
            return new ParticleBuff(world, x, y, z, spriteProvider);
        }
    }
}
