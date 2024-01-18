package binaris.ebwizardry.util;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.item.ItemSpellBook;
import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.spell.Spell;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public final class SpellUtils {

    public static String SPELL_KEY = "Spell";


    public static ItemStack setSpell(ItemStack stack, Spell spell) {
        Identifier identifier = Wizardry.REGISTRIES_SPELL.getId(spell);
        stack.getOrCreateNbt().putString(SPELL_KEY, identifier.toString());

        return stack;
    }


    public static Spell getSpell(ItemStack stack) {
        return getSpell(stack.getNbt());
    }

    public static Spell getSpell(@Nullable NbtCompound compound) {
        return compound == null ? Spells.NONE : ItemSpellBook.byId(compound.getString(SPELL_KEY));
    }
}
