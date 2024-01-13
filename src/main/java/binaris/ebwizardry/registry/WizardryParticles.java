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
    public static DefaultParticleType DARK_MAGIC = FabricParticleTypes.simple();
    public static DefaultParticleType VINE_LEAF = FabricParticleTypes.simple();
    public static DefaultParticleType VINE = FabricParticleTypes.simple();
    public static DefaultParticleType SNOW = FabricParticleTypes.simple();
    public static DefaultParticleType SCORCH = FabricParticleTypes.simple();
    public static DefaultParticleType PATH = FabricParticleTypes.simple();
    public static DefaultParticleType LEAF = FabricParticleTypes.simple();
    public static DefaultParticleType ICE = FabricParticleTypes.simple();
    public static DefaultParticleType CLOUD = FabricParticleTypes.simple();
    public static void registryParticleTypes(){
        LIGHTNING = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "lightning"), LIGHTNING);
        MAGIC_FIRE = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "magic_fire"), MAGIC_FIRE);
        SPARKLE = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "sparkle"), SPARKLE);
        DARK_MAGIC = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "dark_magic"), DARK_MAGIC);
        VINE_LEAF = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "vine_leaf"), VINE_LEAF);
        VINE = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "vine"), VINE);
        SNOW = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "snow"), SNOW);
        SCORCH = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "scorch"), SCORCH);
        PATH = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "path"), PATH);
        LEAF = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "leaf"), LEAF);
        ICE = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "ice"), ICE);
        CLOUD = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, "cloud"), CLOUD);
    }

    public static void registryParticlesClient(){
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(LIGHTNING, ParticleWizardry.WizardryFactory::new);
        registry.register(MAGIC_FIRE, ParticleWizardry.WizardryFactory::new);
        registry.register(SPARKLE, ParticleWizardry.WizardryFactory::new);
        registry.register(DARK_MAGIC, ParticleWizardry.WizardryFactory::new);
        registry.register(VINE_LEAF, ParticleWizardry.WizardryFactory::new);
        registry.register(VINE, ParticleWizardry.WizardryFactory::new);
        registry.register(SNOW, ParticleWizardry.WizardryFactory::new);
        registry.register(SCORCH, ParticleWizardry.WizardryFactory::new);
        registry.register(PATH, ParticleWizardry.WizardryFactory::new);
        registry.register(LEAF, ParticleWizardry.WizardryFactory::new);
        registry.register(ICE, ParticleWizardry.WizardryFactory::new);
        registry.register(CLOUD, ParticleWizardry.WizardryFactory::new);
    }
}
