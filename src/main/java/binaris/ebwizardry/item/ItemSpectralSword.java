package binaris.ebwizardry.item;

import binaris.ebwizardry.WizardryClient;
import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.util.InventoryUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

public class ItemSpectralSword extends SwordItem implements IConjuredItem {
    public ItemSpectralSword() {
        super(ToolMaterials.IRON, 3, -2.4F, new FabricItemSettings().maxDamage(1200).rarity(Rarity.EPIC));
        WizardryClient.addConjuredItem(this);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return this.getMaxDamageFromNBT(stack, Spells.CONJURE_SWORD);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return IConjuredItem.getTimerBarColour(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        int damage = stack.getDamage();
        if (damage > stack.getMaxDamage()) InventoryUtils.replaceItemInInventory(entity, slot, stack, ItemStack.EMPTY);
        stack.setDamage(damage + 1);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }
    @Override
    public int getEnchantability() {
        return 0;
    }
    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }



}
