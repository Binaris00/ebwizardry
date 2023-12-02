package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.constant.Element;
import binaris.ebwizardry.constant.SpellType;
import binaris.ebwizardry.constant.Tier;
import binaris.ebwizardry.spell.None;
import binaris.ebwizardry.spell.Spell;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;

import java.util.List;

import static binaris.ebwizardry.Wizardry.REGISTRIES_SPELL;

public abstract class Spells {

    public static Spell NONE;
    public static Spell SPARK_BOMB;
    public static Spell TEST;


    public static void registry(){
        NONE = registrySpell("none", new None().createProperties(Tier.NOVICE, Element.MAGIC, SpellType.UTILITY, 0, 0, 0));
        // Temporally spells for testing
        SPARK_BOMB = registrySpell("spark_bomb" , new Spell("spark_bomb", UseAction.NONE, false).createProperties(Tier.APPRENTICE, Element.LIGHTNING, SpellType.PROJECTILE, 15, 0, 25)
                .addProperties(Spell.DIRECT_DAMAGE, 10));

        TEST = registrySpell("test", new Spell("test", UseAction.NONE, false));
    }

    private static Spell registrySpell(String name, Spell spell){
        return Registry.register(REGISTRIES_SPELL, new Identifier(Wizardry.MODID, name), spell);
    }

    /**Return all the spells except for {@link None}*/
    public static List<Spell> getAllSpells(){
        List<Spell> temp = new java.util.ArrayList<>(REGISTRIES_SPELL.stream().toList());
        temp.remove(NONE);
        return temp;
    }
}
