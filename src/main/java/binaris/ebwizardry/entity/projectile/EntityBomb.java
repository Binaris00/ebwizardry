package binaris.ebwizardry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class EntityBomb extends EntityMagicProjectile{
    /** The entity blast multiplier. This is now synced and saved centrally from {@link EntityBomb}. */
    public float blastMultiplier = 1.0f;
    public EntityBomb(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntityBomb(EntityType<? extends ThrownItemEntity> entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
    }

    @Override
    public void readNbt(NbtCompound nbt){
        super.readNbt(nbt);
        blastMultiplier = nbt.getFloat("blastMultiplier");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt){
        nbt.putFloat("blastMultiplier", blastMultiplier);
        return super.writeNbt(nbt);
    }
}
