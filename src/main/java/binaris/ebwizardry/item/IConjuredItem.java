package binaris.ebwizardry.item;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.spell.SpellConjuration;
import binaris.ebwizardry.util.DrawingUtils;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.UUID;

public interface IConjuredItem {
    /** The NBT tag key used to store the duration multiplier for conjured items. */
    String DURATION_MULTIPLIER_KEY = "durationMultiplier";
    /** The NBT tag key used to store the damage multiplier for conjured items. */
    String DAMAGE_MULTIPLIER = "damageMultiplier";

    UUID POTENCY_MODIFIER = UUID.fromString("da067ea6-0b35-4140-8436-5476224de9dd");


    /** Helper method for setting the duration multiplier (via NBT) for conjured items. */
    static void setDurationMultiplier(ItemStack stack, float multiplier){
        if(!stack.hasNbt()) stack.setNbt(new NbtCompound());
        stack.getNbt().putFloat(DURATION_MULTIPLIER_KEY, multiplier);
    }

    /** Helper method for setting the damage multiplier (via NBT) for conjured items. */
    static void setDamageMultiplier(ItemStack stack, float multiplier){
        if(!stack.hasNbt()) stack.setNbt(new NbtCompound());
        stack.getNbt().putFloat(DAMAGE_MULTIPLIER, multiplier);
    }

    /** Helper method for getting the damage multiplier (via NBT) for conjured items. */
    static float getDamageMultiplier(ItemStack stack){
        if(!stack.hasNbt()) return 1;
        return stack.getNbt().getFloat(DAMAGE_MULTIPLIER);
    }

    /**
     * Helper method for returning the max damage of a conjured item based on its NBT data. Centralises the code.
     * Implementors will almost certainly want to call this from {@link Item#getMaxDamage()}}.
     */
    default int getMaxDamageFromNBT(ItemStack stack, Spell spell){

        if(!spell.arePropertiesInitialised()) return 600; // Failsafe, some edge-cases call this during preInit

        float baseDuration = spell.getFloatProperty(SpellConjuration.ITEM_LIFETIME);

        if(stack.hasNbt() && stack.getNbt().contains(DURATION_MULTIPLIER_KEY)){
            return (int)(baseDuration * stack.getNbt().getFloat(DURATION_MULTIPLIER_KEY));
        }

        return (int)baseDuration;
    }

    /**
     * Adds property overrides to define the conjuring/vanishing animation. Call this from the item's constructor.
     */
    default void addAnimationPropertyOverrides(){
        Wizardry.LOGGER.info("Adding animation property overrides for " + this);
        if(!(this instanceof Item item)) throw new ClassCastException("Cannot set up conjuring animations for a non-item!");

        final int frames = getAnimationFrames();

        ModelPredicateProviderRegistry.register(item, new Identifier("conjure"), (stack, clientWorld, entity, seed) -> stack.getDamage() < frames ? (float) stack.getDamage() / frames
                        : (float) (stack.getMaxDamage() - stack.getDamage()) / frames);

        ModelPredicateProviderRegistry.register(item, new Identifier("conjuring"), (stack, clientWorld, entity, seed) ->
                stack.getDamage() < frames
                || stack.getDamage() > stack.getMaxDamage() - frames ? 1.0F : 0.0F);
    }

    /** Returns the number of frames in the conjuring/vanishing animation. Override to change the number of frames
     * set by {@link IConjuredItem#addAnimationPropertyOverrides()}. */
    default int getAnimationFrames(){
        return 8;
    }

    /** Returns a blue colour to use for the durability bar for the given stack, so all items that use it as a timer
     * have the same colours. */
    static int getTimerBarColour(ItemStack stack){
        return DrawingUtils.mix(0x8bffe0, 0x2ed1e4, (float)stack.getItem().getItemBarStep(stack));
    }

}
