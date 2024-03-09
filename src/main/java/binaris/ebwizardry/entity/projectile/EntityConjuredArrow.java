package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.WizardryEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityConjuredArrow extends PersistentProjectileEntity {

    public EntityConjuredArrow(World world, PlayerEntity player) {
        super(WizardryEntities.CONJURED_ARROW, player, world);
    }

    public EntityConjuredArrow(World world, double x, double y, double z) {
        super(WizardryEntities.CONJURED_ARROW, x, y, z, world);
    }

    public EntityConjuredArrow(EntityType<EntityConjuredArrow> entityConjuredArrowEntityType, World world) {
        super(entityConjuredArrowEntityType, world);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 400) {
            this.discard();
        }
        super.tick();
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }
}
