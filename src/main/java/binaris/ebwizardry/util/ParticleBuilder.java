package binaris.ebwizardry.util;

import binaris.ebwizardry.registry.WizardryParticles;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;


/**
 * ParticleBuilder is a class that allows you to create particles easily.
 * */
public class ParticleBuilder {
    public static ParticleBuilder instance = new ParticleBuilder();
    public DefaultParticleType particle;
    public World world;
    public boolean building;
    public double x;
    public double y;
    public double z;
    public int lifetime;
    public float scale;
    public float red;
    public float green;
    public float blue;





    // ------------------------- Core methods -------------------------------- //

    /**
     * Start building a particle.
     * This is just for more readable code with a static function.
     * @see #particle(DefaultParticleType)
     *
     * @param particle The particle type
     * @return The ParticleBuilder instance
     * @throws IllegalStateException If already building
     * */
    public static ParticleBuilder create(DefaultParticleType particle){
        return ParticleBuilder.instance.particle(particle);
    }

    /**
     * Start building a particle.
     * @param particle The particle type
     * @return The ParticleBuilder instance
     * @throws IllegalStateException If already building
     * */
    private ParticleBuilder particle(DefaultParticleType particle){
        this.particle = particle;
        this.building = true;
        return this;
    }

    /**
     * Creates a particle at a random position within a radius of the given position.
     * Just in case if you need to spawn random particles in a radius.
     * For a normal particle, use {@link #create(DefaultParticleType)}
     * @param type The particle type
     * @param random The random object
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * @param radius The radius
     * */
    public static ParticleBuilder create(DefaultParticleType type, Random random, double x, double y, double z, double radius){
        double px = x + (random.nextDouble()*2 - 1) * radius;
        double py = y + (random.nextDouble()*2 - 1) * radius;
        double pz = z + (random.nextDouble()*2 - 1) * radius;

        return ParticleBuilder.create(type).pos(px, py, pz);
    }

    /**
     * Sets the position of the particle.
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * */
    public ParticleBuilder pos(double x, double y, double z){
        if(!building) throw new IllegalStateException("Not building yet!");
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public ParticleBuilder time(int lifetime){
        if(!building) throw new IllegalStateException("Not building yet!");
        this.lifetime = lifetime;
        return this;
    }

    public ParticleBuilder scale(float scale){
        if(!building) throw new IllegalStateException("Not building yet!");
        this.scale = scale;
        return this;
    }

    public ParticleBuilder color(float red, float green, float blue){
        if(!building) throw new IllegalStateException("Not building yet!");
        this.red = red;
        this.green = green;
        this.blue = blue;
        return this;
    }

    public void spawn(World world){
        if(!building) throw new IllegalStateException("Not building yet!");
        this.world = world;

        world.addParticle(particle, x, y, z, 0, 0, 0);

        this.reset();
    }

    public static void spawnShockParticles(World world, double x, double y, double z) {
        double px, py, pz;

        for(int i=0; i<8; i++){
            px = x + world.random.nextDouble() - 0.5;
            py = y + world.random.nextDouble() + 0.5;
            pz = z + world.random.nextDouble() - 0.5;
            ParticleBuilder.create(WizardryParticles.LIGHTNING).pos(px, py, pz).spawn(world);

            px = x + world.random.nextDouble() - 0.5;
            py = y + world.random.nextDouble() - 0.5;
            pz = z + world.random.nextDouble() - 0.5;
            ParticleBuilder.create(ParticleTypes.LARGE_SMOKE).pos(px, py, pz).spawn(world);
        }
    }






    public void reset(){
        this.particle = null;
        this.world = null;
        this.building = false;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.lifetime = 0;
        this.scale = 0;
    }
}
