package binaris.ebwizardry.util;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.spell.Spell;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * This class provides utility methods for handling spells in the game.
 */
public final class SpellUtils {
    public static String SPELL_KEY = "Spell";

    /**
     * Sets a spell to the given ItemStack.
     *
     * @param stack The ItemStack to which the spell is to be set.
     * @param spell The spell to be set to the ItemStack.
     * @return The ItemStack with the spell set.
     */
    public static ItemStack setSpell(ItemStack stack, Spell spell) {
        stack.getOrCreateNbt().putString(SPELL_KEY, Wizardry.REGISTRIES_SPELL.getId(spell).toString());
        return stack;
    }

    /**
     * Retrieves the spell from the given ItemStack.
     *
     * @param stack The ItemStack from which the spell is to be retrieved.
     * @return The spell retrieved from the ItemStack.
     */
    public static Spell getSpell(ItemStack stack) {
        return getSpellFromNbt(stack.getNbt());
    }

    /**
     * Retrieves the spell from the given NbtCompound.
     *
     * @param compound The NbtCompound from which the spell is to be retrieved.
     * @return The spell retrieved from the NbtCompound. If the compound is null, returns Spells.NONE.
     */
    private static Spell getSpellFromNbt(@Nullable NbtCompound compound) {
        return compound == null ? Spells.NONE : byId(compound.getString(SPELL_KEY));
    }

    // -------------------------------- NBT --------------------------------
    // All the methods below are used to store the spell in the item's NBT.
    // ---------------------------------------------------------------------

    /**
     * Gets a spell from the given id.
     * @param id The id of the spell.
     * @return The spell with the given id.
     * */
    private static Spell byId(String id) {
        return Wizardry.REGISTRIES_SPELL.get(Identifier.tryParse(id));
    }
}
