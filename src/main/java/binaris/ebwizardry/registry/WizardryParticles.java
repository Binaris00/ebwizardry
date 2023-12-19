package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.client.particle.ParticleWizardry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class WizardryParticles {
    public static DefaultParticleType LIGHTNING = FabricParticleTypes.simple();
    public static DefaultParticleType MAGIC_FIRE = FabricParticleTypes.simple();
    public static DefaultParticleType SPARKLE = FabricParticleTypes.simple();

    public static void registryParticleTypes(){
        LIGHTNING = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "lightning"), LIGHTNING);
        MAGIC_FIRE = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "magic_fire"), MAGIC_FIRE);
        SPARKLE = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "sparkle"), SPARKLE);
    }

    public static void registryParticlesClient(){
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(LIGHTNING, ParticleWizardry.Factory::new);
        registry.register(MAGIC_FIRE, ParticleWizardry.Factory::new);
        registry.register(SPARKLE, ParticleWizardry.Factory::new);
    }

}
