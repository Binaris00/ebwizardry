package binaris.ebwizardry.item;

import binaris.ebwizardry.WizardryClient;
import binaris.ebwizardry.entity.projectile.EntityConjuredArrow;
import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.util.InventoryUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemSpectralBow extends BowItem implements IConjuredItem {
    public ItemSpectralBow() {
        super(new FabricItemSettings().rarity(Rarity.EPIC).maxDamage(1200));
        WizardryClient.addConjuredItem(this);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return this.getMaxDamageFromNBT(stack, Spells.CONJURE_BOW);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return IConjuredItem.getTimerBarColour(stack);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.success(itemStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if(!world.isClient) stack.setDamage(stack.getDamage() + (this.getMaxUseTime(stack) - remainingUseTicks));

        if(user instanceof PlayerEntity player){
            int charge = this.getMaxUseTime(stack) - remainingUseTicks;
            if (charge < 0) return;

            float velocity = getPullProgress(charge);

            if (!((double) velocity < 0.1D)) {
                if (!world.isClient) {
                    EntityConjuredArrow arrow = new EntityConjuredArrow(world, player);
                    arrow.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, velocity * 3.0F, 1.0F);
                    world.spawnEntity(arrow);
                }
            }
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) * 0.5f);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        int damage = stack.getDamage();
        if (damage > stack.getMaxDamage()) InventoryUtils.replaceItemInInventory(entity, slot, stack, ItemStack.EMPTY);
        stack.setDamage(damage + 1);

        super.inventoryTick(stack, world, entity, slot, selected);
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
