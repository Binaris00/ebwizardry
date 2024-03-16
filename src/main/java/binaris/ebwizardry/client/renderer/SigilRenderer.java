package binaris.ebwizardry.client.renderer;

import binaris.ebwizardry.entity.construct.EntityMagicConstruct;
import binaris.ebwizardry.util.DrawingUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
public class SigilRenderer extends EntityRenderer<EntityMagicConstruct> {
    private final Identifier texture;
    private float rotationSpeed;
    private boolean invisibleToEnemies;
    public SigilRenderer(EntityRendererFactory.Context ctx, Identifier texture, float rotationSpeed, boolean invisibleToEnemies) {
        super(ctx);
        this.texture = texture;
        this.rotationSpeed = rotationSpeed;
        this.invisibleToEnemies = invisibleToEnemies;
    }

    @Override
    public void render(EntityMagicConstruct entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        matrices.push();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        float yOffset = 0;

        matrices.translate(0, yOffset, 0);

        RenderSystem.setShaderTexture(0, texture);
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.5F;

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));

        if (rotationSpeed != 0) matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(entity.age * rotationSpeed));

        float s = entity.getWidth() * DrawingUtils.smoothScaleFactor(entity.lifetime, entity.age, tickDelta, 10, 10);
        matrices.scale(s, s, s);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        buffer.vertex(matrices.peek().getPositionMatrix(), 0.0F - f7, 0.0F - f8, 0.01f).texture(0, 1).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), f6 - f7, 0.0F - f8, 0.01f).texture(1, 1).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), f6 - f7, 1.0F - f8, 0.01f).texture(1, 0).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 0.0F - f7, 1.0F - f8, 0.01f).texture(0, 0).next();
        tessellator.draw();

        RenderSystem.disableBlend();
        matrices.pop();
    }

    @Override
    public Identifier getTexture(EntityMagicConstruct entity) {
        return texture;
    }
}
