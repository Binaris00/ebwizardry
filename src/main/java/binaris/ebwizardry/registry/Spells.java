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
    public static Spell FIRE_BOMB;
    public static Spell POISON_BOMB;
    public static Spell SMOKE_BOMB;


    public static void registry(){
        NONE = registrySpell("none", new None().createProperties(Tier.NOVICE, Element.MAGIC, SpellType.UTILITY, 0, 0, 0));
        SPARK_BOMB = registrySpell("spark_bomb" , new Spell("spark_bomb", UseAction.NONE, false).createProperties(Tier.APPRENTICE, Element.LIGHTNING, SpellType.PROJECTILE, 15, 0, 25)
                // Temporal properties for testing
                .addProperties(Spell.DIRECT_DAMAGE, 6)
                .addProperties(Spell.EFFECT_RADIUS, 5)
                .addProperties(Spell.SPLASH_DAMAGE, 4));
        FIRE_BOMB = registrySpell("fire_bomb", new Spell("fire_bomb", UseAction.NONE, false).createProperties(Tier.APPRENTICE, Element.FIRE, SpellType.PROJECTILE, 15, 0, 25)
                // Temporal properties for testing
                .addProperties(Spell.RANGE, 10)
                .addProperties(Spell.DIRECT_DAMAGE, 5)
                .addProperties(Spell.SPLASH_DAMAGE, 3)
                .addProperties(Spell.BLAST_RADIUS, 3)
                .addProperties(Spell.BURN_DURATION, 7));
        POISON_BOMB = registrySpell("poison_bomb", new Spell("poison_bomb", UseAction.NONE, false).createProperties(Tier.APPRENTICE, Element.EARTH, SpellType.PROJECTILE, 15, 0, 25)
                // Temporal properties for testing
                .addProperties(Spell.RANGE, 10)
                .addProperties(Spell.DIRECT_DAMAGE, 5)
                .addProperties(Spell.SPLASH_DAMAGE, 3)
                .addProperties(Spell.EFFECT_RADIUS, 3)
                .addProperties(Spell.DIRECT_EFFECT_DURATION, 120)
                .addProperties(Spell.DIRECT_EFFECT_STRENGTH, 1)
                .addProperties(Spell.SPLASH_EFFECT_DURATION, 100)
                .addProperties(Spell.SPLASH_EFFECT_STRENGTH, 1));
        SMOKE_BOMB = registrySpell("smoke_bomb", new Spell("smoke_bomb", UseAction.NONE, false).createProperties(Tier.APPRENTICE, Element.FIRE, SpellType.PROJECTILE, 15, 0, 25)
                .addProperties(Spell.RANGE, 10)
                .addProperties(Spell.EFFECT_RADIUS, 3)
                .addProperties(Spell.EFFECT_DURATION, 120));
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
