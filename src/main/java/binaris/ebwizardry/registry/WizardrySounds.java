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

    private static SoundEvent registerSound(String name){
        Identifier temp = new Identifier("%s:%s".formatted(Wizardry.MODID, name));
        return Registry.register(Registries.SOUND_EVENT, temp, SoundEvent.of(temp));
    }

    public static void register(){}
}
