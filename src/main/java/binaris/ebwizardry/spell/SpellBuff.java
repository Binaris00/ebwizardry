package binaris.ebwizardry.spell;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.util.ParticleBuilder;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Potion;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Generic superclass for all spells which buff their caster.
 * This allows all the relevant code to be centralised, since these spells all work in the same way. Usually, a simple
 * instantiation of this class is sufficient to create a buff spell; if something extra needs to be done, such as
 * applying a non-potion buff, then methods can be overridden (perhaps using an anonymous class) to add the required
 * functionality.
 * <p></p>
 * Properties added by this type of spell: {@link SpellBuff#getDurationKey(String)}, {@link SpellBuff#getStrengthKey(String)}
 * <p></p>
 * By default, this type of spell can be cast by NPCs. {@link Spell#canBeCastBy(LivingEntity, boolean)} 
 * <p></p>
 * By default, this type of spell can be cast by dispensers. {@link Spell#canBeCastBy(DoubleBlockProperties)}
 * <p></p>
 * By default, this type of spell requires a packet to be sent. {@link Spell#requiresPacket()}
 *
 * @author Electroblob
 * @since Wizardry 4.2
 */
public class SpellBuff extends Spell{
    /** An array of factories for the status effects that this spell applies to its caster. The effect factory
     * avoids the issue of the potions being registered after the spell. */
    protected final Supplier<StatusEffect>[] effects;
    /** A set of all the different potions (status effects) that this spell applies to its caster. Loaded during
     * init(). */
    protected Set<StatusEffect> potionSet = new java.util.HashSet<>();
    /** The RGB colour values of the particles spawned when this spell is cast. */
    protected final float r, g, b;

    /** The number of sparkle particles spawned when this spell is cast. Defaults to 10. */
    protected float particleCount = 10;
    @SafeVarargs
    public SpellBuff(String name, float r, float g, float b, Supplier<StatusEffect>... effects){
        this(Wizardry.MODID, name, r, g, b, effects);
    }

    @SafeVarargs
    public SpellBuff(String modID, String name, float r, float g, float b, Supplier<StatusEffect>... effects){
        super(modID, name, UseAction.NONE, false);
        this.effects = effects;
        this.r = r;
        this.g = g;
        this.b = b;
        // add all effects to potionSet
        for(Supplier<StatusEffect> effect : effects){
            potionSet.add(effect.get());
        }

        // TODO: NPC STUFF
        //this.npcSelector((e, o) -> true);
    }
    /**
     * Sets the number of sparkle particles spawned when this spell is cast.
     * @param particleCount The number of particles.
     * @return The spell instance, allowing this method to be chained onto the constructor.
     */
    public SpellBuff particleCount(int particleCount){
        this.particleCount = particleCount;
        return this;
    }

    @Override
    public boolean canBeCastBy(DoubleBlockProperties dispenser) {
        return true;
    }

    /** Returns an unmodifiable view of the set of {@link Potion} objects that this spell applies to its caster. */
    public Set<StatusEffect> getPotionSet(){
        return Collections.unmodifiableSet(potionSet);
    }

    public static String getDurationKey(String effect){
        return "duration_" + effect;
    }

    public static String getStrengthKey(String effect){
        return "strength_" + effect;
    }


    @Override
    public boolean cast(World world, PlayerEntity caster, Hand hand, int ticksInUse, SpellModifiers modifiers){
        // Only return on the server side or the client probably won't spawn particles
        if(!this.applyEffects(caster, modifiers) && !world.isClient) return false;
        if(world.isClient) this.spawnParticles(world, caster, modifiers);
        // TODO: SOUND
        //this.playSound(world, caster, ticksInUse, -1, modifiers);
        return true;
    }

    @Override
    public boolean cast(World world, LivingEntity caster, Hand hand, int ticksInUse, LivingEntity target, SpellModifiers modifiers){
        // Wizards can only cast a buff spell if they don't already have its effects.
        // Some buff spells doesn't add any potion effects, those are ignored by this check
        if(!potionSet.isEmpty() && caster.getActiveStatusEffects().keySet().containsAll(potionSet)) return false;
        // Only return on the server side or the client probably won't spawn particles
        if(!this.applyEffects(caster, modifiers) && !world.isClient) return false;
        if(world.isClient) this.spawnParticles(world, caster, modifiers);
        // TODO: SOUND
        //this.playSound(world, caster, ticksInUse, -1, modifiers);
        return true;
    }

    @Override
    public boolean cast(World world, double x, double y, double z, Direction direction, int ticksInUse, int duration, SpellModifiers modifiers) {
// Gets a 1x1x1 bounding box corresponding to the block in front of the dispenser
        Box boundingBox = new Box(new BlockPos((int) x, (int) y, (int) z));
        List<LivingEntity> entities = world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), boundingBox, EntityPredicates.VALID_LIVING_ENTITY);

        float distance = -1;
        LivingEntity nearestEntity = null;
        // Finds the nearest entity within the bounding box
        for(LivingEntity entity : entities){
            float newDistance = (float)entity.squaredDistanceTo(x, y, z);
            if(distance == -1 || newDistance < distance){
                distance = newDistance;
                nearestEntity = entity;
            }
        }

        if(nearestEntity == null) return false;

        // Only return on the server side or the client probably won't spawn particles
        if(!this.applyEffects(nearestEntity, modifiers) && !world.isClient) return false;
        if(world.isClient) this.spawnParticles(world, nearestEntity, modifiers);
        // TODO: SOUND
        //this.playSound(world, x - direction.getXOffset(), y - direction.getYOffset(), z - direction.getZOffset(), ticksInUse, duration, modifiers);

        return true;
    }

    /** Actually applies the status effects to the caster. By default, this iterates through the array of effects and
     * applies each in turn, multiplying the duration and amplifier by the appropriate modifiers. Particles are always
     * hidden and isAmbient is always set to false. Override to do something special, like apply a non-potion buff.
     * Returns a boolean to allow subclasses to cause the spell to fail if for some reason the effect cannot be applied
     * (for example, {@link Heal} fails if the caster is on full health). */
    protected boolean applyEffects(LivingEntity caster, SpellModifiers modifiers){
        // TODO: MODIFIERS
        int bonusAmplifier = getBonusAmplifier(modifiers.get(SpellModifiers.POTENCY));

        for(StatusEffect effect : potionSet){
            // TODO: MODIFIERS
            // (int)(getProperty(getDurationKey(potion)).floatValue() * modifiers.get(WizardryItems.duration_upgrade)),
            caster.addStatusEffect(new StatusEffectInstance(effect, effect.isInstant() ? 1 : (int)(getFloatProperty(getDurationKey(Registries.STATUS_EFFECT.getId(effect).getPath()))),
                    (int)getFloatProperty(getStrengthKey(Registries.STATUS_EFFECT.getId(effect).getPath())) + bonusAmplifier, false, true
            ));
        }

        return true;
    }

    /** Returns the number to be added to the potion amplifier(s) based on the given potency modifier. Override
     * to define custom modifier handling. Delegates to {@link SpellBuff#getStandardBonusAmplifier(float)} by
     * default. */
    protected int getBonusAmplifier(float potencyModifier){
        return getStandardBonusAmplifier(potencyModifier);
    }

    /** Returns a number to be added to potion amplifiers based on the given potency modifier. This method uses
     * a standard calculation which results in zero extra levels for novice and apprentice wands and one extra
     * level for advanced and master wands (this generally seems to give about the right weight to potency
     * modifiers). This is public static because it is useful in a variety of places. */
    public static int getStandardBonusAmplifier(float potencyModifier){
        return (int)((potencyModifier - 1) / 0.4);
    }

    /** Spawns buff particles around the caster. Override to add a custom particle effect. Only called client-side. */
    protected void spawnParticles(World world, LivingEntity caster, SpellModifiers modifiers){

        for(int i = 0; i < particleCount; i++){
            double x = caster.prevX + world.random.nextDouble() * 2 - 1;
            double y = caster.prevY + caster.getStandingEyeHeight() - 0.5 + world.random.nextDouble();
            double z = caster.prevZ + world.random.nextDouble() * 2 - 1;
            ParticleBuilder.create(WizardryParticles.SPARKLE).pos(x, y, z).velocity(0, 0.1, 0).color(r, g, b).spawn(world);
        }
        // TODO: BUFF PARTICLES
        //ParticleBuilder.create(Type.BUFF).entity(caster).clr(r, g, b).spawn(world);
    }
}
