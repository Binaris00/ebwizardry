package binaris.ebwizardry.spell;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.entity.projectile.EntityMagicProjectile;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Generic superclass for all spells which launch non-directed projectiles (i.e. instances of {@link EntityMagicProjectile}).
 * This allows all the relevant code to be centralised, since these spells all work in the same way. Usually, a simple
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
public class SpellProjectile<T extends EntityMagicProjectile> extends Spell {
    private static final float DISPENSER_INACCURACY = 1; // This is the same as for players
    private static final float FALLBACK_VELOCITY = 1.5f; // 1.5 seems to be a pretty standard value
    /** A factory that creates projectile entities. */
    protected final Function<World, T> projectileFactory;

    /**
     * Constructor for the SpellProjectile class.
     * @param name The name of the spell.
     * @param projectileFactory The factory function to create the projectile.
     */
    public SpellProjectile(String name, Function<World, T> projectileFactory) {
        this(Wizardry.MODID, name, projectileFactory);
    }

    public SpellProjectile(String modID, String name, Function<World, T> projectileFactory){
        super(modID, name, UseAction.NONE, false);
        this.projectileFactory = projectileFactory;

        // TODO: NPC Stuff here...
        // this.npcSelector((e, o) -> true);
    }


    /**
     * This method calculates the velocity at which the projectile should be launched to achieve the required range.
     * The velocity is calculated based on the range property of the spell, the gravity effect on the projectile, and the launch height.
     * If gravity does not affect the projectile, the velocity is calculated as the range divided by the fire ticks of the projectile.
     * If gravity affects the projectile, the velocity is calculated using the formula for the range of a projectile launched horizontally under constant gravity.
     * @param projectile The projectile entity.
     * @param modifiers The spell modifiers.
     * @param launchHeight The height from which the projectile is launched.
     * @return The velocity at which the projectile should be launched.
     */
    protected float calculateVelocity(EntityMagicProjectile projectile, SpellModifiers modifiers, float launchHeight){
        // The required range
        // TODO: Range upgrade here...
        // float range = getFloatProperty(RANGE) * modifiers.get(WizardryItems.range_upgrade);
        float range = getFloatProperty(RANGE);

        if(projectile.hasNoGravity()){
            // No sensible spell will do this - range is meaningless if the projectile has no gravity or lifetime
            if(projectile.getFireTicks() <= 0) return FALLBACK_VELOCITY;
            // Speed = distance/time (trivial, I know, but I've put it here for the sake of completeness)
            return range / projectile.getFireTicks();
        }else{
            // It seems that in Minecraft, g is usually* 0.03 - the getter method is protected unfortunately
            // * Potions and xp bottles seem to have more gravity (because that makes sense...)
            float g = 0.03f;
            // Assume horizontal projection
            return range / MathHelper.sqrt(2 * launchHeight/g);
        }
    }
    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers) {
        if(!world.isClient()){
            // Creates a projectile from the supplied factory
            T projectile = projectileFactory.apply(world);
            // Sets the necessary parameters
            projectile.aim(caster, calculateVelocity(projectile, modifiers, caster.getStandingEyeHeight() - (float)EntityMagicProjectile.LAUNCH_Y_OFFSET));

            projectile.damageMultiplier = modifiers.get(SpellModifiers.POTENCY);
            // TODO: Blast update here...
            // if(projectile instanceof EntityBomb) ((EntityBomb)projectile).blastMultiplier = modifiers.get(WizardryItems.blast_upgrade);

            addProjectileExtras(projectile, caster, modifiers);
            // Spawns the projectile in the world
            world.spawnEntity(projectile);
        }

        caster.swingHand(hand);

        // TODO: Missing sound stuff here...
        //this.playSound(world, caster, ticksInUse, -1, modifiers);

        return true;
    }


    /**
     * This method is called just before the projectile is spawned.
     * It is intended to be overridden by subclasses to add extra methods or behaviors to the projectile.
     * By default, it does nothing.
     * Note that this method is only called server-side, so it cannot be used to spawn particles directly.
     * @param projectile The entity being spawned.
     * @param caster The caster of this spell, or null if it was cast by a dispenser.
     * @param modifiers The modifiers this spell was cast with.
     */
    protected void addProjectileExtras(T projectile, @Nullable LivingEntity caster, SpellModifiers modifiers){
        // Subclasses can put spell-specific stuff here
    }


    @Override public boolean requiresPacket(){
        return false;
    }
    @Override
    public boolean canBeCastBy(DoubleBlockProperties dispenser) {
        return true;
    }
}
