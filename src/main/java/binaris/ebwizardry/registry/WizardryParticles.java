package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.client.particle.*;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.function.BiFunction;

import static binaris.ebwizardry.client.particle.ParticleWizardry.FACTORIES;

public class WizardryParticles {
    /** Helical animated 'buffing' particle.<p></p><b>Defaults:</b><br>Lifetime: 15 ticks
     * <br>Velocity: (0, 0.27, 0)<br>Colour: white */
    public static DefaultParticleType BUFF = register("buff", ParticleBuff.BuffFactory::createParticle);
    /** 3D-rendered lightning particle.<p></p><b>Defaults:</b><br>Lifetime: 3 ticks<br> Colour: blue */
    public static DefaultParticleType LIGHTNING = register("lightning", ParticleLightning.LightningFactory::createParticle);
    /** Animated flame.<p></p><b>Defaults:</b><br>Lifetime: 12-16 ticks<br> */
    public static DefaultParticleType MAGIC_FIRE = register("magic_fire", ParticleMagicFire.MagicFireFactory::createParticle);
    /** Animated sparkle particle.<p></p><b>Defaults:</b><<br>Lifetime: 48-60 ticks<br>Colour: white */
    public static DefaultParticleType SPARKLE = register("sparkle", ParticleSparkle.SparkleFactory::createParticle);
    /** Spiral particle, like potions.<p></p><b>Defaults:</b><br>Lifetime: 8-40 ticks<br>Colour: white */
    public static DefaultParticleType DARK_MAGIC = register("dark_magic", ParticleDarkMagic.DarkMagicFactory::createParticle);
    public static DefaultParticleType VINE_LEAF = FabricParticleTypes.simple();
    public static DefaultParticleType VINE = FabricParticleTypes.simple();
    /** Snowflake particle.<p></p><b>Defaults:</b><br>Lifetime: 40-50 ticks<br>Velocity: (0, -0.02, 0) */
    public static DefaultParticleType SNOW = register("snow", ParticleSnow.SnowFactory::createParticle);
    /** Scorch mark.<p></p><b>Defaults:</b><br>Lifetime: 100-140 ticks<br>Colour: black<br>Fade: black */
    public static DefaultParticleType SCORCH = register("scorch", ParticleScorch.ScorchFactory::createParticle);
    /** Soft-edged round particle.<p></p><b>Defaults:</b><br>Lifetime: 8-40 ticks<br>Colour: white */
    public static DefaultParticleType PATH = register("path", ParticlePath.PathFactory::createParticle);
    /** Single leaf.<p></p><b>Defaults:</b><br>Lifetime: 10-15 ticks<br>Velocity: (0, -0.03, 0)
     * <br>Colour: green/brown */
    public static DefaultParticleType LEAF = register("leaf", ParticleLeaf.LeafFactory::createParticle);
    /** Small shard of ice.<p></p><b>Defaults:</b><br>Lifetime: 8-40 ticks<br>Gravity: true */
    public static DefaultParticleType ICE = register("ice", ParticleIce.IceFactory::createParticle);
    /** Large, thick cloud.<p></p><b>Defaults:</b><br>Lifetime: 48-60 ticks<br> Colour: dark grey */
    public static DefaultParticleType CLOUD = register("cloud", ParticleCloud.CloudFactory::createParticle);
    /** Bubble that doesn't burst in air.<p></p><b>Defaults:</b><br>Lifetime: 8-40 ticks */
    public static DefaultParticleType MAGIC_BUBBLE = register("magic_bubble", ParticleMagicBubble.MagicBubbleFactory::createParticle);
    /** 3D-rendered light-beam particle.<p></p><b>Defaults:</b><br>Lifetime: 1 tick<br> Colour: white */
    @Deprecated
    public static DefaultParticleType BEAM = register("beam", ParticleBeam.BeamFactory::createParticle);
    /** Animated lightning particle.<p></p><b>Defaults:</b><br>Lifetime: 3 ticks */
    public static DefaultParticleType SPARK = register("spark", ParticleSpark.SparkFactory::createParticle);
    /** Single pixel particle.<p></p><b>Defaults:</b><br>Lifetime: 16-80 ticks<br>Colour: white */
    public static DefaultParticleType DUST = register("dust", ParticleDust.DustFactory::createParticle);
    /** Rapid flash, like fireworks.<p></p><b>Defaults:</b><br>Lifetime: 6 ticks<br>Colour: white */
    public static DefaultParticleType FLASH = register("flash", ParticleFlash.FlashFactory::createParticle);
    /** Particle that looks like the guardian's beam attack.<p></p><b>Defaults:</b><br>Lifetime: 1 tick */
    public static DefaultParticleType GUARDIAN_BEAM = register("guardian_beam", ParticleGuardianBeam.GuardianBeamFactory::createParticle);
    /** 2D lightning effect, normally on the ground.<p></p><b>Defaults:</b><br>Lifetime: 7 ticks
     * <br>Facing: up */
    public static DefaultParticleType LIGHTNING_PULSE = register("lightning_pulse", ParticleLightningPulse.LightningPulseFactory::createParticle);
    /** 3D-rendered expanding sphere.<p></p><b>Defaults:</b><<br>Lifetime: 6 ticks<br>Colour: white */
    public static DefaultParticleType SPHERE = register("sphere", ParticleSphere.SphereFactory::createParticle);

    public static void registryParticlesClient() {
        ParticleFactoryRegistry factoryRegistry = ParticleFactoryRegistry.getInstance();
        factoryRegistry.register(LEAF, ParticleLeaf.LeafFactory::new);
        factoryRegistry.register(BUFF, ParticleBuff.BuffFactory::new);
        factoryRegistry.register(LIGHTNING, ParticleLightning.LightningFactory::new);
        factoryRegistry.register(MAGIC_FIRE, ParticleMagicFire.MagicFireFactory::new);
        factoryRegistry.register(SPARKLE, ParticleSparkle.SparkleFactory::new);
        factoryRegistry.register(DARK_MAGIC, ParticleDarkMagic.DarkMagicFactory::new);
        factoryRegistry.register(SNOW, ParticleSnow.SnowFactory::new);
        factoryRegistry.register(SCORCH, ParticleScorch.ScorchFactory::new);
        factoryRegistry.register(PATH, ParticlePath.PathFactory::new);
        factoryRegistry.register(ICE, ParticleIce.IceFactory::new);
        factoryRegistry.register(CLOUD, ParticleCloud.CloudFactory::new);
        factoryRegistry.register(MAGIC_BUBBLE, ParticleMagicBubble.MagicBubbleFactory::new);
        factoryRegistry.register(BEAM, ParticleBeam.BeamFactory::new);
        factoryRegistry.register(SPARK, ParticleSpark.SparkFactory::new);
        factoryRegistry.register(DUST, ParticleDust.DustFactory::new);
        factoryRegistry.register(FLASH, ParticleFlash.FlashFactory::new);
        factoryRegistry.register(GUARDIAN_BEAM, ParticleGuardianBeam.GuardianBeamFactory::new);
        factoryRegistry.register(LIGHTNING_PULSE, ParticleLightningPulse.LightningPulseFactory::new);
        factoryRegistry.register(SPHERE, ParticleSphere.SphereFactory::new);

    }

    private static DefaultParticleType register(String name, BiFunction<ClientWorld, Vec3d, ParticleWizardry> factory){
        DefaultParticleType particle = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Wizardry.MODID, name), FabricParticleTypes.simple());
        FACTORIES.put(particle, factory);

        return particle;
    }
}
