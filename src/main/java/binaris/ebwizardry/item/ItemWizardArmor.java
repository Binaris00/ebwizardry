package binaris.ebwizardry.item;

import binaris.ebwizardry.constant.Element;
import binaris.ebwizardry.util.DrawingUtils;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class ItemWizardArmor extends ArmorItem {
    private static final float SAGE_OTHER_COST_REDUCTION = 0.2f;
    private static final float WARLOCK_SPEED_BOOST = 0.2f;
    private static final UUID WARLOCK_SPEED_BOOST_UUID = UUID.fromString("4bad7152-2663-4b1b-bb59-552e92847031");
    private static final UUID[] ARMOR_MODIFIERS = new UUID[] {UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
            UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    public Element element;
    public ArmorType armorType;

    public ItemWizardArmor(ArmorType armorType, Type type, Settings settings, Element element) {
        super(armorType, type, settings);
        this.element = element;
        this.armorType = armorType;

        // TODO: Add to list of items that can be charge with mana flask
        // 		WizardryRecipes.addToManaFlaskCharging(this);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return DrawingUtils.mix(0xff8bfe, 0x8e2ee4, (float)getItemBarStep(stack));
    }

    // We don't like the anvils repairing our items, so we're going to override this method to prevent it..
    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }
}