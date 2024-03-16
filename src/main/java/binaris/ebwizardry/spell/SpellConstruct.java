package binaris.ebwizardry.spell;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.entity.construct.EntityMagicConstruct;
import binaris.ebwizardry.entity.construct.EntityScaledConstruct;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.util.BlockUtils;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class SpellConstruct<T extends EntityMagicConstruct> extends Spell{
    protected final Function<World, T> constructFactory;

    protected final boolean permanent;

    protected boolean requiresFloor = false;

    protected boolean allowOverlap = false;

    public SpellConstruct(String name, UseAction action, Function<World, T> constructFactory, boolean permanent) {
        this(Wizardry.MODID, name, action, constructFactory, permanent);
    }

    public SpellConstruct(String modID, String name, UseAction action, Function<World, T> constructFactory, boolean permanent) {
        super(modID, name, action, false);
        this.constructFactory = constructFactory;
        this.permanent = permanent;
        this.npcSelector((e, o) -> true);
    }

    @Override
    public boolean requiresPacket() {
        return false;
    }

    @Override
    public boolean canBeCastBy(DoubleBlockProperties dispenser) {
        return true;
    }

    public SpellConstruct<T> floor(boolean requiresFloor) {
        this.requiresFloor = requiresFloor;
        return this;
    }

    public SpellConstruct<T> overlap(boolean allowOverlap) {
        this.allowOverlap = allowOverlap;
        return this;
    }

    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers) {
        if (caster.isOnGround() || !requiresFloor) {
            if (!spawnConstruct(world, caster.getX(), caster.getY(), caster.getZ(), caster.isOnGround() ? Direction.UP : null, caster, modifiers))
                return false;
            this.playSound(world, caster, ticksInUse, -1, modifiers);
            return true;
        }

        return false;
    }

    @Override
    public boolean cast(World world, LivingEntity caster, Hand hand, int ticksInUse, LivingEntity target, SpellModifiers modifiers) {
        if (target != null) {
            if (caster.isOnGround() || !requiresFloor) {
                if (!spawnConstruct(world, caster.getX(), caster.getY(), caster.getZ(), caster.isOnGround() ? Direction.UP : null, caster, modifiers))
                    return false;
                this.playSound(world, caster, ticksInUse, -1, modifiers);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean cast(World world, double x, double y, double z, Direction direction, int ticksInUse, int duration, SpellModifiers modifiers) {
        Integer floor = (int) y;

        if (requiresFloor) {
            floor = BlockUtils.getNearestFloor(world, new BlockPos((int) x, (int) y, (int) z), 1);
            direction = Direction.UP;
        }

        if (floor != null) {
            if (!spawnConstruct(world, x, floor, z, direction, null, modifiers)) return false;
            this.playSound(world, x - direction.getOffsetX(), y - direction.getOffsetY(), z - direction.getOffsetZ(), ticksInUse, duration, modifiers);
            return true;
        }

        return false;
    }

    protected boolean spawnConstruct(World world, double x, double y, double z, @Nullable Direction side, @Nullable LivingEntity caster, SpellModifiers modifiers) {
        if (!world.isClient) {
            T construct = constructFactory.apply(world);

            construct.setPos(x, y, z);
            construct.setCaster(caster);
            construct.lifetime = permanent ? -1 : (int) (getFloatProperty(DURATION) * modifiers.get(WizardryItems.DURATION_UPGRADE));
            construct.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
            if (construct instanceof EntityScaledConstruct scaledConstruct)
                scaledConstruct.setSizeMultiplier(modifiers.get(WizardryItems.BLAST_UPGRADE));
            addConstructExtras(construct, side, caster, modifiers);

            if (!allowOverlap && !world.getNonSpectatingEntities(construct.getClass(), construct.getBoundingBox()).isEmpty())
                return false;

            world.spawnEntity(construct);
        }

        return true;
    }

    protected void addConstructExtras(T construct, Direction side, @Nullable LivingEntity caster, SpellModifiers modifiers) {
    }
}
