package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public abstract class WizardryParticles {
    public static DefaultParticleType LIGHTNING = FabricParticleTypes.simple();
    public static DefaultParticleType MAGIC_FIRE = FabricParticleTypes.simple();

    public static void registry(){
        LIGHTNING = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "lightning"), LIGHTNING);
        MAGIC_FIRE = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "magic_fire"), MAGIC_FIRE);
    }

}
