package binaris.ebwizardry.spell;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.entity.living.SummonedEntity;
import binaris.ebwizardry.registry.WizardryItems;
import binaris.ebwizardry.util.BlockUtils;
import binaris.ebwizardry.util.GeometryUtils;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import java.util.function.Function;

/**
 * Generic superclass for all spells which summon minions (i.e. instances of {@link SummonedEntity}).
 * This allows all the relevant code to be centralised, since these spells all work in the same way. Usually, a simple
 * instantiation of this class is sufficient to create a minion spell; if something extra needs to be done, such as
 * particle spawning, then methods can be overridden (perhaps using an anonymous class) to add the required functionality.
 * It is encouraged, however, to put extra functionality in the summoned creature class instead whenever possible.
 * <p></p>
 * Properties added by this type of spell: {@link SpellMinion#MINION_LIFETIME}
 * <p></p>
 * By default, this type of spell can be cast by NPCs. {@link Spell#canBeCastBy(LivingEntity, boolean)}
 * <p></p>
 * By default, this type of spell can be cast by dispensers. {@link Spell#canBeCastBy(DoubleBlockProperties)}
 * <p></p>
 * By default, this type of spell does not require a packet to be sent. {@link Spell#requiresPacket()}
 *
 * @author Electroblob
 * @since Wizardry 4.2
 */
public class SpellMinion<T extends MobEntity & SummonedEntity> extends Spell{
    /** The string identifier for the minion lifetime spell property. */
    public static final String MINION_LIFETIME = "minion_lifetime";
    /** The string identifier for the minion count spell property. */
    public static final String MINION_COUNT = "minion_count";
    /** The string identifier for the summoned radius spell property. */
    public static final String SUMMON_RADIUS = "summon_radius";

    /** The string identifier for the minion health spell modifier, which doubles as the identifier for the
     * entity attribute modifier. */
    public static final String HEALTH_MODIFIER = "minion_health";
    /** The string identifier for the potency attribute modifier. */
    public static final String POTENCY_ATTRIBUTE_MODIFIER = "potency";
    /** A factory that creates summoned creature entities. */
    protected final Function<World, T> minionFactory;
    /** Whether the minions are spawned in midair. Defaults to false. */
    protected boolean flying = false;


    public SpellMinion(String name, Function<World, T> minionFactory){
        this(Wizardry.MODID, name, minionFactory);
    }

    public SpellMinion(String modID, String name, Function<World, T> minionFactory){
        super(modID, name, UseAction.NONE, false);
        this.minionFactory = minionFactory;
        this.npcSelector((e, o) -> true);
    }

    /**
     * Sets whether the minions are spawned in midair.
     * @param flying True to spawn the minions in midair, false to spawn them on the ground.
     * @return The spell instance, allowing this method to be chained onto the constructor.
     */
    public SpellMinion<T> flying(boolean flying){
        this.flying = flying;
        return this;
    }

    @Override
    public boolean requiresPacket() {
        return false;
    }

    @Override
    public boolean canBeCastBy(DoubleBlockProperties dispenser) {
        return true;
    }

    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers) {
        if (!this.spawnMinions(world, caster, modifiers)) return false;
        this.playSound(world, caster, ticksInUse, -1, modifiers);
        return true;
    }

    @Override
    public boolean cast(World world, LivingEntity caster, Hand hand, int ticksInUse, LivingEntity target, SpellModifiers modifiers) {
        if (!this.spawnMinions(world, caster, modifiers)) return false;
        this.playSound(world, caster, ticksInUse, -1, modifiers);
        return true;
    }

    @Override
    public boolean cast(World world, double x, double y, double z, Direction direction, int ticksInUse, int duration, SpellModifiers modifiers) {
        BlockPos pos = new BlockPos((int) x, (int) y, (int) z);

        if (!world.isClient) {
            for (int i = 0; i < getIntProperty(MINION_COUNT); i++) {

                T minion = minionFactory.apply(world);

                minion.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                minion.setLifetime((int) (getFloatProperty(MINION_LIFETIME) * modifiers.get(WizardryItems.DURATION_UPGRADE)));
                this.addMinionExtras(minion, pos);

                world.spawnEntity(minion);
            }
        }

        this.playSound(world, x - direction.getOffsetX(), y - direction.getOffsetY(), z - direction.getOffsetZ(), ticksInUse, duration, modifiers);

        return true;
    }

    /**
     * Actually spawns the minions. By default, this spawns the number of minions specified by the
     * {@link SpellMinion#MINION_COUNT} property within a number of blocks of the caster specified by the property
     * {@link SpellMinion#SUMMON_RADIUS}, returning false if there is no space to spawn the minions. Override to do
     * something special, like spawning minions in a specific position.
     *
     * @param world The world in which to spawn the minions.
     * @param caster The entity that cast this spell or null if it was cast by a dispenser.
     * @param modifiers The spell modifiers this spell was cast with.
     * @return False to cause the spell to fail, true to allow it to continue.
     *
     * @see SpellMinion#addMinionExtras(MobEntity, BlockPos)
     */
    protected boolean spawnMinions(World world, LivingEntity caster, SpellModifiers modifiers) {
        if (!world.isClient) {
            for (int i = 0; i < getIntProperty(MINION_COUNT); i++) {
                int range = getIntProperty(SUMMON_RADIUS);

                BlockPos pos = BlockUtils.findNearbyFloorSpace(caster, range, range*2);

                if (flying) {
                    if (pos != null) {
                        pos = pos.up(2);
                    } else {
                        pos = caster.getBlockPos().north(world.random.nextInt(range * 2) - range).east(world.random.nextInt(range * 2) - range);
                    }
                } else {
                    if (pos == null) return false;
                }

                T minion = createMinion(world);

                minion.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                minion.setCaster(caster);

                minion.setLifetime((int) (getFloatProperty(MINION_LIFETIME) * modifiers.get(WizardryItems.DURATION_UPGRADE)));
                EntityAttributeInstance attribute = minion.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                if (attribute != null)
                    attribute.addPersistentModifier(new EntityAttributeModifier(POTENCY_ATTRIBUTE_MODIFIER, modifiers.get(SpellModifiers.POTENCY) - 1, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));

                minion.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(new EntityAttributeModifier(HEALTH_MODIFIER, modifiers.get(HEALTH_MODIFIER) - 1, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
                minion.setHealth(minion.getMaxHealth());

                this.addMinionExtras(minion, pos);

                world.spawnEntity(minion);
            }
        }

        return true;
    }
    /**
     * Creates and returns a new instance of this spell's minion entity. By default, this simply calls the apply
     * method in {@link SpellMinion#minionFactory}. Override to add logic for changing the type of entity summoned.
     *
     * @param world The world in which to spawn the minion.
     * @return The resulting minion entity.
     */
    protected T createMinion(World world) {
        return minionFactory.apply(world);
    }

    /**
     * Called just before each minion is spawned. Calls {@link MobEntity#initialize(ServerWorldAccess, LocalDifficulty, SpawnReason, EntityData, NbtCompound)}
     * by default, but subclasses can override to call extra methods on the summoned entity, for example, to add
     * special equipment. This method is only called server-side so cannot be used to spawn particles directly.
     * @param minion The entity being spawned.
     * @param pos The position at which the entity was spawned.
     * {@link SpellMinion#MINION_COUNT}.
     */
    protected void addMinionExtras(T minion, BlockPos pos) {
        minion.initialize((ServerWorldAccess) minion.getWorld(), minion.getWorld().getLocalDifficulty(pos), null, null, null);
    }
}
