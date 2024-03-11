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
    /** Helical animated 'buffing' particle.<p></p><b>Defaults:</b><br>Lifetime: 15 ticks
     * <br>Velocity: (0, 0.27, 0)<br>Colour: white */
    public static DefaultParticleType BUFF = register("buff", ParticleBuff.BuffFactory::new);
    public static DefaultParticleType LIGHTNING = register("lightning", ParticleLightning.LightningFactory::new);
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
    /** 3D-rendered light-beam particle.<p></p><b>Defaults:</b><br>Lifetime: 1 tick<br> Colour: white */
    @Deprecated
    public static DefaultParticleType BEAM = register("beam", ParticleBeam.BeamFactory::new);
    public static DefaultParticleType SPARK = register("spark", ParticleSpark.SparkFactory::new);
    /** Single pixel particle.<p></p><b>Defaults:</b><br>Lifetime: 16-80 ticks<br>Colour: white */
    public static DefaultParticleType DUST = register("dust", ParticleDust.DustFactory::new);
    /** Rapid flash, like fireworks.<p></p><b>Defaults:</b><br>Lifetime: 6 ticks<br>Colour: white */
    public static DefaultParticleType FLASH = register("flash", ParticleFlash.FlashFactory::new);
    /** Particle that looks like the guardian's beam attack.<p></p><b>Defaults:</b><br>Lifetime: 1 tick */
    public static DefaultParticleType GUARDIAN_BEAM = register("guardian_beam", ParticleGuardianBeam.GuardianBeamFactory::new);

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
