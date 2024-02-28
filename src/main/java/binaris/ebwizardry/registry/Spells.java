package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.constant.Element;
import binaris.ebwizardry.constant.SpellType;
import binaris.ebwizardry.constant.Tier;
import binaris.ebwizardry.entity.living.BlazeMinionEntity;
import binaris.ebwizardry.entity.living.IceWraithEntity;
import binaris.ebwizardry.entity.living.ZombieMinionEntity;
import binaris.ebwizardry.entity.projectile.*;
import binaris.ebwizardry.spell.*;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

import static binaris.ebwizardry.Wizardry.REGISTRIES_SPELL;

public abstract class Spells {

    public static Spell NONE;
    public static Spell MAGIC_MISSILE;
    public static Spell SNOWBALL;
    public static Spell THUNDERBOLT;
    public static Spell ARC;
    public static Spell DART;
    public static Spell HEAL;
    public static Spell FIRE_BALL;
    public static Spell IGNITE;
    public static Spell FIRE_BOLT;
    public static Spell HOMING_SPARK;
    public static Spell SPARK_BOMB;
    public static Spell FIRE_BOMB;
    public static Spell ICE_SHARD;
    public static Spell POISON_BOMB;
    public static Spell LIGHTNING_ARROW;
    public static Spell SMOKE_BOMB;
    public static Spell ICE_CHARGE;
    public static Spell ICE_LANCE;
    public static Spell FIRE_RESISTANCE;
    public static Spell AGILITY;
    public static Spell FIRE_SKIN;
    public static Spell ICE_SHROUD;
    public static Spell STATIC_AURA;
    public static Spell WATER_BREATHING;
    public static Spell INVISIBILITY;
    public static Spell FONT_OF_VITALITY;
    public static Spell DARK_VISION;
    public static Spell SUMMON_ZOMBIE;
    public static Spell SUMMON_BLAZE;
    public static Spell SUMMON_ICE_WRAITH;
    public static Spell FREEZE;
    public static Spell FLAME_RAY;
    public static Spell FROST_RAY;
    public static Spell SUMMON_SNOW_GOLEM;
    public static Spell LIFE_DRAIN;

    public static void registry(){
        NONE = registrySpell("none", new None().createProperties(Tier.NOVICE, Element.MAGIC, SpellType.UTILITY, 0, 0, 0));

        MAGIC_MISSILE = registrySpell("magic_missile", new SpellArrow<>("magic_missile", EntityMagicMissile::new).createProperties(Tier.NOVICE, Element.MAGIC, SpellType.PROJECTILE, 5, 0, 5)
                .addProperties(Spell.RANGE, 18)
                .addProperties(Spell.DAMAGE, 3));

        IGNITE = registrySpell("ignite", new Ignite().createProperties(Tier.NOVICE, Element.FIRE, SpellType.PROJECTILE, 5, 0, 10)
                .addProperties(Spell.RANGE, 10)
                .addProperties(Spell.BURN_DURATION, 10));

        FREEZE = registrySpell("freeze", new Freeze().createProperties(Tier.NOVICE, Element.ICE, SpellType.ATTACK, 5, 0, 10)
                .addProperties(Spell.RANGE, 10)
                .addProperties(Spell.DAMAGE, 3)
                .addProperties(Spell.EFFECT_DURATION, 200)
                .addProperties(Spell.EFFECT_STRENGTH, 1));

        SNOWBALL = registrySpell("snowball", new SpellThrowable<>("snowball", SnowballEntity::new).createProperties(Tier.NOVICE, Element.ICE, SpellType.PROJECTILE, 1, 0, 1)
                .addProperties(Spell.RANGE, 15));

        ARC = registrySpell("arc", new Arc().createProperties(Tier.NOVICE, Element.LIGHTNING, SpellType.ATTACK, 5, 0, 15)
                .addProperties(Spell.RANGE, 3)
                .addProperties(Spell.DAMAGE, 8));

        THUNDERBOLT = registrySpell("thunderbolt", new SpellProjectile<>("thunderbolt", EntityThunderbolt::new).createProperties(Tier.APPRENTICE, Element.LIGHTNING, SpellType.PROJECTILE, 10, 0, 15)
                .addProperties(Spell.RANGE, 12)
                .addProperties(Spell.DAMAGE, 3)
                .addProperties(Spell.KNOCKBACK_STRENGTH, 0.2));

        //TODO: Snare spell

        DART = registrySpell("dart", new SpellArrow<>("dart", EntityDart::new).createProperties(Tier.NOVICE, Element.EARTH, SpellType.PROJECTILE, 5, 0, 10)
                .addProperties(Spell.RANGE, 15)
                .addProperties(Spell.DAMAGE, 4)
                .addProperties(Spell.EFFECT_STRENGTH, 1)
                .addProperties(Spell.EFFECT_DURATION, 200));

        // TODO: Light spell

        HEAL = registrySpell("heal", new Heal().createProperties(Tier.APPRENTICE, Element.HEALING, SpellType.DEFENCE, 5, 0, 20)
                .addProperties(Spell.HEALTH, 4));

        FLAME_RAY = registrySpell("flame_ray", new FlameRay().createProperties(Tier.APPRENTICE, Element.FIRE, SpellType.ATTACK, 5, 0, 0)
                .addProperties(Spell.RANGE, 10)
                .addProperties(Spell.DAMAGE, 3)
                .addProperties(Spell.BURN_DURATION, 10));

        FIRE_BALL = registrySpell("fire_ball", new SpellProjectile<>("fire_ball", EntityMagicFireball::new).createProperties(Tier.APPRENTICE, Element.FIRE, SpellType.PROJECTILE, 10, 0, 15)
                .addProperties(Spell.RANGE, 20)
                .addProperties(Spell.DAMAGE, 5)
                .addProperties(Spell.BURN_DURATION, 5));

        FIRE_BOMB = registrySpell("fire_bomb", new SpellProjectile<>("fire_bomb", EntityFireBomb::new).createProperties(Tier.APPRENTICE, Element.FIRE, SpellType.PROJECTILE, 15, 0, 25)
                // Temporal properties for testing
                .addProperties(Spell.RANGE, 10)
                .addProperties(Spell.DIRECT_DAMAGE, 5)
                .addProperties(Spell.SPLASH_DAMAGE, 3)
                .addProperties(Spell.BLAST_RADIUS, 3)
                .addProperties(Spell.BURN_DURATION, 7));

        // TODO: Fire Sigill spell

        FIRE_BOLT = registrySpell("fire_bolt", new SpellProjectile<>("fire_bolt", EntityFireBolt::new).createProperties(Tier.APPRENTICE, Element.FIRE, SpellType.PROJECTILE, 10, 0, 10)
                .addProperties(Spell.RANGE, 15)
                .addProperties(Spell.DAMAGE, 5)
                .addProperties(Spell.BURN_DURATION, 5));

        ICE_SHARD = registrySpell("ice_shard", new SpellArrow<>("ice_shard", EntityIceShard::new).createProperties(Tier.APPRENTICE, Element.ICE, SpellType.PROJECTILE, 10, 0, 10)
                .addProperties(Spell.RANGE, 15)
                .addProperties(Spell.DAMAGE, 6)
                .addProperties(Spell.EFFECT_DURATION, 200)
                .addProperties(Spell.EFFECT_STRENGTH, 0));

        ICE_CHARGE = registrySpell("ice_charge", new SpellProjectile<>("ice_charge", EntityIceCharge::new).createProperties(Tier.ADVANCED, Element.ICE, SpellType.PROJECTILE, 20, 0, 30)
                .addProperties(Spell.RANGE, 15)
                .addProperties(Spell.DAMAGE, 4)
                .addProperties(Spell.EFFECT_RADIUS, 3)
                .addProperties(Spell.DIRECT_EFFECT_DURATION, 120)
                .addProperties(Spell.DIRECT_EFFECT_STRENGTH, 1)
                .addProperties(Spell.SPLASH_EFFECT_DURATION, 100)
                .addProperties(Spell.SPLASH_EFFECT_STRENGTH, 0)
                .addProperties(EntityIceCharge.ICE_SHARDS, 10));

        HOMING_SPARK = registrySpell("homing_spark", new SpellProjectile<>("homing_spark", EntitySpark::new).createProperties(Tier.APPRENTICE, Element.LIGHTNING, SpellType.PROJECTILE, 10, 0, 20)
                .addProperties(Spell.RANGE, 25)
                .addProperties(Spell.DAMAGE, 6)
                .addProperties(Spell.SEEKING_STRENGTH, 5));

        LIGHTNING_ARROW = registrySpell("lightning_arrow", new SpellArrow<>("lightning_arrow", EntityLightningArrow::new).createProperties(Tier.APPRENTICE, Element.LIGHTNING, SpellType.PROJECTILE, 15, 0, 20)
                .addProperties(Spell.RANGE, 25)
                .addProperties(Spell.DAMAGE, 7));

        SPARK_BOMB = registrySpell("spark_bomb" , new SpellProjectile<>("spark_bomb", EntitySparkBomb::new).createProperties(Tier.APPRENTICE, Element.LIGHTNING, SpellType.PROJECTILE, 15, 0, 25)
                // Temporal properties for testing
                .addProperties(Spell.RANGE, 10)
                .addProperties(Spell.MAX_SECONDARY_TARGETS, 4)
                .addProperties(Spell.DIRECT_DAMAGE, 6)
                .addProperties(Spell.EFFECT_RADIUS, 5)
                .addProperties(Spell.SPLASH_DAMAGE, 4));

        POISON_BOMB = registrySpell("poison_bomb", new SpellProjectile<>("poison_bomb", EntityPoisonBomb::new).createProperties(Tier.APPRENTICE, Element.EARTH, SpellType.PROJECTILE, 15, 0, 25)
                // Temporal properties for testing
                .addProperties(Spell.RANGE, 10)
                .addProperties(Spell.DIRECT_DAMAGE, 5)
                .addProperties(Spell.SPLASH_DAMAGE, 3)
                .addProperties(Spell.EFFECT_RADIUS, 3)
                .addProperties(Spell.DIRECT_EFFECT_DURATION, 120)
                .addProperties(Spell.DIRECT_EFFECT_STRENGTH, 1)
                .addProperties(Spell.SPLASH_EFFECT_DURATION, 100)
                .addProperties(Spell.SPLASH_EFFECT_STRENGTH, 1));
        SMOKE_BOMB = registrySpell("smoke_bomb", new SpellProjectile<>("smoke_bomb", EntitySmokeBomb::new).createProperties(Tier.APPRENTICE, Element.FIRE, SpellType.PROJECTILE, 15, 0, 25)
                .addProperties(Spell.RANGE, 10)
                .addProperties(Spell.EFFECT_RADIUS, 3)
                .addProperties(Spell.EFFECT_DURATION, 120));
        ICE_LANCE = registrySpell("ice_lance", new SpellArrow<>("ice_lance", EntityIceLance::new).createProperties(Tier.ADVANCED, Element.ICE, SpellType.PROJECTILE, 20, 5, 20)
                .addProperties(Spell.RANGE, 15)
                .addProperties(Spell.DAMAGE, 15)
                .addProperties(Spell.EFFECT_DURATION, 300)
                .addProperties(Spell.EFFECT_STRENGTH, 0));

        FIRE_RESISTANCE = registrySpell("fire_resistance", new SpellBuff("fire_resistance", 1, 0.5f, 0, () -> StatusEffects.FIRE_RESISTANCE).soundValues(0.7f, 1.2f, 0.4f)
                .createProperties(Tier.ADVANCED, Element.FIRE, SpellType.DEFENCE, 20, 15, 80)
                .addProperties(SpellBuff.getDurationKey("fire_resistance"), 600)
                .addProperties(SpellBuff.getStrengthKey("fire_resistance"), 0));

        AGILITY = registrySpell("agility", new SpellBuff("agility", 0.4f, 1.0f, 0.8f, () -> StatusEffects.SPEED, () -> StatusEffects.JUMP_BOOST).soundValues(0.7f, 1.2f, 0.4f)
                .createProperties(Tier.APPRENTICE, Element.SORCERY, SpellType.BUFF, 20, 0, 40)
                .addProperties(SpellBuff.getDurationKey("speed"), 600)
                .addProperties(SpellBuff.getStrengthKey("speed"), 1)
                .addProperties(SpellBuff.getDurationKey("jump_boost"), 600)
                .addProperties(SpellBuff.getStrengthKey("jump_boost"), 1));
        FIRE_SKIN = registrySpell("fire_skin", new SpellBuff("fireskin", 1, 0.5f, 0, () -> WizardryEffects.FIRE_SKIN)
                .createProperties(Tier.ADVANCED, Element.FIRE, SpellType.DEFENCE, 40, 0, 250)
                .addProperties(SpellBuff.getDurationKey("fire_skin"), 600)
                .addProperties(SpellBuff.getStrengthKey("fire_skin"), 0)
                .addProperties(Spell.BURN_DURATION, 5));
        ICE_SHROUD = registrySpell("ice_shroud", new SpellBuff("ice_shroud", 0.3f, 0.5f, 1, () -> WizardryEffects.ICE_SHROUD).soundValues(1, 1.6f, 0.4f)
                .createProperties(Tier.ADVANCED, Element.ICE, SpellType.DEFENCE, 40, 0, 250)
                .addProperties(SpellBuff.getDurationKey("ice_shroud"), 600)
                .addProperties(SpellBuff.getStrengthKey("ice_shroud"), 0)
                .addProperties(Spell.EFFECT_DURATION, 100)
                .addProperties(Spell.EFFECT_STRENGTH, 0));
        STATIC_AURA = registrySpell("static_aura", new SpellBuff("static_aura", 0, 0.5f, 0.7f, () -> WizardryEffects.STATIC_AURA).soundValues(1, 1.6f, 0.4f)
                .createProperties(Tier.ADVANCED, Element.LIGHTNING, SpellType.DEFENCE, 40, 0, 250)
                .addProperties(SpellBuff.getDurationKey("static_aura"), 600)
                .addProperties(SpellBuff.getStrengthKey("static_aura"), 0)
                .addProperties(Spell.DAMAGE, 4));
        WATER_BREATHING = registrySpell("water_breathing", new SpellBuff("water_breathing", 0.3f, 0.3f, 1, () -> StatusEffects.WATER_BREATHING).soundValues(0.7f, 1.2f, 0.4f)
                .createProperties(Tier.ADVANCED, Element.EARTH, SpellType.BUFF, 30, 15, 250)
                .addProperties(SpellBuff.getDurationKey("water_breathing"), 1200)
                .addProperties(SpellBuff.getStrengthKey("water_breathing"), 0));
        INVISIBILITY = registrySpell("invisibility", new SpellBuff("invisibility", .7f, 1, 1, () -> StatusEffects.INVISIBILITY).soundValues(0.7f, 1.2f, 0.4f)
                .createProperties(Tier.ADVANCED, Element.SORCERY, SpellType.BUFF, 35, 15, 200)
                .addProperties(SpellBuff.getDurationKey("invisibility"), 600)
                .addProperties(SpellBuff.getStrengthKey("invisibility"), 0));
        FONT_OF_VITALITY = registrySpell("font_of_vitality", new SpellBuff("font_of_vitality", 1, 0.8f, 0.3f, () -> StatusEffects.ABSORPTION, () -> StatusEffects.REGENERATION).soundValues(0.7f, 1.2f, 0.4f)).createProperties(Tier.ADVANCED, Element.HEALING, SpellType.UTILITY, 40, 0, 200)
                .createProperties(Tier.MASTER, Element.HEALING, SpellType.DEFENCE, 75, 20, 300)
                .addProperties(SpellBuff.getDurationKey("absorption"), 1200)
                .addProperties(SpellBuff.getStrengthKey("absorption"), 1)
                .addProperties(SpellBuff.getDurationKey("regeneration"), 300)
                .addProperties(SpellBuff.getStrengthKey("regeneration"), 1);
        DARK_VISION = registrySpell("dark_vision", new SpellBuff("dark_vision", 0, 0.4f, 0.7f, () -> StatusEffects.NIGHT_VISION).soundValues(0.7f, 1.2f, 0.4f)
                .createProperties(Tier.APPRENTICE, Element.EARTH, SpellType.BUFF, 20, 0, 40)
                .addProperties(SpellBuff.getDurationKey("night_vision"), 900)
                .addProperties(SpellBuff.getStrengthKey("night_vision"), 0));
        SUMMON_ZOMBIE = registrySpell("summon_zombie", new SpellMinion<>("summon_vision", ZombieMinionEntity::new).createProperties(Tier.NOVICE, Element.NECROMANCY, SpellType.MINION, 10, 0, 40))
                .addProperties(SpellMinion.MINION_LIFETIME, 600)
                .addProperties(SpellMinion.MINION_COUNT, 1)
                .addProperties(SpellMinion.SUMMON_RADIUS, 2);
        SUMMON_BLAZE = registrySpell("summon_blaze", new SpellMinion<>("summon_blaze", BlazeMinionEntity::new).soundValues(1, 1.1f, 0.2f)
                .createProperties(Tier.ADVANCED, Element.FIRE, SpellType.MINION, 40, 10, 200))
                .addProperties(SpellMinion.MINION_LIFETIME, 600)
                .addProperties(SpellMinion.MINION_COUNT, 1)
                .addProperties(SpellMinion.SUMMON_RADIUS, 2);
        SUMMON_ICE_WRAITH = registrySpell("summon_ice_wraith", new SpellMinion<>("summon_ice_wraith", IceWraithEntity::new)).soundValues(1, 1.1f, 0.2f)
                .createProperties(Tier.ADVANCED, Element.ICE, SpellType.MINION, 40, 10, 200)
                .addProperties(SpellMinion.MINION_LIFETIME, 600)
                .addProperties(SpellMinion.MINION_COUNT, 1)
                .addProperties(SpellMinion.SUMMON_RADIUS, 2);
        FROST_RAY = registrySpell("frost_ray", new FrostRay().createProperties(Tier.APPRENTICE, Element.ICE, SpellType.ATTACK, 5, 0, 0)
                .addProperties(Spell.RANGE, 10)
                .addProperties(Spell.DAMAGE, 3)
                .addProperties(Spell.EFFECT_DURATION, 200)
                .addProperties(Spell.EFFECT_STRENGTH, 0));
        SUMMON_SNOW_GOLEM = registrySpell("summon_snow_golem", new SummonSnowGolem().createProperties(Tier.NOVICE, Element.ICE, SpellType.MINION, 15, 0, 20)
                .addProperties(SpellMinion.SUMMON_RADIUS, 2));
        LIFE_DRAIN = registrySpell("life_drain", new LifeDrain().createProperties(Tier.APPRENTICE, Element.NECROMANCY, SpellType.ATTACK, 10, 0, 0)
                .addProperties(Spell.RANGE, 10)
                .addProperties(Spell.DAMAGE, 2)
                .addProperties(LifeDrain.HEAL_FACTOR, 0.35F));
    }


    private static Spell registrySpell(String name, Spell spell){
        return Registry.register(REGISTRIES_SPELL, new Identifier(Wizardry.MODID, name), spell);
    }

    /**Return all the spells except for {@link None}*/
    public static List<Spell> getAllSpells(){
        List<Spell> temp = new java.util.ArrayList<>(REGISTRIES_SPELL.stream().toList());
        temp.remove(NONE);
        return temp;
    }
}
