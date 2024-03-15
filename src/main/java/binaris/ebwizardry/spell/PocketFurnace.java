package binaris.ebwizardry.spell;

import binaris.ebwizardry.util.InventoryUtils;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.Optional;

public class PocketFurnace extends Spell{
    public static final String ITEMS_SMELTED = "items_smelted";

    public PocketFurnace() {
            super("pocket_furnace", UseAction.NONE, false);
    }

    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers) {
        int usesLeft = (int) (getFloatProperty(ITEMS_SMELTED) * modifiers.get(SpellModifiers.POTENCY));

        ItemStack stack, result;

        for (int i = 0; i < caster.getInventory().size() && usesLeft > 0; i++) {
            stack = caster.getInventory().getStack(i);

            if (!stack.isEmpty() && !world.isClient) {

                Inventory dummyInv = new SimpleInventory(1);
                dummyInv.setStack(0, stack);
                Optional<RecipeEntry<SmeltingRecipe>> recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, dummyInv, world);

                if (recipe.isPresent()) {
                    DynamicRegistryManager registryManager =  world.getRegistryManager();
                    result = recipe.get().value().getResult(registryManager);


                    if (!result.isEmpty() && !(stack.getItem() instanceof ToolItem) && !(stack.getItem() instanceof ArmorItem)) {
                        // TODO: implement setting
                        // && !Settings.containsMetaItem(Wizardry.settings.pocketFurnaceItemBlacklist, stack)) {

                        if (stack.getCount() <= usesLeft) {
                            ItemStack stack2 = new ItemStack(result.getItem(), stack.getCount());
                            if (InventoryUtils.doesPlayerHaveItem(caster, result.getItem())) {
                                caster.giveItemStack(stack2);
                                caster.getInventory().setStack(i, ItemStack.EMPTY);
                            } else {
                                caster.getInventory().setStack(i, stack2);
                            }
                            usesLeft -= stack.getCount();
                        } else {
                            ItemStack copy = caster.getInventory().getStack(i).copy();
                            copy.decrement(usesLeft);
                            caster.getInventory().setStack(i, copy);
                            caster.getInventory().insertStack(new ItemStack(result.getItem(), usesLeft));
                            usesLeft = 0;
                        }
                    }
                }
            }
        }

        this.playSound(world, caster, ticksInUse, -1, modifiers);

        if (world.isClient) {
            for (int i = 0; i < 10; i++) {
                double x1 = (float) caster.getPos().x + world.random.nextFloat() * 2 - 1.0F;
                double y1 = (float) caster.getPos().y + caster.getStandingEyeHeight() - 0.5F + world.random.nextFloat();
                double z1 = (float) caster.getPos().z + world.random.nextFloat() * 2 - 1.0F;
                world.addParticle(ParticleTypes.FLAME, x1, y1, z1, 0, 0.01F, 0);
            }
        }

        return usesLeft < 5;
    }
}
