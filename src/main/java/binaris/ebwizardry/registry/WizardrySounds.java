package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public abstract class WizardrySounds {
    public static final SoundEvent ENTITY_SPARK_BOMB_THROW = registerSound("entity.spark_bomb.throw");
    public static final SoundEvent ENTITY_SPARK_BOMB_HIT = registerSound("entity.spark_bomb.hit");
    public static final SoundEvent ENTITY_SPARK_BOMB_HIT_BLOCK = registerSound("entity.spark_bomb.hit_block");
    public static final SoundEvent ENTITY_SPARK_BOMB_CHAIN = registerSound("entity.spark_bomb.chain");
    public static final SoundEvent ENTITY_FIREBOMB_THROW = registerSound("entity.firebomb.throw");
    public static final SoundEvent ENTITY_FIREBOMB_SMASH = registerSound("entity.firebomb.smash");
    public static final SoundEvent ENTITY_FIREBOMB_FIRE = registerSound("entity.firebomb.fire");
    public static final SoundEvent ENTITY_POISON_BOMB_THROW = registerSound("entity.poison_bomb.throw");
    public static final SoundEvent ENTITY_POISON_BOMB_SMASH = registerSound("entity.poison_bomb.smash");
    public static final SoundEvent ENTITY_POISON_BOMB_POISON = registerSound("entity.poison_bomb.poison");
    public static final SoundEvent ENTITY_SMOKE_BOMB_THROW = registerSound("entity.smoke_bomb.throw");
    public static final SoundEvent ENTITY_SMOKE_BOMB_SMASH = registerSound("entity.smoke_bomb.smash");
    public static final SoundEvent ENTITY_SMOKE_BOMB_SMOKE = registerSound("entity.smoke_bomb.smoke");
    public static final SoundEvent ENTITY_THUNDERBOLT_HIT = registerSound("entity.thunderbolt.hit");
    public static final SoundEvent ENTITY_MAGIC_MISSILE_HIT = registerSound("entity.magic_missile.hit");
    private static SoundEvent registerSound(String name){
        Identifier temp = new Identifier("%s:%s".formatted(Wizardry.MODID, name));
        return Registry.register(Registries.SOUND_EVENT, temp, SoundEvent.of(temp));
    }

    public static void register(){}
}
