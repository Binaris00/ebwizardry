package binaris.ebwizardry.effects;

import net.minecraft.entity.LivingEntity;

@Deprecated
public interface ISyncedPotion {
    double SYNC_RADIUS = 64;

    default boolean shouldSync(LivingEntity host) {
        return true;
    }
}
