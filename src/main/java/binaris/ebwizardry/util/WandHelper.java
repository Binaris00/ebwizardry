package binaris.ebwizardry.util;

import net.minecraft.item.Item;

import java.util.HashMap;

public class WandHelper {
    private static final HashMap<Item, String> upgradeMap = new HashMap<>();

    /** Returns true if the given item is a valid special wand upgrade. */
    public static boolean isWandUpgrade(Item upgrade){
        return upgradeMap.containsKey(upgrade);
    }

    /**
     * Package-protected getter for the identifier that corresponds to the given item, used only in the
     * {@link SpellModifiers} class. Internal to Wizardry.
     *
     * @throws IllegalArgumentException if the given item is not a registered special wand upgrade.
     */
    static String getIdentifier(Item upgrade){
        if(!isWandUpgrade(upgrade)) throw new IllegalArgumentException(
                "Tried to get a wand upgrade key for an item" + "that is not a registered special wand upgrade.");
        return upgradeMap.get(upgrade);
    }
}
