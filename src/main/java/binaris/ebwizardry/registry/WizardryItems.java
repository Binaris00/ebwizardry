package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.constant.Element;
import binaris.ebwizardry.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public abstract class WizardryItems {
    public static Item SPELL_BOOK;
    public static Item SCROLL;
    public static Item SPARK_BOMB;
    public static Item FIRE_BOMB;
    public static Item POISON_BOMB;
    public static Item SMOKE_BOMB;
    public static Item SILK;
    public static Item WIZARD_HAT;
    public static Item WIZARD_ROBE;
    public static Item WIZARD_LEGGINGS;
    public static Item WIZARD_BOOTS;
    public static Item WIZARD_HAT_FIRE;
    public static Item WIZARD_ROBE_FIRE;
    public static Item WIZARD_LEGGINGS_FIRE;
    public static Item WIZARD_BOOTS_FIRE;
    public static Item WIZARD_HAT_ICE;
    public static Item WIZARD_ROBE_ICE;
    public static Item WIZARD_LEGGINGS_ICE;
    public static Item WIZARD_BOOTS_ICE;
    public static Item WIZARD_HAT_LIGHTNING;
    public static Item WIZARD_ROBE_LIGHTNING;
    public static Item WIZARD_LEGGINGS_LIGHTNING;
    public static Item WIZARD_BOOTS_LIGHTNING;
    public static Item WIZARD_HAT_POISON;
    public static Item WIZARD_ROBE_POISON;
    public static Item WIZARD_LEGGINGS_POISON;
    public static Item WIZARD_BOOTS_POISON;
    public static Item WIZARD_HAT_EARTH;
    public static Item WIZARD_ROBE_EARTH;
    public static Item WIZARD_LEGGINGS_EARTH;
    public static Item WIZARD_BOOTS_EARTH;
    public static Item WIZARD_HAT_SORCERY;
    public static Item WIZARD_ROBE_SORCERY;
    public static Item WIZARD_LEGGINGS_SORCERY;
    public static Item WIZARD_BOOTS_SORCERY;
    public static Item WIZARD_HAT_HEALING;
    public static Item WIZARD_ROBE_HEALING;
    public static Item WIZARD_LEGGINGS_HEALING;
    public static Item WIZARD_BOOTS_HEALING;
    public static Item WIZARD_HAT_NECROMANCY;
    public static Item WIZARD_ROBE_NECROMANCY;
    public static Item WIZARD_LEGGINGS_NECROMANCY;
    public static Item WIZARD_BOOTS_NECROMANCY;



    public static void register(){
        // Normal items
        SPELL_BOOK = registerItem("spell_book", new ItemSpellBook(new FabricItemSettings().maxCount(16)));
        SPARK_BOMB = registerItem("spark_bomb", new ItemSparkBomb(new FabricItemSettings().maxCount(16)));
        FIRE_BOMB = registerItem("fire_bomb", new ItemFireBomb(new FabricItemSettings().maxCount(16)));
        POISON_BOMB = registerItem("poison_bomb", new ItemPoisonBomb(new FabricItemSettings().maxCount(16)));
        SMOKE_BOMB = registerItem("smoke_bomb", new ItemSmokeBomb(new FabricItemSettings().maxCount(16)));
        SCROLL = registerItem("scroll", new ItemScroll(new FabricItemSettings().maxCount(16)));
        SILK = registerItem("magic_silk", new Item(new FabricItemSettings()));

        // Armors
        WIZARD_HAT = registerItem("wizard_hat", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), null));
        WIZARD_ROBE = registerItem("wizard_robe", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), null));
        WIZARD_LEGGINGS = registerItem("wizard_leggings", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), null));
        WIZARD_BOOTS = registerItem("wizard_boots", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), null));

        WIZARD_HAT_FIRE = registerItem("wizard_hat_fire", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.FIRE));
        WIZARD_ROBE_FIRE = registerItem("wizard_robe_fire", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.FIRE));
        WIZARD_LEGGINGS_FIRE = registerItem("wizard_leggings_fire", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.FIRE));
        WIZARD_BOOTS_FIRE = registerItem("wizard_boots_fire", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.FIRE));

        WIZARD_HAT_ICE = registerItem("wizard_hat_ice", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.ICE));
        WIZARD_ROBE_ICE = registerItem("wizard_robe_ice", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.ICE));
        WIZARD_LEGGINGS_ICE = registerItem("wizard_leggings_ice", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.ICE));
        WIZARD_BOOTS_ICE = registerItem("wizard_boots_ice", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.ICE));

        WIZARD_HAT_LIGHTNING = registerItem("wizard_hat_lightning", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.LIGHTNING));
        WIZARD_ROBE_LIGHTNING = registerItem("wizard_robe_lightning", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.LIGHTNING));
        WIZARD_LEGGINGS_LIGHTNING = registerItem("wizard_leggings_lightning", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.LIGHTNING));
        WIZARD_BOOTS_LIGHTNING = registerItem("wizard_boots_lightning", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.LIGHTNING));

        WIZARD_HAT_POISON = registerItem("wizard_hat_poison", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.NECROMANCY));
        WIZARD_ROBE_POISON = registerItem("wizard_robe_poison", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.NECROMANCY));
        WIZARD_LEGGINGS_POISON = registerItem("wizard_leggings_poison", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.NECROMANCY));
        WIZARD_BOOTS_POISON = registerItem("wizard_boots_poison", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.NECROMANCY));

        WIZARD_HAT_EARTH = registerItem("wizard_hat_earth", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.EARTH));
        WIZARD_ROBE_EARTH = registerItem("wizard_robe_earth", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.EARTH));
        WIZARD_LEGGINGS_EARTH = registerItem("wizard_leggings_earth", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.EARTH));
        WIZARD_BOOTS_EARTH = registerItem("wizard_boots_earth", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.EARTH));

        WIZARD_HAT_SORCERY = registerItem("wizard_hat_sorcery", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.SORCERY));
        WIZARD_ROBE_SORCERY = registerItem("wizard_robe_sorcery", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.SORCERY));
        WIZARD_LEGGINGS_SORCERY = registerItem("wizard_leggings_sorcery", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.SORCERY));
        WIZARD_BOOTS_SORCERY = registerItem("wizard_boots_sorcery", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.SORCERY));

        WIZARD_HAT_HEALING = registerItem("wizard_hat_healing", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.HEALING));
        WIZARD_ROBE_HEALING = registerItem("wizard_robe_healing", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.HEALING));
        WIZARD_LEGGINGS_HEALING = registerItem("wizard_leggings_healing", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.HEALING));
        WIZARD_BOOTS_HEALING = registerItem("wizard_boots_healing", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.HEALING));

        WIZARD_HAT_NECROMANCY = registerItem("wizard_hat_necromancy", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.NECROMANCY));
        WIZARD_ROBE_NECROMANCY = registerItem("wizard_robe_necromancy", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.NECROMANCY));
        WIZARD_LEGGINGS_NECROMANCY = registerItem("wizard_leggings_necromancy", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.NECROMANCY));
        WIZARD_BOOTS_NECROMANCY = registerItem("wizard_boots_necromancy", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.NECROMANCY));

    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Wizardry.MODID, name), item);
    }
}
