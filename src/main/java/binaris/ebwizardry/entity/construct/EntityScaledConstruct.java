package binaris.ebwizardry.entity.construct;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class EntityScaledConstruct extends EntityMagicConstruct{
    protected float sizeMultiplier = 1;
    protected EntityDimensions size = EntityDimensions.changing(this.getWidth(), this.getHeight());
    public EntityScaledConstruct(EntityType<?> type, World world) {
        super(type, world);
        this.calculateDimensions();
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return size;
    }

    public float getSizeMultiplier() {
        return sizeMultiplier;
    }

    public void setSizeMultiplier(float sizeMultiplier) {
        this.sizeMultiplier = sizeMultiplier;
        this.size = EntityDimensions.changing(shouldScaleWidth() ? getWidth() * sizeMultiplier : getWidth(), shouldScaleHeight() ? getHeight() * sizeMultiplier : getHeight());
    }

    protected boolean shouldScaleWidth() {
        return true;
    }

    protected boolean shouldScaleHeight() {
        return true;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setSizeMultiplier(nbt.getFloat("sizeMultiplier"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("sizeMultiplier", sizeMultiplier);
    }
}
