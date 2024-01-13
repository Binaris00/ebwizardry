package binaris.ebwizardry.util;

import binaris.ebwizardry.client.particle.ParticleProperties;
import binaris.ebwizardry.client.particle.ParticleWizardry;
import binaris.ebwizardry.registry.WizardryParticles;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;


/**
 * Tired of creating particles without custom properties?
 * This is the solution!
 * ParticleBuilder is a class that allows you to create particles easily.
 * You can set the position, velocity, color, scale, lifetime and more!
 * This only works on particles instance of {@link ParticleWizardry}
 *
 * You can use this class calling {@link ParticleBuilder#create(DefaultParticleType)} (Be sure to call this method first!)
 * Later, use {@link ParticleBuilder#pos(double, double, double)} to set the position of the particle, and if you want to set the velocity use {@link ParticleBuilder#velocity(double, double, double)}
 * And then you can set the other methods to set other specific things.
 * Finally, call {@link ParticleBuilder#spawn(World)} to spawn the particle in the world.
 * */
public final class ParticleBuilder {
    private static final ParticleBuilder instance = new ParticleBuilder();
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
    public double velocityX;
    public double velocityY;
    public double velocityZ;
    public boolean shaded;
    // ------------------------- Core methods -------------------------------- //

    /**
     * Start building a particle. For creating a particle in a static way use {@link #create(DefaultParticleType)}
     * @param particle The particle type
     * @return The ParticleBuilder instance
     * */
    private ParticleBuilder particle(DefaultParticleType particle){
        this.particle = particle;
        this.building = true;
        return this;
    }
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
     * Creates a particle at a random position within a radius of the given position.
     * Just in case if you need to spawn random particles in a radius.
     * For creating a normal particle, use {@link #create(DefaultParticleType)}
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

    public static ParticleBuilder create(DefaultParticleType type, Random random, double x, double y, double z, double radius, boolean move){
        double px = x + (random.nextDouble()*2 - 1) * radius;
        double py = y + (random.nextDouble()*2 - 1) * radius;
        double pz = z + (random.nextDouble()*2 - 1) * radius;

        if(move){return ParticleBuilder.create(type).pos(px, py, pz).velocity(px - x, py - y, pz - z);}

        return ParticleBuilder.create(type).pos(px, py, pz);
    }


    // ------------------------- Setters -------------------------------- //
    /**
     * Sets the position of the particle.
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * @throws IllegalStateException If not building yet
     * */
    public ParticleBuilder pos(double x, double y, double z){
        if(!building) throw new IllegalStateException("Not building yet!");
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }
    /**
     * Set the max age of the particle.
     * @param lifetime The lifetime
     * @throws IllegalStateException If not building yet
     * */
    public ParticleBuilder time(int lifetime){
        if(!building) throw new IllegalStateException("Not building yet!");
        this.lifetime = lifetime;
        return this;
    }
    /**
     * Sets the velocity of the particle.
     * @param velocityX The x velocity
     * @param velocityY The y velocity
     * @param velocityZ The z velocity
     * @throws IllegalStateException If not building yet
     * */
    public ParticleBuilder velocity(double velocityX, double velocityY, double velocityZ){
        if(!building) throw new IllegalStateException("Not building yet!");
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        return this;
    }
    /** set the scale of the particle
     * @param scale The scale
     * @throws IllegalStateException If not building yet
     * */
    public ParticleBuilder scale(float scale){
        if(!building) throw new IllegalStateException("Not building yet!");
        this.scale = scale;
        return this;
    }
    /**
     * Sets the color of the particle.
     * @param red The red value
     * @param green The green value
     * @param blue The blue value
     * @throws IllegalStateException If not building yet
     * */
    public ParticleBuilder color(float red, float green, float blue){
        if(!building) throw new IllegalStateException("Not building yet!");
        this.red = red;
        this.green = green;
        this.blue = blue;
        return this;
    }
    /**
     * Set the shaded property of the particle.
     * @param value The value
     * @throws IllegalStateException If not building yet
     * **/
    public ParticleBuilder shaded(boolean value){
        if(!building) throw new IllegalStateException("Not building yet!");
        this.shaded = value;
        return this;
    }

    /**
     * Spawn the particle in the world.
     * Create a new particle properties {@link ParticleProperties} object and set the properties.
     * Call the ParticleWizardry factory to set this property and call the reset method.
     * @param world The world
     * @throws IllegalStateException If not building yet
     * */
    public void spawn(World world){
        if(!building) throw new IllegalStateException("Not building yet!");
        this.world = world;

        // Create a new particle properties
        ParticleProperties properties = new ParticleProperties();
        properties.setPos(x, y, z);
        properties.setMaxAge(lifetime);
        properties.setVelocity(velocityX, velocityY, velocityZ);
        properties.setColor(red, green, blue);
        properties.setScale(scale);
        properties.setShaded(shaded);

        ParticleWizardry.WizardryFactory.setProperties(properties);
        this.world.addParticle(particle, x, y, z, velocityX, velocityY, velocityZ);

        // Reset the builder
        this.reset();
    }

    public void reset(){
        this.particle = null;
        this.world = null;
        this.building = false;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.scale = 1;

        //Default lifetime: 20
        this.lifetime = 20;

        // To set the default color
        this.red = 1;
        this.green = 1;
        this.blue = 1;

        this.velocityX = 0;
        this.velocityY = 0;
        this.velocityZ = 0;
    }


    // ------------------------- Helper methods -------------------------------- //
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
            world.addParticle(ParticleTypes.LARGE_SMOKE, px, py, pz, 0, 0, 0);
        }
    }
}
