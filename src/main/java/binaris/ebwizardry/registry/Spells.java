package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.constant.Element;
import binaris.ebwizardry.constant.SpellType;
import binaris.ebwizardry.constant.Tier;
import binaris.ebwizardry.entity.projectile.*;
import binaris.ebwizardry.spell.*;
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
    public static Spell DART;
    public static Spell HEAL;
    public static Spell FIRE_BALL;
    public static Spell FIRE_BOLT;
    public static Spell SPARK_BOMB;
    public static Spell FIRE_BOMB;
    public static Spell ICE_SHARD;
    public static Spell POISON_BOMB;
    public static Spell SMOKE_BOMB;


    public static void registry(){
        NONE = registrySpell("none", new None().createProperties(Tier.NOVICE, Element.MAGIC, SpellType.UTILITY, 0, 0, 0));

        MAGIC_MISSILE = registrySpell("magic_missile", new SpellArrow<>("magic_missile", EntityMagicMissile::new).createProperties(Tier.NOVICE, Element.MAGIC, SpellType.PROJECTILE, 5, 0, 5)
                .addProperties(Spell.RANGE, 18)
                .addProperties(Spell.DAMAGE, 3));

        // TODO: ignite spell

        // TODO: freeze spell

        SNOWBALL = registrySpell("snowball", new SpellThrowable<>("snowball", SnowballEntity::new).createProperties(Tier.NOVICE, Element.ICE, SpellType.PROJECTILE, 1, 0, 1)
                .addProperties(Spell.RANGE, 15));

        // TODO: Arc spell

        THUNDERBOLT = registrySpell("thunderbolt", new SpellProjectile<>("thunderbolt", EntityThunderbolt::new).createProperties(Tier.APPRENTICE, Element.LIGHTNING, SpellType.PROJECTILE, 10, 0, 15)
                .addProperties(Spell.RANGE, 12)
                .addProperties(Spell.DAMAGE, 3)
                .addProperties(Spell.KNOCKBACK_STRENGTH, 0.2));

        // TODO: Summon Zombie

        //TODO: Snare spell

        DART = registrySpell("dart", new SpellArrow<>("dart", EntityDart::new).createProperties(Tier.NOVICE, Element.EARTH, SpellType.PROJECTILE, 5, 0, 10)
                .addProperties(Spell.RANGE, 15)
                .addProperties(Spell.DAMAGE, 4)
                .addProperties(Spell.EFFECT_STRENGTH, 1)
                .addProperties(Spell.EFFECT_DURATION, 200));

        // TODO: Light spell

        HEAL = registrySpell("heal", new Heal().createProperties(Tier.APPRENTICE, Element.HEALING, SpellType.DEFENCE, 5, 0, 20)
                .addProperties(Spell.HEALTH, 4));

        // TODO: FlameRay spell

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
