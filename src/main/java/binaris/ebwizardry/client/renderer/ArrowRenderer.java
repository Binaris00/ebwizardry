package binaris.ebwizardry.client.renderer;

import binaris.ebwizardry.Wizardry;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class ArrowRenderer extends EntityRenderer {
    public ArrowRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return new Identifier(Wizardry.MODID, "textures/entity/magic_missile.png");
    }
}
