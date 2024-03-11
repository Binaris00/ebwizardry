package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.client.particle.*;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class WizardryParticles {
    public static DefaultParticleType BUFF = FabricParticleTypes.simple();
    public static DefaultParticleType LIGHTNING = FabricParticleTypes.simple();
    /** Animated flame.<p></p><b>Defaults:</b><br>Lifetime: 12-16 ticks<br> */
    public static DefaultParticleType MAGIC_FIRE = register("magic_fire", ParticleMagicFire.MagicFireFactory::new);
    /** Animated sparkle particle.<p></p><b>Defaults:</b><<br>Lifetime: 48-60 ticks<br>Colour: white */
    public static DefaultParticleType SPARKLE = register("sparkle", ParticleSparkle.SparkleFactory::new);
    /** Spiral particle, like potions.<p></p><b>Defaults:</b><br>Lifetime: 8-40 ticks<br>Colour: white */
    public static DefaultParticleType DARK_MAGIC = register("dark_magic", ParticleDarkMagic.DarkMagicFactory::new);
    public static DefaultParticleType VINE_LEAF = FabricParticleTypes.simple();
    public static DefaultParticleType VINE = FabricParticleTypes.simple();
    /** Snowflake particle.<p></p><b>Defaults:</b><br>Lifetime: 40-50 ticks<br>Velocity: (0, -0.02, 0) */
    public static DefaultParticleType SNOW = register("snow", ParticleSnow.SnowFactory::new);
    public static DefaultParticleType SCORCH = FabricParticleTypes.simple();
    public static DefaultParticleType PATH = FabricParticleTypes.simple();
    /** Single leaf.<p></p><b>Defaults:</b><br>Lifetime: 10-15 ticks<br>Velocity: (0, -0.03, 0)
     * <br>Colour: green/brown */
    public static DefaultParticleType LEAF = register("leaf", ParticleLeaf.LeafFactory::new);
    /** Small shard of ice.<p></p><b>Defaults:</b><br>Lifetime: 8-40 ticks<br>Gravity: true */
    public static DefaultParticleType ICE = register("ice", ParticleIce.IceFactory::new);
    /** Large, thick cloud.<p></p><b>Defaults:</b><br>Lifetime: 48-60 ticks<br> Colour: dark grey */
    public static DefaultParticleType CLOUD = register("cloud", ParticleCloud.CloudFactory::new);
    public static DefaultParticleType MAGIC_BUBBLE = FabricParticleTypes.simple();
    public static DefaultParticleType BEAM = FabricParticleTypes.simple();
    public static DefaultParticleType SPARK = FabricParticleTypes.simple();
    public static DefaultParticleType DUST = register("dust", ParticleDust.DustFactory::new);

    public static void registryParticlesClient() {
        ParticleFactoryRegistry factoryRegistry = ParticleFactoryRegistry.getInstance();
        ParticleWizardry.FACTORIES.forEach(factoryRegistry::register);
    }

    /**
     * Internal method to add a particle type to the registry.
     * Only used in this mod with the specific wizardry id.
     * If you want to add a particle type to the registry and Factories,
     * use {@link ParticleWizardry#register(String, String, ParticleFactoryRegistry.PendingParticleFactory)}.
     */
    private static DefaultParticleType register(String name, ParticleFactoryRegistry.PendingParticleFactory<DefaultParticleType> constructor) {
        var particle = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, name), FabricParticleTypes.simple());
        ParticleWizardry.FACTORIES.put(particle, constructor);
        return particle;
    }
}
