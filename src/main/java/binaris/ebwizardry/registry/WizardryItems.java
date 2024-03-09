package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.constant.Element;
import binaris.ebwizardry.constant.Tier;
import binaris.ebwizardry.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public final class WizardryItems {
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
    public static Item STORE_UPGRADE;
    public static Item SIPHON_UPGRADE;
    public static Item CONDENSER_UPGRADE;
    public static Item RANGE_UPGRADE;
    public static Item DURATION_UPGRADE;
    public static Item COOLDOWN_UPGRADE;
    public static Item BLAST_UPGRADE;
    public static Item ATTUNEMENT_UPGRADE;
    public static Item MELEE_UPGRADE;
    public static Item FLAMING_AXE;
    public static Item FLAMECATCHER;
    public static Item FROST_AXE;
    public static Item SPECTRAL_SWORD;
    public static Item SPECTRAL_PICKAXE;
    public static Item SPECTRAL_BOW;

    public static ToolMaterial MAGICAL = new MaterialMagic();

    public static Item MAGIC_WAND;
    public static Item APPRENTICE_WAND;
    public static Item ADVANCED_WAND;
    public static Item MASTER_WAND;
    public static Item NOVICE_FIRE_WAND;
    public static Item APPRENTICE_FIRE_WAND;
    public static Item ADVANCED_FIRE_WAND;
    public static Item MASTER_FIRE_WAND;
    public static Item NOVICE_ICE_WAND;
    public static Item APPRENTICE_ICE_WAND;
    public static Item ADVANCED_ICE_WAND;
    public static Item MASTER_ICE_WAND;
    public static Item NOVICE_LIGHTNING_WAND;
    public static Item APPRENTICE_LIGHTNING_WAND;
    public static Item ADVANCED_LIGHTNING_WAND;
    public static Item MASTER_LIGHTNING_WAND;
    public static Item NOVICE_NECROMANCY_WAND;
    public static Item APPRENTICE_NECROMANCY_WAND;
    public static Item ADVANCED_NECROMANCY_WAND;
    public static Item MASTER_NECROMANCY_WAND;
    public static Item NOVICE_EARTH_WAND;
    public static Item APPRENTICE_EARTH_WAND;
    public static Item ADVANCED_EARTH_WAND;
    public static Item MASTER_EARTH_WAND;
    public static Item NOVICE_SORCERY_WAND;
    public static Item APPRENTICE_SORCERY_WAND;
    public static Item ADVANCED_SORCERY_WAND;
    public static Item MASTER_SORCERY_WAND;
    public static Item NOVICE_HEALING_WAND;
    public static Item APPRENTICE_HEALING_WAND;
    public static Item ADVANCED_HEALING_WAND;
    public static Item MASTER_HEALING_WAND;


    public static void register(){
        // Normal items
        SPELL_BOOK = registerItem("spell_book", new ItemSpellBook(new FabricItemSettings().maxCount(16)), WizardryGroups.SPELLS);
        SCROLL = registerItem("scroll", new ItemScroll(new FabricItemSettings().maxCount(16)), WizardryGroups.SPELLS);

        SPARK_BOMB = registerItem("spark_bomb", new ItemSparkBomb(new FabricItemSettings().maxCount(16)), WizardryGroups.GEARS);
        FIRE_BOMB = registerItem("fire_bomb", new ItemFireBomb(new FabricItemSettings().maxCount(16)), WizardryGroups.GEARS);
        POISON_BOMB = registerItem("poison_bomb", new ItemPoisonBomb(new FabricItemSettings().maxCount(16)), WizardryGroups.GEARS);
        SMOKE_BOMB = registerItem("smoke_bomb", new ItemSmokeBomb(new FabricItemSettings().maxCount(16)), WizardryGroups.GEARS);

        SILK = registerItem("magic_silk", new Item(new FabricItemSettings()), WizardryGroups.WIZARDRY);

        // ---------------- Wand Upgrades ----------------
        STORE_UPGRADE = registerItem("store_upgrade", new ItemWandUpgrade(), WizardryGroups.WIZARDRY);
        SIPHON_UPGRADE = registerItem("siphon_upgrade", new ItemWandUpgrade(), WizardryGroups.WIZARDRY);
        CONDENSER_UPGRADE = registerItem("condenser_upgrade", new ItemWandUpgrade(), WizardryGroups.WIZARDRY);
        RANGE_UPGRADE = registerItem("range_upgrade", new ItemWandUpgrade(), WizardryGroups.WIZARDRY);
        DURATION_UPGRADE = registerItem("duration_upgrade", new ItemWandUpgrade(), WizardryGroups.WIZARDRY);
        COOLDOWN_UPGRADE = registerItem("cooldown_upgrade", new ItemWandUpgrade(), WizardryGroups.WIZARDRY);
        BLAST_UPGRADE = registerItem("blast_upgrade", new ItemWandUpgrade(), WizardryGroups.WIZARDRY);
        ATTUNEMENT_UPGRADE = registerItem("attunement_upgrade", new ItemWandUpgrade(), WizardryGroups.WIZARDRY);
        MELEE_UPGRADE = registerItem("melee_upgrade", new ItemWandUpgrade(), WizardryGroups.WIZARDRY);
        // ------------------------------------------------


        // ---------------- Wizard Armor ----------------
        WIZARD_HAT = registerItem("wizard_hat", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), null), WizardryGroups.GEARS);
        WIZARD_ROBE = registerItem("wizard_robe", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), null), WizardryGroups.GEARS);
        WIZARD_LEGGINGS = registerItem("wizard_leggings", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), null), WizardryGroups.GEARS);
        WIZARD_BOOTS = registerItem("wizard_boots", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), null), WizardryGroups.GEARS);

        WIZARD_HAT_FIRE = registerItem("wizard_hat_fire", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.FIRE), WizardryGroups.GEARS);
        WIZARD_ROBE_FIRE = registerItem("wizard_robe_fire", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.FIRE), WizardryGroups.GEARS);
        WIZARD_LEGGINGS_FIRE = registerItem("wizard_leggings_fire", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.FIRE), WizardryGroups.GEARS);
        WIZARD_BOOTS_FIRE = registerItem("wizard_boots_fire", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.FIRE), WizardryGroups.GEARS);

        WIZARD_HAT_ICE = registerItem("wizard_hat_ice", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.ICE), WizardryGroups.GEARS);
        WIZARD_ROBE_ICE = registerItem("wizard_robe_ice", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.ICE), WizardryGroups.GEARS);
        WIZARD_LEGGINGS_ICE = registerItem("wizard_leggings_ice", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.ICE), WizardryGroups.GEARS);
        WIZARD_BOOTS_ICE = registerItem("wizard_boots_ice", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.ICE), WizardryGroups.GEARS);

        WIZARD_HAT_LIGHTNING = registerItem("wizard_hat_lightning", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.LIGHTNING), WizardryGroups.GEARS);
        WIZARD_ROBE_LIGHTNING = registerItem("wizard_robe_lightning", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.LIGHTNING), WizardryGroups.GEARS);
        WIZARD_LEGGINGS_LIGHTNING = registerItem("wizard_leggings_lightning", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.LIGHTNING), WizardryGroups.GEARS);
        WIZARD_BOOTS_LIGHTNING = registerItem("wizard_boots_lightning", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.LIGHTNING), WizardryGroups.GEARS);

        WIZARD_HAT_EARTH = registerItem("wizard_hat_earth", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.EARTH), WizardryGroups.GEARS);
        WIZARD_ROBE_EARTH = registerItem("wizard_robe_earth", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.EARTH), WizardryGroups.GEARS);
        WIZARD_LEGGINGS_EARTH = registerItem("wizard_leggings_earth", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.EARTH), WizardryGroups.GEARS);
        WIZARD_BOOTS_EARTH = registerItem("wizard_boots_earth", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.EARTH), WizardryGroups.GEARS);

        WIZARD_HAT_SORCERY = registerItem("wizard_hat_sorcery", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.SORCERY), WizardryGroups.GEARS);
        WIZARD_ROBE_SORCERY = registerItem("wizard_robe_sorcery", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.SORCERY), WizardryGroups.GEARS);
        WIZARD_LEGGINGS_SORCERY = registerItem("wizard_leggings_sorcery", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.SORCERY), WizardryGroups.GEARS);
        WIZARD_BOOTS_SORCERY = registerItem("wizard_boots_sorcery", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.SORCERY), WizardryGroups.GEARS);

        WIZARD_HAT_HEALING = registerItem("wizard_hat_healing", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.HEALING), WizardryGroups.GEARS);
        WIZARD_ROBE_HEALING = registerItem("wizard_robe_healing", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.HEALING), WizardryGroups.GEARS);
        WIZARD_LEGGINGS_HEALING = registerItem("wizard_leggings_healing", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.HEALING), WizardryGroups.GEARS);
        WIZARD_BOOTS_HEALING = registerItem("wizard_boots_healing", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.HEALING), WizardryGroups.GEARS);

        WIZARD_HAT_NECROMANCY = registerItem("wizard_hat_necromancy", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.HELMET, new FabricItemSettings(), Element.NECROMANCY), WizardryGroups.GEARS);
        WIZARD_ROBE_NECROMANCY = registerItem("wizard_robe_necromancy", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings(), Element.NECROMANCY), WizardryGroups.GEARS);
        WIZARD_LEGGINGS_NECROMANCY = registerItem("wizard_leggings_necromancy", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.LEGGINGS, new FabricItemSettings(), Element.NECROMANCY), WizardryGroups.GEARS);
        WIZARD_BOOTS_NECROMANCY = registerItem("wizard_boots_necromancy", new ItemWizardArmor(ArmorType.WIZARD, ArmorItem.Type.BOOTS, new FabricItemSettings(), Element.NECROMANCY), WizardryGroups.GEARS);
        // ----------------------------------------------

        // Conjured items
        // ----------------------------------------------
        FLAMING_AXE = registerItem("flaming_axe", new ItemFlamingAxe(), WizardryGroups.GEARS);
        FLAMECATCHER = registerItem("flamecatcher", new ItemFlamecatcher(), WizardryGroups.GEARS);
        FROST_AXE = registerItem("frost_axe", new ItemFrostAxe(), WizardryGroups.GEARS);
        SPECTRAL_SWORD = registerItem("spectral_sword", new ItemSpectralSword(), WizardryGroups.GEARS);
        SPECTRAL_PICKAXE = registerItem("spectral_pickaxe", new ItemSpectralPickaxe(), WizardryGroups.GEARS);
        SPECTRAL_BOW = registerItem("spectral_bow", new ItemSpectralBow(), WizardryGroups.GEARS);
        // ----------------------------------------------

        // ------------------- Wands --------------------
        MAGIC_WAND = registerItem("magic_wand", new ItemWand(Tier.NOVICE, null), WizardryGroups.WIZARDRY);
        APPRENTICE_WAND = registerItem("apprentice_wand", new ItemWand(Tier.APPRENTICE, null), WizardryGroups.WIZARDRY);
        ADVANCED_WAND = registerItem("advanced_wand", new ItemWand(Tier.ADVANCED, null), WizardryGroups.WIZARDRY);
        MASTER_WAND = registerItem("master_wand", new ItemWand(Tier.MASTER, null), WizardryGroups.WIZARDRY);

        NOVICE_FIRE_WAND = registerItem("novice_fire_wand", new ItemWand(Tier.NOVICE, Element.FIRE), WizardryGroups.WIZARDRY);
        APPRENTICE_FIRE_WAND = registerItem("apprentice_fire_wand", new ItemWand(Tier.APPRENTICE, Element.FIRE), WizardryGroups.WIZARDRY);
        ADVANCED_FIRE_WAND = registerItem("advanced_fire_wand", new ItemWand(Tier.ADVANCED, Element.FIRE), WizardryGroups.WIZARDRY);
        MASTER_FIRE_WAND = registerItem("master_fire_wand", new ItemWand(Tier.MASTER, Element.FIRE), WizardryGroups.WIZARDRY);

        NOVICE_ICE_WAND = registerItem("novice_ice_wand", new ItemWand(Tier.NOVICE, Element.ICE), WizardryGroups.WIZARDRY);
        APPRENTICE_ICE_WAND = registerItem("apprentice_ice_wand", new ItemWand(Tier.APPRENTICE, Element.ICE), WizardryGroups.WIZARDRY);
        ADVANCED_ICE_WAND = registerItem("advanced_ice_wand", new ItemWand(Tier.ADVANCED, Element.ICE), WizardryGroups.WIZARDRY);
        MASTER_ICE_WAND = registerItem("master_ice_wand", new ItemWand(Tier.MASTER, Element.ICE), WizardryGroups.WIZARDRY);

        NOVICE_LIGHTNING_WAND = registerItem("novice_lightning_wand", new ItemWand(Tier.NOVICE, Element.LIGHTNING), WizardryGroups.WIZARDRY);
        APPRENTICE_LIGHTNING_WAND = registerItem("apprentice_lightning_wand", new ItemWand(Tier.APPRENTICE, Element.LIGHTNING), WizardryGroups.WIZARDRY);
        ADVANCED_LIGHTNING_WAND = registerItem("advanced_lightning_wand", new ItemWand(Tier.ADVANCED, Element.LIGHTNING), WizardryGroups.WIZARDRY);
        MASTER_LIGHTNING_WAND = registerItem("master_lightning_wand", new ItemWand(Tier.MASTER, Element.LIGHTNING), WizardryGroups.WIZARDRY);

        NOVICE_NECROMANCY_WAND = registerItem("novice_necromancy_wand", new ItemWand(Tier.NOVICE, Element.NECROMANCY), WizardryGroups.WIZARDRY);
        APPRENTICE_NECROMANCY_WAND = registerItem("apprentice_necromancy_wand", new ItemWand(Tier.APPRENTICE, Element.NECROMANCY), WizardryGroups.WIZARDRY);
        ADVANCED_NECROMANCY_WAND = registerItem("advanced_necromancy_wand", new ItemWand(Tier.ADVANCED, Element.NECROMANCY), WizardryGroups.WIZARDRY);
        MASTER_NECROMANCY_WAND = registerItem("master_necromancy_wand", new ItemWand(Tier.MASTER, Element.NECROMANCY), WizardryGroups.WIZARDRY);

        NOVICE_EARTH_WAND = registerItem("novice_earth_wand", new ItemWand(Tier.NOVICE, Element.EARTH), WizardryGroups.WIZARDRY);
        APPRENTICE_EARTH_WAND = registerItem("apprentice_earth_wand", new ItemWand(Tier.APPRENTICE, Element.EARTH), WizardryGroups.WIZARDRY);
        ADVANCED_EARTH_WAND = registerItem("advanced_earth_wand", new ItemWand(Tier.ADVANCED, Element.EARTH), WizardryGroups.WIZARDRY);
        MASTER_EARTH_WAND = registerItem("master_earth_wand", new ItemWand(Tier.MASTER, Element.EARTH), WizardryGroups.WIZARDRY);

        NOVICE_SORCERY_WAND = registerItem("novice_sorcery_wand", new ItemWand(Tier.NOVICE, Element.SORCERY), WizardryGroups.WIZARDRY);
        APPRENTICE_SORCERY_WAND = registerItem("apprentice_sorcery_wand", new ItemWand(Tier.APPRENTICE, Element.SORCERY), WizardryGroups.WIZARDRY);
        ADVANCED_SORCERY_WAND = registerItem("advanced_sorcery_wand", new ItemWand(Tier.ADVANCED, Element.SORCERY), WizardryGroups.WIZARDRY);
        MASTER_SORCERY_WAND = registerItem("master_sorcery_wand", new ItemWand(Tier.MASTER, Element.SORCERY), WizardryGroups.WIZARDRY);

        NOVICE_HEALING_WAND = registerItem("novice_healing_wand", new ItemWand(Tier.NOVICE, Element.HEALING), WizardryGroups.WIZARDRY);
        APPRENTICE_HEALING_WAND = registerItem("apprentice_healing_wand", new ItemWand(Tier.APPRENTICE, Element.HEALING), WizardryGroups.WIZARDRY);
        ADVANCED_HEALING_WAND = registerItem("advanced_healing_wand", new ItemWand(Tier.ADVANCED, Element.HEALING), WizardryGroups.WIZARDRY);
        MASTER_HEALING_WAND = registerItem("master_healing_wand", new ItemWand(Tier.MASTER, Element.HEALING), WizardryGroups.WIZARDRY);
        // ----------------------------------------------


    }

    private static Item registerItem(String name, Item item, @Nullable RegistryKey<ItemGroup> group) {
        toItemGroup(item, group);
        return Registry.register(Registries.ITEM, new Identifier(Wizardry.MODID, name), item);
    }

    // Null if you don't want to add to a group...
    private static void toItemGroup(Item item, @Nullable RegistryKey<ItemGroup> group) {
        if(group == null) return;
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
    }
}
