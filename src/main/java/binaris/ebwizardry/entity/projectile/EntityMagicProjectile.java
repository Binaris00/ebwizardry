package binaris.ebwizardry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public class EntityMagicProjectile extends ThrownItemEntity {
    public static final double LAUNCH_Y_OFFSET = 0.1;
    public static final int SEEKING_TIME = 15;

    public float damageMultiplier = 1.0f;

    public EntityMagicProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntityMagicProjectile(EntityType<? extends ThrownItemEntity> entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
    }



    @Override
    protected Item getDefaultItem() {
        return null;
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        damageMultiplier = nbt.getFloat("damageMultiplier");

    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putFloat("damageMultiplier", damageMultiplier);
        return super.writeNbt(nbt);

    }

    @Override
    public SoundCategory getSoundCategory() {
        //WizardrySounds
        return super.getSoundCategory();
    }


}
