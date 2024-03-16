package binaris.ebwizardry.client.renderer;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.entity.construct.EntityIceSpike;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class IceSpikeRenderer extends EntityRenderer<EntityIceSpike> {
    private static final Identifier texture = new Identifier(Wizardry.MODID, "textures/entity/ice_spike.png");

    public IceSpikeRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EntityIceSpike entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        RenderSystem.enableDepthTest();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getYaw() - 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(entity.getPitch() - 90));

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(matrices.peek().getPositionMatrix(), 0, 0, 0.5f).texture(1, 1).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 0, 1, 0.5f).texture(1, 0).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 0, 1, -0.5f).texture(0, 0).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 0, 0, -0.5f).texture(0, 1).next();
        tessellator.draw();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(matrices.peek().getPositionMatrix(), 0.5f, 0, 0).texture(1, 1).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 0.5f, 1, 0).texture(1, 0).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), -0.5f, 1, 0).texture(0, 0).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), -0.5f, 0, 0).texture(0, 1).next();
        tessellator.draw();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(matrices.peek().getPositionMatrix(), 0, 0, -0.5f).texture(0, 1).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 0, 1, -0.5f).texture(0, 0).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 0, 1, 0.5f).texture(1, 0).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 0, 0, 0.5f).texture(1, 1).next();
        tessellator.draw();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(matrices.peek().getPositionMatrix(), -0.5f, 0, 0).texture(1, 1).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), -0.5f, 1, 0).texture(1, 0).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 0.5f, 1, 0).texture(0, 0).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 0.5f, 0, 0).texture(0, 1).next();
        tessellator.draw();

        matrices.pop();
    }

    @Override
    public Identifier getTexture(EntityIceSpike entity) {
        return texture;
    }
}
