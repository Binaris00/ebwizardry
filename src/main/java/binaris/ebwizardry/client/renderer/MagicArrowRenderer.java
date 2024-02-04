package binaris.ebwizardry.client.renderer;

import binaris.ebwizardry.entity.projectile.EntityMagicArrow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

/**
 * This class, MagicArrowRenderer, extends the ProjectileEntityRenderer class.
 * It is used to render the EntityMagicArrow in the game, see {@link EntityMagicArrow} and {@link EntityMagicArrow#getTexture()}.
 *
 * @param <T> This represents any object that extends the EntityMagicArrow class.
 */
@Environment(value = EnvType.CLIENT)
public class MagicArrowRenderer<T extends EntityMagicArrow> extends ProjectileEntityRenderer<T> {
    /**
     * This is the constructor for the MagicArrowRenderer class.
     * It calls the constructor of the superclass, ProjectileEntityRenderer.
     * <p>
     * If you don't understand what this does, don't touch it ^^
     * @param ctx context of the EntityRendererFactory.
     */
    public MagicArrowRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    /**
     * This method overrides the getTexture method in the superclass.
     * It returns the texture of the EntityMagicArrow.
     *
     * @param entity This is the EntityMagicArrow object.
     * @return Identifier object representing the texture of the EntityMagicArrow.
     */
    @Override
    public Identifier getTexture(T entity) {
        return entity.getTexture();
    }
}
