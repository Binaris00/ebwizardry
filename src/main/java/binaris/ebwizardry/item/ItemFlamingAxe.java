package binaris.ebwizardry.item;

import binaris.ebwizardry.WizardryClient;
import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.InventoryUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

public class ItemFlamingAxe extends AxeItem implements IConjuredItem{

    public ItemFlamingAxe() {
        super(WizardryItems.MAGICAL, 8, -3, new FabricItemSettings().maxDamage(1200).rarity(Rarity.COMMON));
        WizardryClient.addConjuredItem(this);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return this.getMaxDamageFromNBT(stack, Spells.FLAMING_AXE);
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
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(Spells.FLAMING_AXE.getIntProperty(Spell.BURN_DURATION));
        return false;
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
