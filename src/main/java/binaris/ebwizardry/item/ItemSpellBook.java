package binaris.ebwizardry.item;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.constant.Tier;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.SpellUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ItemSpellBook extends Item {
    /**
     * Used to determine which texture to use for the spell book GUI. This is a map because the texture depends on the
     * tier of the spell book, which is not known at compile time.
     *
     * @deprecated because I'm not sure if this will be used in the future.
     */
    @Deprecated
    private static final Map<Tier, Identifier> guiTextures = ImmutableMap.of(
            Tier.NOVICE, new Identifier(Wizardry.MODID, "textures/gui/spell_book_novice.png"),
            Tier.APPRENTICE, new Identifier(Wizardry.MODID, "textures/gui/spell_book_apprentice.png"),
            Tier.ADVANCED, new Identifier(Wizardry.MODID, "textures/gui/spell_book_advanced.png"),
            Tier.MASTER, new Identifier(Wizardry.MODID, "textures/gui/spell_book_master.png"));

    public ItemSpellBook(Settings settings) {
        super(settings);
    }


    // TODO: System to make a spell book item from a spell.


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        // TODO: Open spell book GUI.

        return TypedActionResult.success(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Spell spell = SpellUtils.getSpell(stack);

        // TODO: Tooltip for discovering system.
        if(spell == null) return;

        tooltip.add(spell.getNameWithFormatting());
        tooltip.add(spell.getProperties().getTier().getNameForTranslationFormatted());

        super.appendTooltip(stack, world, tooltip, context);
    }
}