package binaris.ebwizardry.item;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.SpellModifiers;
import binaris.ebwizardry.util.SpellUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemScroll extends Item implements WorkbenchItem, SpellCastingItem {
    public ItemScroll(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        Spell spell = SpellUtils.getSpell(stack);

        if(spell == null) {
            Wizardry.LOGGER.info("Spell is null");
            return TypedActionResult.fail(stack);
        }


        SpellModifiers modifiers = new SpellModifiers();
        if(canCast(stack, spell, user, hand, 0, modifiers)){
            // Now we can cast continuous spells with scrolls!
            if(spell.isContinuous){
                if(!user.isUsingItem()){
                    user.setCurrentHand(hand);
                    // Store the modifiers for use each tick (there aren't any by default but there could be, as above)
                    // TODO: WizardData stuff here...
                    // if(WizardData.get(player) != null) WizardData.get(player).itemCastingModifiers = modifiers;
                    return TypedActionResult.success(stack);
                }
            }else{
                if(cast(stack, spell, user, hand, 0, modifiers)){
                    return TypedActionResult.success(stack);
                }
            }
        }
        Wizardry.LOGGER.info("Spell cast failed");
        return TypedActionResult.fail(stack);
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Spell spell = SpellUtils.getSpell(stack);



        // TODO: Tooltip for discovering system.
        if(spell == null) return;

        tooltip.add(spell.getNameWithFormatting());
        tooltip.add(spell.getProperties().getTier().getNameForTranslationFormatted());

        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if(user instanceof PlayerEntity player){
            Spell spell = SpellUtils.getSpell(stack);

            SpellModifiers modifiers = new SpellModifiers();
            int castingTick = stack.getMaxUseTime() - remainingUseTicks;

            // TODO: WizardData stuff here...

            if(spell.isContinuous && spell.cast(world, player, Hand.MAIN_HAND, castingTick, modifiers)){
                cast(stack, spell, player, Hand.MAIN_HAND, castingTick, modifiers);
            } else {
                player.stopUsingItem();
            }
        }

        super.usageTick(world, user, stack, remainingUseTicks);
    }

    // =============================== WorkbenchItem ===============================
    // All the methods below are used to make the item work in the workbench GUI.
    // =============================================================================
    @Override
    public int getSpellSlotCount(ItemStack stack) {
        return 1; // Scrolls only have one spell slot
    }

    @Override
    public boolean onApplyButtonPressed(PlayerEntity player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {
        return false;
    }

    @Override
    public boolean showTooltip(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canPlace(ItemStack stack) {
        return false;
    }


    // ============================== SpellCastingItem ==============================
    // All the methods below are used to make the item cast spells.
    // =============================================================================

    @Override
    public @NotNull Spell getCurrentSpell(ItemStack stack) {
        return SpellUtils.getSpell(stack);
    }

    @Override
    public boolean showSpellHUD(PlayerEntity player, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canCast(ItemStack stack, Spell spell, PlayerEntity caster, Hand hand, int castingTick, SpellModifiers modifiers) {
        // TODO: Scroll event
        //		// Even neater!
        //		if(castingTick == 0){
        //			return !MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Pre(Source.SCROLL, spell, caster, modifiers));
        //		}else{
        //			return !MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Tick(Source.SCROLL, spell, caster, modifiers, castingTick));
        //		}
        return true;
    }

    @Override
    public boolean cast(ItemStack stack, Spell spell, PlayerEntity caster, Hand hand, int castingTick, SpellModifiers modifiers) {
        World world = caster.getWorld();

        //if(world.isClient && !spell.isContinuous && spell.requiresPacket()) return false;


        if(spell.cast(world, caster, hand, castingTick, modifiers)){
            // TODO: Scroll event
            Wizardry.LOGGER.info("Spell cast");
            if(!world.isClient()){
                // Continuous spells never require packets so don't rely on the requiresPacket method to specify it
                if(!spell.isContinuous && spell.requiresPacket()){
                    // TODO: Scroll packet
                }

                // Scrolls are consumed upon successful use in survival mode
                if(!spell.isContinuous && !caster.isCreative()) stack.decrement(1);

                // Now uses the vanilla cooldown mechanic to prevent spamming of spells
                if(!spell.isContinuous && !caster.isCreative()) caster.getItemCooldownManager().set(this, spell.getProperties().getCooldown());
            }

            return true;
        }
        Wizardry.LOGGER.info("Spell cast failed in cast method");
        return false;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        finishUsing(stack, user, 0);
    }

    private void finishUsing(ItemStack stack, LivingEntity user, int timeLeft) {
        if(SpellUtils.getSpell(stack).isContinuous) {
            if (!(user instanceof PlayerEntity) || !((PlayerEntity) user).isCreative()) stack.decrement(1);

            Spell spell = SpellUtils.getSpell(stack);
            SpellModifiers modifiers = new SpellModifiers();
            int castingTick = stack.getMaxUseTime() - timeLeft;

            // TODO: Scroll event

            spell.finishCasting(user.getWorld(), user, Double.NaN, Double.NaN, Double.NaN, null, castingTick, modifiers);

            if(user instanceof PlayerEntity && !((PlayerEntity)user).isCreative()){
                ((PlayerEntity)user).getItemCooldownManager().set(this, spell.getProperties().getCooldown());
            }
        }
    }

    // =============================== Translation Keys ===============================
    // A different translation key is used for each spell
    // ===============================================================================


    @Override
    protected String getOrCreateTranslationKey() {
        return super.getOrCreateTranslationKey();
    }
}
