package binaris.ebwizardry.spell;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.config.SpellProperties;
import binaris.ebwizardry.entity.projectile.EntityMagicArrow;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Generic superclass for all spells which launch directed projectiles (i.e. instances of {@link EntityMagicArrow}). This
 * allows all the relevant code to be centralised, since these spells all work in the same way. Usually, a simple
 * instantiation of this class is sufficient to create a projectile spell; if something extra needs to be done, such as
 * particle spawning, then methods can be overridden (perhaps using an anonymous class) to add the required functionality.
 * It is encouraged, however, to put extra functionality in the projectile class instead whenever possible.
 * <p></p>
 * Properties added by this type of spell: {@link Spell#RANGE}
 * <p></p>
 * By default, this type of spell can be cast by NPCs. {@link Spell#canBeCastBy(LivingEntity, boolean)}
 * <p></p>
 * By default, this type of spell can be cast by dispensers. {@link Spell#canBeCastBy(DoubleBlockProperties)}
 * <p></p>
 * By default, this type of spell does not require a packet to be sent. {@link Spell#requiresPacket()}
 */
public class SpellArrow<T extends EntityMagicArrow> extends Spell{
    private static final float DISPENSER_INACCURACY = 1; // This is the same as for players
    private static final float FALLBACK_VELOCITY = 2; // 2 seems to be a pretty standard value

    // The general contract for these spell subtypes is that any required parameters are set via the constructor and are
    // final, whereas any non-critical parameters are set via chainable setters with sensible defaults if not. For example,
    // the actual sound to play is required, but it makes sense for its volume and pitch to default to 1 if unspecified.

    /** A factory that creates projectile entities. */
    protected final Function<World, T> arrowFactory;

    public SpellArrow(String name, Function<World, T> arrowFactory){
        this(Wizardry.MODID, name, arrowFactory);
    }

    public SpellArrow(String modID, String name, Function<World, T> arrowFactory) {
        super(modID, name, UseAction.NONE, false);
        this.arrowFactory = arrowFactory;

        // TODO: NPC stuff here
        //this.npcSelector((e, o) -> true);
    }

    @Override
    public boolean requiresPacket() {
        return false;
    }
    @Override
    public boolean canBeCastBy(DoubleBlockProperties dispenser) {
        return true;
    }

    /** Computes the velocity the projectile should be launched at to achieve the required range. */
    protected float calculateVelocity(EntityMagicArrow projectile, SpellModifiers modifiers, float launchHeight){
        // The required range
        // TODO: RANGE UPDATE
        float range = getFloatProperty(RANGE); // * modifiers.get(WizardryItems.range_upgrade);

        if(!projectile.doGravity()){
            // No sensible spell will do this - range is meaningless if the particle has no gravity or lifetime
            if(projectile.getLifetime() <= 0) return FALLBACK_VELOCITY;
            // Speed = distance/time (trivial, I know, but I've put it here for the sake of completeness)
            return range / projectile.getLifetime();
        }else{
            // Arrows have gravity 0.05
            float g = 0.05f;
            // Assume horizontal projection
            return range / MathHelper.sqrt(2 * launchHeight/g);
        }
    }



    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers) {
        if(!world.isClient){
            // Creates a projectile from the supplied factory
            T projectile = arrowFactory.apply(world);
            // Sets the necessary parameters
            //projectile.aim(caster, calculateVelocity(projectile, modifiers, caster.getStandingEyeHeight()
            //        - (float)EntityMagicArrow.LAUNCH_Y_OFFSET));

            // TODO: Spell Modifiers...
            // projectile.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
            // addArrowExtras(projectile, caster, modifiers);
            world.spawnEntity(projectile);
        }
        // TODO: Spell sound
        //this.playSound(world, caster, ticksInUse, -1, modifiers);

        return true;
    }

    @Override
    public boolean cast(World world, double x, double y, double z, Direction direction, int ticksInUse, int duration, SpellProperties properties) {
        if(!world.isClient){
            // Creates a projectile from the supplied factory
            T projectile = arrowFactory.apply(world);
            // Sets the necessary parameters
            projectile.setPosition(x, y, z);
            projectile.setVelocity(projectile.getOwner(), (float) x, (float) y, (float) z, projectile.getOwner().getPitch(), projectile.getOwner().getYaw());
            projectile.aim((LivingEntity) projectile.getOwner(), calculateVelocity(projectile, new SpellModifiers(), projectile.getOwner().getStandingEyeHeight() - (float)EntityMagicArrow.LAUNCH_Y_OFFSET));
            Vec3i vec = direction.getVector();

            // TODO: Spell Modifiers...
            //projectile.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
            //addArrowExtras(projectile, null, modifiers);
            world.spawnEntity(projectile);
        }

        // TODO: Spell sound
        // this.playSound(world, x - direction.getXOffset(), y - direction.getYOffset(), z - direction.getZOffset(), ticksInUse, duration, modifiers);

        return true;
    }



    /**
     * Called just before the arrow is spawned. Does nothing by default, but subclasses can override to call extra
     * methods on the spawned arrow. This method is only called server-side so cannot be used to spawn particles directly.
     * @param arrow The entity being spawned.
     * @param caster The caster of this spell, or null if it was cast by a dispenser.
     * @param modifiers The modifiers this spell was cast with.
     */
    protected void addArrowExtras(T arrow, @Nullable LivingEntity caster, SpellModifiers modifiers){
        // Subclasses can put spell-specific stuff here
    }
}
