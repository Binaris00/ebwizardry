package binaris.ebwizardry.item;

import binaris.ebwizardry.WizardryClient;
import binaris.ebwizardry.entity.projectile.EntityFlamecatcherArrow;
import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Flamecatcher;
import binaris.ebwizardry.util.InventoryUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemFlamecatcher extends BowItem implements IConjuredItem {

    public ItemFlamecatcher() {
        super(new FabricItemSettings().maxDamage(1200).rarity(Rarity.EPIC));
        WizardryClient.addConjuredItem(this);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return this.getMaxDamageFromNBT(stack, Spells.FLAMECATCHER);
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
    public int getEnchantability() {
        return 0;
    }
    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if(!world.isClient) stack.setDamage(stack.getDamage() + (this.getMaxUseTime(stack) - remainingUseTicks));

        if(user instanceof PlayerEntity player){
            int charge = this.getMaxUseTime(stack) - remainingUseTicks;
            if (charge < 0) return;

            if (stack.getNbt() != null) {
                int shotsLeft = stack.getNbt().getInt(Flamecatcher.SHOTS_REMAINING_NBT_KEY) - 1;
                stack.getNbt().putInt(Flamecatcher.SHOTS_REMAINING_NBT_KEY, shotsLeft);
                if (shotsLeft == 0 && !world.isClient) {
                    stack.setDamage(getMaxDamage() - getAnimationFrames());
                }
            }

            float velocity = getPullProgress(charge);

            if (!((double) velocity < 0.1D)) {
                if (!world.isClient) {
                    EntityFlamecatcherArrow arrow = new EntityFlamecatcherArrow(world);
                    arrow.aim(player, EntityFlamecatcherArrow.SPEED * velocity);
                    world.spawnEntity(arrow);
                }

                world.playSound(null, player.getX(), player.getY(), player.getZ(), WizardrySounds.ITEM_FLAMECATCHER_SHOOT, WizardrySounds.SPELLS, 1, 1);

                world.playSound(null, player.getX(), player.getY(), player.getZ(), WizardrySounds.ITEM_FLAMECATCHER_FLAME, WizardrySounds.SPELLS, 1, 1);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.success(itemStack);
    }
}
