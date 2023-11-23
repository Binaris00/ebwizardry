package binaris.ebwizardry.item;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.entity.projectile.EntitySparkBomb;
import binaris.ebwizardry.registry.WizardrySounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemSparkBomb extends Item {
    public ItemSparkBomb(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        user.playSound(WizardrySounds.ENTITY_SPARK_BOMB_THROW, 0.5F, 0.4F / (Wizardry.random.nextFloat() * 0.4F + 0.8F));

        user.getItemCooldownManager().set(this, 20);

        if (!world.isClient) {
            EntitySparkBomb sparkBomb = new EntitySparkBomb(user, world);
            sparkBomb.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1F, 1.0F);

            world.spawnEntity(sparkBomb);
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}
