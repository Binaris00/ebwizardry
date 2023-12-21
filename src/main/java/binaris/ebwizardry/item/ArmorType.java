package binaris.ebwizardry.item;

import binaris.ebwizardry.constant.Element;
import binaris.ebwizardry.registry.WizardryItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Enum defining the different classes of wizard armor. Each class has its own armor material, upgrade item, and
 * armor piece names. The armor piece names are used to construct the registry names for the armor items.
 * Implementation note: This enum implements ArmorMaterial so that the armor material can be accessed directly from
 * the enum value.
 * */
public enum ArmorType implements ArmorMaterial{
    /** The default wizard armor class. */
    WIZARD("wizard", WizardryItems.SILK, 15,() -> null, 0.1F, 0, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, new int[]{2, 4, 5, 2}, 15, "hat", "robe", "leggings", "boots");

    /** The base durability of each piece of armor in this class. */
    private final int[] BASE_DURABILITY = new int[] {13, 15, 16, 11};

    /** The protection values of each piece of armor in this class. */
    final int[] protectionValues;

    final int durabilityMultiplier;
    /** The first part of the texture filenames for this armor class. Also defines certain translation keys. */
    final String name;
    /** The armor material to use for this armour class. */
    final Item material;
    /** The item that upgrades regular wizard armour into this class of armour. */
    final Supplier<Item> upgradeItem;
    /** The fraction by which spell cost is reduced for each armour piece of this class with the matching element. */
    final float elementalCostReduction;
    /** The fraction by which spell cooldown is reduced for each armour piece of this class. */
    final float cooldownReduction;
    /** The middle part of each item name (in the registry) for this armour class, e.g. "hat" or "chestplate". */
    @Deprecated
    final Map<EquipmentSlot, String> armourPieceNames;

    /** The sound played when this armor class is equipped. */
    final SoundEvent equipSound;
    /** The enchantability of this armor class. */
    final int enchantability;
    Element element = null;

    ArmorType(String name, Item material, int durabilityMultiplier, Supplier<Item> upgradeItem, float elementalCostReduction, float cooldownReduction, SoundEvent equipSound, int[] protectionValues, int enchantability, String... armourPieceNames){
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.material = material;
        this.upgradeItem = upgradeItem;
        this.elementalCostReduction = elementalCostReduction;
        this.cooldownReduction = cooldownReduction;
        this.equipSound = equipSound;
        this.protectionValues = protectionValues;
        this.enchantability = enchantability;


        if(armourPieceNames.length != 4){throw new IllegalArgumentException("Armour class " + name + " must have exactly 4 armour piece names. Try again!!!!!");}
        this.armourPieceNames = new EnumMap<>(EquipmentSlot.class);
        this.armourPieceNames.put(EquipmentSlot.HEAD, armourPieceNames[0]);
        this.armourPieceNames.put(EquipmentSlot.CHEST, armourPieceNames[1]);
        this.armourPieceNames.put(EquipmentSlot.LEGS, armourPieceNames[2]);
        this.armourPieceNames.put(EquipmentSlot.FEET, armourPieceNames[3]);

    }
    // --------------------------------- ArmorMaterial methods --------------------------------- //
    @Override
    public int getDurability(ArmorItem.Type type) {
        return BASE_DURABILITY[type.ordinal()] * durabilityMultiplier;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return protectionValues[type.ordinal()];
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }

    // We don't want to allow repairing of wizard armor in an anvil, so we return null here.
    @Override
    public Ingredient getRepairIngredient() {
        return null;
    }

    @Override
    public String getName() {
        if (element != null) {
            return name + "_" + element.asString();
        } else {
            return name;
        }
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }

    // --------------------------------- Magic stuff here uuuu --------------------------------- //

    public void setElement(Element element){
        this.element = element;
    }
}
