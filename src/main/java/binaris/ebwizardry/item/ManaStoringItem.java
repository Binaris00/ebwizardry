package binaris.ebwizardry.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ManaStoringItem {

    /** Returns the amount of mana contained in the given item stack. */
    int getMana(ItemStack stack);

    /** Sets the amount of mana contained in the given item stack to the given value. This method does not perform any
     * checks for creative mode, etc. */
    void setMana(ItemStack stack, int mana);

    /** Returns the maximum amount of mana that the given item stack can hold. */
    int getManaCapacity(ItemStack stack);

    /**
     * Returns whether this item's mana should be displayed in the arcane workbench tooltip. Only called client-side.
     * @param player The player using the workbench.
     * @param stack The itemstack to query.
     * @return True if the mana should be shown, false if not. Returns true by default.
     */
    default boolean showManaInWorkbench(PlayerEntity player, ItemStack stack){
        return true;
    }

    /** Convenience method that decreases the amount of mana contained in the given item stack by the given value. This
     * method automatically limits the mana to a minimum of 0 and performs the relevant checks for creative mode, etc. */
    default void consumeMana(ItemStack stack, int mana, @Nullable LivingEntity wielder){
        if(wielder instanceof PlayerEntity && ((PlayerEntity)wielder).isCreative()) return; // Mana isn't consumed in creative
        setMana(stack, Math.max(getMana(stack) - mana, 0));
    }

    /** Convenience method that increases the amount of mana contained in the given item stack by the given value.
     * This method automatically limits the mana to within the item's capacity. */
    // We don't really need to limit this one because Item#setDamage() ultimately limits it anyway, but we may as well
    default void rechargeMana(ItemStack stack, int mana){
        setMana(stack, Math.min(getMana(stack) + mana, getManaCapacity(stack)));
    }

    /** Convenience method that returns true if the given stack contains the maximum amount of mana, false otherwise. */
    default boolean isManaFull(ItemStack stack){
        return getMana(stack) == getManaCapacity(stack);
    }

    /** Convenience method that returns true if the given stack contains no mana, false otherwise. */
    default boolean isManaEmpty(ItemStack stack){
        return getMana(stack) == 0;
    }

    /** Returns how full the given stack's mana is, as a fraction between 0 (empty) and 1 (full) */
    default float getFullness(ItemStack stack){
        return (float)getMana(stack) / getManaCapacity(stack);
    }
}
