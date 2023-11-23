package binaris.ebwizardry.item;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.constant.Tier;
import binaris.ebwizardry.spell.Spell;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ItemSpellBook extends Item {
    private static final Map<Tier, Identifier> guiTextures = ImmutableMap.of(
            Tier.NOVICE, 		new Identifier(Wizardry.MODID, "textures/gui/spell_book_novice.png"),
            Tier.APPRENTICE, 	new Identifier(Wizardry.MODID, "textures/gui/spell_book_apprentice.png"),
            Tier.ADVANCED, 		new Identifier(Wizardry.MODID, "textures/gui/spell_book_advanced.png"),
            Tier.MASTER, 		new Identifier(Wizardry.MODID, "textures/gui/spell_book_master.png"));

    public ItemSpellBook(Settings settings) {
        super(settings);
    }





}
