package binaris.ebwizardry.client.renderer;

import binaris.ebwizardry.entity.projectile.EntityMagicProjectile;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class MagicProjectileRenderer<T extends EntityMagicProjectile> extends EntityRenderer<T> {
    private float scale = 0.7f;
    private boolean blend = false;
    private Identifier texture;

    public MagicProjectileRenderer(EntityRendererFactory.Context context, Identifier texture){
        super(context);
        this.texture = texture;
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderTexture(0, this.getTexture(entity));
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        }

        float f2 = this.scale;
        matrices.scale(f2, f2, f2);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        float f3 = 0.0f;
        float f4 = 1.0f;
        float f5 = 0.0f;
        float f6 = 1.0f;
        float f7 = 1.0F;
        float f8 = 0.5F;
        float f9 = 0.25F;


        matrices.multiply(this.dispatcher.getRotation());
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(matrices.peek().getPositionMatrix(), (0.0F - f8), (0.0F - f9), 0.0F).texture(f3, f6).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), (f7 - f8), (0.0F - f9), 0.0F).texture(f4, f6).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), (f7 - f8), (1.0F - f9), 0.0F).texture(f4, f5).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), (0.0F - f8), (1.0F - f9), 0.0F).texture(f3, f5).next();
        tessellator.draw();

        if (blend) {
            RenderSystem.disableBlend();
        }
        matrices.pop();
    }

    @Override
    public Identifier getTexture(T entity) {
        return texture;
    }
}
