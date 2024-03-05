package binaris.ebwizardry.spell;

import binaris.ebwizardry.entity.living.EntitySkeletonMinion;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
@Deprecated
public class SummonSkeleton extends SpellMinion<EntitySkeletonMinion>{
    public SummonSkeleton(){
        super("summon_skeleton", EntitySkeletonMinion::new);
        this.soundValues(7, 0.6f, 0);
    }


    @Override
    protected EntitySkeletonMinion createMinion(World world, LivingEntity caster, SpellModifiers modifiers) {
        // TODO: MISSING SPELL SUMMONSKELETON
        //        if (caster instanceof Player && ItemArtefact.isArtefactActive((Player) caster, WizardryItems.CHARM_MINION_VARIANTS.get())) {
        //            return new StrayMinion(world);
        //        } else {
        //            return super.createMinion(world, caster, modifiers);
        //        }
        return super.createMinion(world, caster, modifiers);
    }

    @Override
    protected void addMinionExtras(EntitySkeletonMinion minion, BlockPos pos, LivingEntity caster, SpellModifiers modifiers, int alreadySpawned) {
        minion.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.BOW));
        minion.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.0f);
        minion.setEquipmentDropChance(EquipmentSlot.OFFHAND, 0.0f);
        super.addMinionExtras(minion, pos, caster, modifiers, alreadySpawned);
    }
}
