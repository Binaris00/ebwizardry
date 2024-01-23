package binaris.ebwizardry.item;

import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public interface SpellCastingItem {

    /**
     * Returns the spell currently equipped on the given itemstack. The given itemstack will be of this item.
     * @param stack The itemstack to query.
     * @return The currently equipped spell, or {@link binaris.ebwizardry.registry.Spells#NONE} if no spell
     * is equipped.
     */
    @NotNull
    Spell getCurrentSpell(ItemStack stack);

    /**
     * Returns the spell equipped in the next slot on the given itemstack. The given itemstack will be of this item.
     * @param stack The itemstack to query.
     * @return The next spell, or {@link binaris.ebwizardry.registry.Spells#NONE} if no spell is equipped
     * in the next slot. Returns the current spell by default (useful for items with only one spell).
     */
    @NotNull
    default Spell getNextSpell(ItemStack stack){
        return getCurrentSpell(stack);
    }

    /**
     * Returns the spell equipped in the previous slot on the given itemStack. The given itemStack will be of this item.
     * @param stack The itemStack to query.
     * @return The previous spell, or {@link binaris.ebwizardry.registry.Spells#NONE} if no spell is
     * equipped in the previous slot. Returns the current spell by default (useful for items with only one spell).
     */
    @NotNull
    default Spell getPreviousSpell(ItemStack stack){
        return getCurrentSpell(stack);
    }

    /**
     * Return all the spells currently bound to the given itemstack. The given itemstack will be of this item.
     * @param stack The itemstack to query.
     * @return The bound spells, or {@link binaris.ebwizardry.registry.Spells#NONE} if no spell
     * is equipped.
     */
    default Spell[] getSpells(ItemStack stack){
        return new Spell[]{getCurrentSpell(stack)}; // Default implementation for single-spell items, because I'm lazy
    }

    /**
     * Selects the next spell bound to the given itemstack. The given itemstack will be of this item.
     * @param stack The itemstack to query.
     */
    default void selectNextSpell(ItemStack stack){
        // If it doesn't need spell-switching, then don't bother the implementor with it
        // Like the scrolls, for example
    }

    /**
     * Selects the previous spell bound to the given itemstack. The given itemstack will be of this item.
     * @param stack The itemstack to query.
     */
    default void selectPreviousSpell(ItemStack stack){
        // Nothing here either
    }

    /**
     * Selects the spell at the given index bound to the given itemStack. The given itemStack will be of this item.
     * @param stack The itemStack to query.
     * @param index The index to set.
     * @return True if the operation succeeded, false if not.
     */
    default boolean selectSpell(ItemStack stack, int index){
        return false;
    }

    /**
     * Returns whether the spell HUD should be shown when a player is holding this item. Only called client-side.
     * @param player The player holding the item.
     * @param stack The itemStack to query.
     * @return True if the spell HUD should be shown, false if not.
     */
    boolean showSpellHUD(PlayerEntity player, ItemStack stack);

    /**
     * Returns the current cooldown to display on the spell HUD for the given itemStack.
     * @param stack The itemStack to query.
     * @return The current cooldown for the equipped spell.
     */
    default int getCurrentCooldown(ItemStack stack){
        return 0;
    }

    /**
     * Returns the max cooldown of the current spell to display on the spell HUD for the given itemStack.
     * @param stack The itemStack to query.
     * @return The max cooldown for the equipped spell.
     */
    default int getCurrentMaxCooldown(ItemStack stack){
        return 0;
    }

    /**
     * Returns whether this item's spells should be displayed in the arcane workbench tooltip. Only called client-side.
     * Ignore this method if this item is not an {@link WorkbenchItem}.
     * @param player The player using the workbench.
     * @param stack The itemStack to query.
     * @return True if the spells should be shown, false if not. Returns true by default.
     */
    default boolean showSpellsInWorkbench(PlayerEntity player, ItemStack stack){
        return true;
    }

    // =============================== Spell Casting ===============================
    // These methods were made with the intention of standardising the code for casting spells using items.
    // For most external uses, there's no reason for them to be separate, however, it makes more sense to do so because
    // then we can eliminate a bit of duplicate code from continuous vs. non-continuous spell casting. Otherwise, we'd
    // need a separate method for casting continuous spells anyway.
    // =============================================================================

    /**
     * Returns whether the given spell can be cast by the given stack in its current state. Does not perform any actual
     * spell casting.
     *
     * @param stack The stack being queried; will be of this item.
     * @param spell The spell to be cast.
     * @param caster The player doing the casting.
     * @param hand The hand in which the casting item is being held.
     * @param castingTick For continuous spells, the number of ticks the spell has already been cast for. For all other
     *                    spells, this will be zero.
     * @param modifiers The modifiers with which the spell is being cast.
     * @return True if the spell can be cast, false if not.
     */
    boolean canCast(ItemStack stack, Spell spell, PlayerEntity caster, Hand hand, int castingTick, SpellModifiers modifiers);

    /**
     * Casts the given spell using the given item stack. <b>This method does not perform any checks</b>; These are done
     * in {@link SpellCastingItem#canCast(ItemStack, Spell, PlayerEntity, Hand, int, SpellModifiers)}. This method
     * also performs any post-casting logic, such as mana costs and cooldowns. This method does not handle charge-up
     * times.
     * <p></p>
     * <i>N.B. Continuous spell casting from an outside of the items requires a bit of extra legwork.
     * @param stack The stack being queried; will be of this item.
     * @param spell The spell to be cast.
     * @param caster The player doing the casting.
     * @param hand The hand in which the casting item is being held.
     * @param castingTick For continuous spells, the number of ticks the spell has already been cast for. For all other
     *                    spells, this will be zero.
     * @param modifiers The modifiers with which the spell is being cast.
     * @return True if the spell succeeded, false if not. This is only really for the purpose of returning a result from
     * {@link net.minecraft.item.Item#use(World, PlayerEntity, Hand)} and similar methods; mana costs,
     * cooldowns and whatever else you might want to do post-spell casting should be done within this method so that
     * external sources don't allow spells to be cast for free, for example.
     */
    boolean cast(ItemStack stack, Spell spell, PlayerEntity caster, Hand hand, int castingTick, SpellModifiers modifiers);
}
