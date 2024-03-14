package binaris.ebwizardry.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

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
    /**
     * Returns a list of the itemstacks in the given player's hotbar and offhand, sorted into the following order: main
     * hand, offhand, rest of hotbar left-to-right. The returned list is a modifiable shallow copy of part of the player's
     * inventory stack list; as such, changes to the list are <b>not</b> written through to the player's inventory.
     * However, the ItemStack instances themselves are not copied, so changes to any of their fields (size, metadata...)
     * will change those in the player's inventory.
     */
    public static List<ItemStack> getPrioritisedHotBarAndOffhand(PlayerEntity player) {
        List<ItemStack> hotbar = getHotbar(player);
        hotbar.add(0, player.getOffHandStack());
        hotbar.remove(player.getMainHandStack());
        hotbar.add(0, player.getMainHandStack());
        return hotbar;
    }

    public static List<ItemStack> getHotbar(PlayerEntity player) {
        DefaultedList<ItemStack> hotBar = DefaultedList.of();
        hotBar.addAll(player.getInventory().main.subList(0, 9));
        return hotBar;
    }
}
