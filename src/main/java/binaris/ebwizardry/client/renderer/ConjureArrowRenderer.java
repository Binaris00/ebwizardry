package binaris.ebwizardry.client.renderer;

import binaris.ebwizardry.entity.projectile.EntityConjuredArrow;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ConjureArrowRenderer extends ProjectileEntityRenderer<EntityConjuredArrow> {
    public ConjureArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(EntityConjuredArrow entity) {
        return new Identifier("ebwizardry", "textures/entity/conjured_arrow.png");
    }

    @Override
    public void render(EntityConjuredArrow persistentProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(persistentProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
