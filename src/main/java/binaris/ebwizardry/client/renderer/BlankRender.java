package binaris.ebwizardry.client.renderer;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class BlankRender extends EntityRenderer {
    public BlankRender(EntityRendererFactory.Context ctx) {
        super(ctx);
    }
    @Override
    public Identifier getTexture(Entity entity) {
        return null;
    }
}
