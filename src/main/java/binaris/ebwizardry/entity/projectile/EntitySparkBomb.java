package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.registry.WizardrySounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class EntitySparkBomb extends EntityBomb{
    public EntitySparkBomb(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntitySparkBomb(LivingEntity livingEntity, World world) {
        super(WizardryEntities.entitySparkBomb, livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {
        return WizardryItems.SPARK_BOMB;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.playSound(WizardrySounds.ENTITY_SPARK_BOMB_HIT_BLOCK, 0.5f, 0.5f);
        super.onBlockHit(blockHitResult);
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        // Direct hit damage
        this.playSound(WizardrySounds.ENTITY_SPARK_BOMB_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));


        super.onEntityHit(entityHitResult);
    }
}
