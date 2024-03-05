package binaris.ebwizardry.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class InventoryUtils {
    /**
     * Returns true if the given player has the given item in their inventory, including their main inventory, armour and
     * offhand slots.
     *
     * @param player The player to check
     * @param item   The item to check for
     * @return True if the player has the given item in their inventory
     */
    public static boolean doesPlayerHaveItem(PlayerEntity player, Item item) {
        for (ItemStack stack : player.getHandItems()) {
            if (stack.getItem() == item) {
                return true;
            }
        }

        for (ItemStack stack : player.getInventory().armor) {
            if (stack.getItem() == item) {
                return true;
            }
        }

        for (ItemStack stack : player.getInventory().main) {
            if (stack.getItem() == item) {
                return true;
            }
        }

        return false;
    }

    public static boolean replaceItemInInventory(Entity entity, int slot, ItemStack original, ItemStack replacement) {
        if (entity instanceof LivingEntity) {
            for (EquipmentSlot eslot : EquipmentSlot.values()) {
                if (((LivingEntity) entity).getEquippedStack(eslot) == original) {
                    entity.equipStack(eslot, replacement);
                    return true;
                }
            }
        }
        return entity.getStackReference(slot).set(replacement);
    }
}
