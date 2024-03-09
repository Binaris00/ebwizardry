package binaris.ebwizardry.item;

import binaris.ebwizardry.WizardryClient;
import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.util.InventoryUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

public class ItemSpectralPickaxe extends PickaxeItem implements IConjuredItem {
    public ItemSpectralPickaxe() {
        super(ToolMaterials.IRON, 1, -2.8F, new FabricItemSettings().rarity(Rarity.EPIC).maxDamage(1200));
        WizardryClient.addConjuredItem(this);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return this.getMaxDamageFromNBT(stack, Spells.CONJURE_PICKAXE);
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
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        float speed = super.getMiningSpeedMultiplier(stack, state);
        return speed > 1 ? speed * IConjuredItem.getDamageMultiplier(stack) : speed;
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
