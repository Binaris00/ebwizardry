package binaris.ebwizardry.item;

import binaris.ebwizardry.entity.projectile.EntityFireBomb;
import binaris.ebwizardry.registry.WizardrySounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemFireBomb extends Item {
    public ItemFireBomb(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        user.getItemCooldownManager().set(this, 20);

        user.playSound(WizardrySounds.ENTITY_FIREBOMB_THROW, 0.5F, 0.4F / (user.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!world.isClient) {
            EntityFireBomb fireBomb = new EntityFireBomb(user, world);
            fireBomb.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1F, 1.0F);

            world.spawnEntity(fireBomb);
        }

        if(!user.isCreative()){
            stack.decrement(1);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));



        return TypedActionResult.success(stack, world.isClient());

    }
}
