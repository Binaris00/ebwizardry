package binaris.ebwizardry.client.renderer;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

/**
 * This class is used to render entities that don't have a texture,
 * such as {@link binaris.ebwizardry.registry.Spells#THUNDERBOLT}
 * */
public class BlankRender extends EntityRenderer<Entity> {
    public BlankRender(EntityRendererFactory.Context ctx) {
        super(ctx);
    }
    @Override
    public Identifier getTexture(Entity entity) {
        return null; // This is a blank texture
    }
}
