package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public abstract class WizardrySounds {
    @Deprecated
    public static final SoundCategory SPELLS = SoundCategory.MASTER;

    public static final SoundEvent BLOCK_ARCANE_WORKBENCH_SPELLBIND = registerSound("block.arcane_workbench.bind_spell");
    public static final SoundEvent BLOCK_PEDESTAL_ACTIVATE = 		registerSound("block.pedestal.activate");
    public static final SoundEvent BLOCK_PEDESTAL_CONQUER = 		registerSound("block.pedestal.conquer");
    public static final SoundEvent BLOCK_LECTERN_LOCATE_SPELL = 	registerSound("block.lectern.locate_spell");
    public static final SoundEvent BLOCK_RECEPTACLE_IGNITE = 		registerSound("block.receptacle.ignite");
    public static final SoundEvent BLOCK_IMBUEMENT_ALTAR_IMBUE = 	registerSound("block.imbuement_altar.imbue");

    public static final SoundEvent ITEM_WAND_SWITCH_SPELL = 		registerSound("item.wand.switch_spell");
    public static final SoundEvent ITEM_WAND_LEVELUP = 				registerSound("item.wand.levelup");
    public static final SoundEvent ITEM_WAND_MELEE = 				registerSound("item.wand.melee");
    public static final SoundEvent ITEM_WAND_CHARGEUP = 			registerSound("item.wand.chargeup");
    public static final SoundEvent ITEM_ARMOUR_EQUIP_SILK = 		registerSound("item.armour.equip_silk");
    public static final SoundEvent ITEM_ARMOUR_EQUIP_SAGE = 		registerSound("item.armour.equip_sage");
    public static final SoundEvent ITEM_ARMOUR_EQUIP_BATTLEMAGE = 	registerSound("item.armour.equip_battlemage");
    public static final SoundEvent ITEM_ARMOUR_EQUIP_WARLOCK = 		registerSound("item.armour.equip_warlock");
    public static final SoundEvent ITEM_PURIFYING_ELIXIR_DRINK = 	registerSound("item.purifying_elixir.drink");
    public static final SoundEvent ITEM_MANA_FLASK_USE = 			registerSound("item.mana_flask.use");
    public static final SoundEvent ITEM_MANA_FLASK_RECHARGE = 		registerSound("item.mana_flask.recharge");
    public static final SoundEvent ITEM_FLAMECATCHER_SHOOT = 		registerSound("item.flamecatcher.shoot");
    public static final SoundEvent ITEM_FLAMECATCHER_FLAME = 		registerSound("item.flamecatcher.flame");

    public static final SoundEvent ENTITY_BLACK_HOLE_AMBIENT = 		registerSound("entity.black_hole.ambient");
    public static final SoundEvent ENTITY_BLACK_HOLE_VANISH = 		registerSound("entity.black_hole.vanish");
    public static final SoundEvent ENTITY_BLACK_HOLE_BREAK_BLOCK = 	registerSound("entity.black_hole.break_block");
    public static final SoundEvent ENTITY_BLIZZARD_AMBIENT = 		registerSound("entity.blizzard.ambient");
    public static final SoundEvent ENTITY_BOULDER_ROLL = 			registerSound("entity.boulder.roll");
    public static final SoundEvent ENTITY_BOULDER_LAND = 			registerSound("entity.boulder.land");
    public static final SoundEvent ENTITY_BOULDER_HIT = 			registerSound("entity.boulder.hit");
    public static final SoundEvent ENTITY_BOULDER_BREAK_BLOCK = 	registerSound("entity.boulder.break_block");
    public static final SoundEvent ENTITY_BUBBLE_POP = 				registerSound("entity.bubble.pop");
    public static final SoundEvent ENTITY_DECAY_AMBIENT = 			registerSound("entity.decay.ambient");
    public static final SoundEvent ENTITY_ENTRAPMENT_AMBIENT = 		registerSound("entity.entrapment.ambient");
    public static final SoundEvent ENTITY_ENTRAPMENT_VANISH = 		registerSound("entity.entrapment.vanish");
    public static final SoundEvent ENTITY_FIRE_RING_AMBIENT = 		registerSound("entity.fire_ring.ambient");
    public static final SoundEvent ENTITY_FIRE_SIGIL_TRIGGER = 		registerSound("entity.fire_sigil.trigger");
    public static final SoundEvent ENTITY_FORCEFIELD_AMBIENT = 		registerSound("entity.forcefield.ambient");
    public static final SoundEvent ENTITY_FORCEFIELD_DEFLECT = 		registerSound("entity.forcefield.deflect");
    public static final SoundEvent ENTITY_FROST_SIGIL_TRIGGER = 	registerSound("entity.frost_sigil.trigger");
    public static final SoundEvent ENTITY_HAMMER_ATTACK = 			registerSound("entity.hammer.attack");
    public static final SoundEvent ENTITY_HAMMER_EXPLODE = 			registerSound("entity.hammer.explode");
    public static final SoundEvent ENTITY_HAMMER_THROW = 			registerSound("entity.hammer.throw");
    public static final SoundEvent ENTITY_HAMMER_LAND = 			registerSound("entity.hammer.land");
    public static final SoundEvent ENTITY_HEAL_AURA_AMBIENT = 		registerSound("entity.heal_aura.ambient");
    public static final SoundEvent ENTITY_ICE_BARRIER_DEFLECT = 	registerSound("entity.ice_barrier.deflect");
    public static final SoundEvent ENTITY_ICE_BARRIER_EXTEND = 		registerSound("entity.ice_barrier.extend");
    public static final SoundEvent ENTITY_ICE_SPIKE_EXTEND = 		registerSound("entity.ice_spike.extend");
    public static final SoundEvent ENTITY_LIGHTNING_SIGIL_TRIGGER = registerSound("entity.lightning_sigil.trigger");
    public static final SoundEvent ENTITY_METEOR_FALLING = 			registerSound("entity.meteor.falling");
    public static final SoundEvent ENTITY_RADIANT_TOTEM_AMBIENT = 	registerSound("entity.radiant_totem.ambient");
    public static final SoundEvent ENTITY_RADIANT_TOTEM_VANISH = 	registerSound("entity.radiant_totem.vanish");
    public static final SoundEvent ENTITY_SHIELD_DEFLECT = 			registerSound("entity.shield.deflect");
    public static final SoundEvent ENTITY_TORNADO_AMBIENT = 		registerSound("entity.tornado.ambient");
    public static final SoundEvent ENTITY_WITHERING_TOTEM_AMBIENT = registerSound("entity.withering_totem.ambient");
    public static final SoundEvent ENTITY_WITHERING_TOTEM_EXPLODE = registerSound("entity.withering_totem.explode");
    public static final SoundEvent ENTITY_ZOMBIE_SPAWNER_SPAWN = 	registerSound("entity.zombie_spawner.spawn");

    public static final SoundEvent ENTITY_EVIL_WIZARD_AMBIENT = 	registerSound("entity.evil_wizard.ambient");
    public static final SoundEvent ENTITY_EVIL_WIZARD_HURT = 		registerSound("entity.evil_wizard.hurt");
    public static final SoundEvent ENTITY_EVIL_WIZARD_DEATH = 		registerSound("entity.evil_wizard.death");
    public static final SoundEvent ENTITY_ICE_GIANT_ATTACK = 		registerSound("entity.ice_giant.attack");
    public static final SoundEvent ENTITY_ICE_GIANT_DESPAWN = 		registerSound("entity.ice_giant.despawn");
    public static final SoundEvent ENTITY_ICE_WRAITH_AMBIENT = 		registerSound("entity.ice_wraith.ambient");
    public static final SoundEvent ENTITY_MAGIC_SLIME_ATTACK = 		registerSound("entity.magic_slime.attack");
    public static final SoundEvent ENTITY_MAGIC_SLIME_EXPLODE = 	registerSound("entity.magic_slime.explode");
    public static final SoundEvent ENTITY_MAGIC_SLIME_SPLAT = 		registerSound("entity.magic_slime.splat");
    public static final SoundEvent ENTITY_PHOENIX_AMBIENT = 		registerSound("entity.phoenix.ambient");
    public static final SoundEvent ENTITY_PHOENIX_BURN = 			registerSound("entity.phoenix.burn");
    public static final SoundEvent ENTITY_PHOENIX_FLAP = 			registerSound("entity.phoenix.flap");
    public static final SoundEvent ENTITY_PHOENIX_HURT = 			registerSound("entity.phoenix.hurt");
    public static final SoundEvent ENTITY_PHOENIX_DEATH = 			registerSound("entity.phoenix.death");
    public static final SoundEvent ENTITY_REMNANT_AMBIENT = 		registerSound("entity.remnant.ambient");
    public static final SoundEvent ENTITY_REMNANT_HURT = 			registerSound("entity.remnant.hurt");
    public static final SoundEvent ENTITY_REMNANT_DEATH = 			registerSound("entity.remnant.death");
    public static final SoundEvent ENTITY_SHADOW_WRAITH_AMBIENT = 	registerSound("entity.shadow_wraith.ambient");
    public static final SoundEvent ENTITY_SHADOW_WRAITH_NOISE = 	registerSound("entity.shadow_wraith.noise");
    public static final SoundEvent ENTITY_SHADOW_WRAITH_HURT = 		registerSound("entity.shadow_wraith.hurt");
    public static final SoundEvent ENTITY_SHADOW_WRAITH_DEATH = 	registerSound("entity.shadow_wraith.death");
    public static final SoundEvent ENTITY_SPIRIT_HORSE_VANISH = 	registerSound("entity.spirit_horse.vanish");
    public static final SoundEvent ENTITY_SPIRIT_WOLF_VANISH = 		registerSound("entity.spirit_wolf.vanish");
    public static final SoundEvent ENTITY_STORM_ELEMENTAL_AMBIENT = registerSound("entity.storm_elemental.ambient");
    public static final SoundEvent ENTITY_STORM_ELEMENTAL_BURN = 	registerSound("entity.storm_elemental.burn");
    public static final SoundEvent ENTITY_STORM_ELEMENTAL_WIND = 	registerSound("entity.storm_elemental.wind");
    public static final SoundEvent ENTITY_STORM_ELEMENTAL_HURT = 	registerSound("entity.storm_elemental.hurt");
    public static final SoundEvent ENTITY_STORM_ELEMENTAL_DEATH = 	registerSound("entity.storm_elemental.death");
    public static final SoundEvent ENTITY_STORMCLOUD_THUNDER = 		registerSound("entity.stormcloud.thunder");
    public static final SoundEvent ENTITY_STORMCLOUD_ATTACK = 		registerSound("entity.stormcloud.attack");
    public static final SoundEvent ENTITY_WIZARD_YES = 				registerSound("entity.wizard.yes");
    public static final SoundEvent ENTITY_WIZARD_NO = 				registerSound("entity.wizard.no");
    public static final SoundEvent ENTITY_WIZARD_AMBIENT = 			registerSound("entity.wizard.ambient");
    public static final SoundEvent ENTITY_WIZARD_TRADING = 			registerSound("entity.wizard.trading");
    public static final SoundEvent ENTITY_WIZARD_HURT = 			registerSound("entity.wizard.hurt");
    public static final SoundEvent ENTITY_WIZARD_DEATH = 			registerSound("entity.wizard.death");
    public static final SoundEvent ENTITY_WIZARD_HOHOHO = 			registerSound("entity.wizard.hohoho");

    public static final SoundEvent ENTITY_DARKNESS_ORB_HIT = 		registerSound("entity.darkness_orb.hit");
    public static final SoundEvent ENTITY_DART_HIT = 				registerSound("entity.dart.hit");
    public static final SoundEvent ENTITY_DART_HIT_BLOCK = 			registerSound("entity.dart.hit_block");
    public static final SoundEvent ENTITY_FIREBOLT_HIT = 			registerSound("entity.firebolt.hit");
    public static final SoundEvent ENTITY_FIREBOMB_THROW = 			registerSound("entity.firebomb.throw");
    public static final SoundEvent ENTITY_FIREBOMB_SMASH = 			registerSound("entity.firebomb.smash");
    public static final SoundEvent ENTITY_FIREBOMB_FIRE = 			registerSound("entity.firebomb.fire");
    public static final SoundEvent ENTITY_FLAMECATCHER_ARROW_HIT = 	registerSound("entity.flamecatcher_arrow.hit");
    public static final SoundEvent ENTITY_FORCE_ARROW_HIT = 		registerSound("entity.force_arrow.hit");
    public static final SoundEvent ENTITY_FORCE_ORB_HIT = 			registerSound("entity.force_orb.hit");
    public static final SoundEvent ENTITY_FORCE_ORB_HIT_BLOCK = 	registerSound("entity.force_orb.hit_block");
    public static final SoundEvent ENTITY_ICEBALL_HIT = 			registerSound("entity.iceball.hit");
    public static final SoundEvent ENTITY_ICE_CHARGE_SMASH = 		registerSound("entity.ice_charge.smash");
    public static final SoundEvent ENTITY_ICE_CHARGE_ICE = 			registerSound("entity.ice_charge.ice");
    public static final SoundEvent ENTITY_ICE_LANCE_SMASH = 		registerSound("entity.ice_lance.smash");
    public static final SoundEvent ENTITY_ICE_LANCE_HIT = 			registerSound("entity.ice_lance.hit");
    public static final SoundEvent ENTITY_ICE_SHARD_SMASH = 		registerSound("entity.ice_shard.smash");
    public static final SoundEvent ENTITY_ICE_SHARD_HIT = 			registerSound("entity.ice_shard.hit");
    public static final SoundEvent ENTITY_LIGHTNING_ARROW_HIT = 	registerSound("entity.lightning_arrow.hit");
    public static final SoundEvent ENTITY_LIGHTNING_DISC_HIT = 		registerSound("entity.lightning_disc.hit");
    //	public static final SoundEvent ENTITY_MAGIC_FIREBALL_HIT = 		registerSound("entity.magic_fireball.hit");
    public static final SoundEvent ENTITY_MAGIC_MISSILE_HIT = 		registerSound("entity.magic_missile.hit");
    public static final SoundEvent ENTITY_POISON_BOMB_THROW = 		registerSound("entity.poison_bomb.throw");
    public static final SoundEvent ENTITY_POISON_BOMB_SMASH = 		registerSound("entity.poison_bomb.smash");
    public static final SoundEvent ENTITY_POISON_BOMB_POISON = 		registerSound("entity.poison_bomb.poison");
    public static final SoundEvent ENTITY_SMOKE_BOMB_THROW = 		registerSound("entity.smoke_bomb.throw");
    public static final SoundEvent ENTITY_SMOKE_BOMB_SMASH = 		registerSound("entity.smoke_bomb.smash");
    public static final SoundEvent ENTITY_SMOKE_BOMB_SMOKE = 		registerSound("entity.smoke_bomb.smoke");
    public static final SoundEvent ENTITY_HOMING_SPARK_HIT = 		registerSound("entity.homing_spark.hit");
    public static final SoundEvent ENTITY_SPARK_BOMB_THROW = 		registerSound("entity.spark_bomb.throw");
    public static final SoundEvent ENTITY_SPARK_BOMB_HIT = 			registerSound("entity.spark_bomb.hit");
    public static final SoundEvent ENTITY_SPARK_BOMB_HIT_BLOCK = 	registerSound("entity.spark_bomb.hit_block");
    public static final SoundEvent ENTITY_SPARK_BOMB_CHAIN = 		registerSound("entity.spark_bomb.chain");
    public static final SoundEvent ENTITY_THUNDERBOLT_HIT = 		registerSound("entity.thunderbolt.hit");

    public static final SoundEvent SPELL_STATIC_AURA_RETALIATE = 	registerSound("spell.static_aura.retaliate");
    public static final SoundEvent SPELL_CURSE_OF_SOULBINDING_RETALIATE = registerSound("spell.curse_of_soulbinding.retaliate");
    public static final SoundEvent SPELL_TRANSPORTATION_TRAVEL = 	registerSound("spell.transportation.travel");

    public static final SoundEvent MISC_DISCOVER_SPELL = 			registerSound("misc.discover_spell");
    public static final SoundEvent MISC_BOOK_OPEN = 				registerSound("misc.book_open");
    public static final SoundEvent MISC_PAGE_TURN = 				registerSound("misc.page_turn");
    public static final SoundEvent MISC_FREEZE = 					registerSound("misc.freeze");
    public static final SoundEvent MISC_SPELL_FAIL = 				registerSound("misc.spell_fail");

    private static SoundEvent registerSound(String name){
        Identifier temp = new Identifier("%s:%s".formatted(Wizardry.MODID, name));
        return Registry.register(Registries.SOUND_EVENT, temp, SoundEvent.of(temp));
    }

    public static void register(){}
}
