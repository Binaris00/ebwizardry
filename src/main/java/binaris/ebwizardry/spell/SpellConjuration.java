package binaris.ebwizardry.spell;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.item.IConjuredItem;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.InventoryUtils;
import binaris.ebwizardry.util.ParticleBuilder;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;


/**
 * Generic superclass for all spells which conjure an item for a certain duration.
 * This allows all the relevant code to be centralised, since these spells all work in the same way. Usually, a simple
 * instantiation of this class is sufficient to create a conjuration spell; if something extra needs to be done, such as
 * a custom particle effect or conjuring the item in a specific slot, then methods can be overridden (perhaps using an
 * anonymous class) to add the required functionality.
 * <p></p>
 * Properties added by this type of spell: {@link SpellConjuration#ITEM_LIFETIME}
 * <p></p>
 * By default, this type of spell cannot be cast by NPCs. {@link Spell#canBeCastBy(LivingEntity, boolean)} 
 * <p></p>
 * By default, this type of spell cannot be cast by dispensers. {@link Spell#canBeCastBy(DoubleBlockProperties)}
 * <p></p>
 * By default, this type of spell requires a packet to be sent. {@link Spell#requiresPacket()}
 *
 * @author Electroblob
 * @since Wizardry 4.2
 * @see IConjuredItem
 */
public class SpellConjuration extends Spell{
    public static final String ITEM_LIFETIME = "item_lifetime";
    /** The item that is conjured by this spell. Should implement {@link IConjuredItem}. */
    protected final Item item;

    public SpellConjuration(String name, Item item){
        this(Wizardry.MODID, name, item);
    }

    public SpellConjuration(String modID, String name, Item item){
        super(modID, name, UseAction.NONE, false);
        this.item = item;
    }

    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers){

        if(conjureItem(caster, modifiers)){

            if(world.isClient) spawnParticles(world, caster, modifiers);

            this.playSound(world, caster, ticksInUse, -1, modifiers);
            return true;
        }

        return false;
    }

    /** Spawns sparkle particles around the caster. Override to add a custom particle effect. Only called client-side. */
    protected void spawnParticles(World world, LivingEntity caster, SpellModifiers modifiers){

        for(int i=0; i<10; i++){
            double x = caster.prevX + world.random.nextDouble() * 2 - 1;
            double y = caster.prevY + caster.getStandingEyeHeight() - 0.5 + world.random.nextDouble();
            double z = caster.prevZ + world.random.nextDouble() * 2 - 1;
            ParticleBuilder.create(WizardryParticles.SPARKLE).pos(x, y, z).velocity(0, 0.1, 0).color(0.7f, 0.9f, 1).spawn(world);
        }
    }

    /** Adds this spell's item to the given player's inventory, placing it in the main hand if the main hand is empty.
     * Returns true if the item was successfully added to the player's inventory, false if there as no space or if the
     * player already had the item. Override to add special conjuring behaviour. */
    protected boolean conjureItem(PlayerEntity caster, SpellModifiers modifiers){

        ItemStack stack = new ItemStack(item);

        if(InventoryUtils.doesPlayerHaveItem(caster, item)) return false;

        IConjuredItem.setDurationMultiplier(stack, modifiers.get(WizardryItems.DURATION_UPGRADE));
        IConjuredItem.setDamageMultiplier(stack, modifiers.get(SpellModifiers.POTENCY));

        addItemExtras(caster, stack, modifiers);

        if(caster.getMainHandStack().isEmpty()){
            caster.setStackInHand(Hand.MAIN_HAND, stack);
        }else{
            return caster.getInventory().insertStack(stack);
        }

        return true;
    }

    /**
     * Called directly <i>before</i> the conjured item is added to the inventory to perform additional behaviour (such
     * as NBT modification). Does nothing by default.
     * @param caster The player that cast this spell
     * @param stack The item stack being conjured
     * @param modifiers The modifiers this spell was cast with
     */
    protected void addItemExtras(PlayerEntity caster, ItemStack stack, SpellModifiers modifiers){}
}
