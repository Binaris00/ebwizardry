package binaris.ebwizardry.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ParticleBuilder {
    /** The static instance of the particle builder. */
    public static final ParticleBuilder instance = new ParticleBuilder();
    /** Whether the particle builder is currently building or not. */
    private boolean building = false;

    // Builder variables
    // We can't just store a particle and set its parameters in the builder methods, because the server won't like having
    // a field of a client-only type
    private Identifier type;
    private double x, y, z;
    private double vx, vy, vz;
    private float r, g, b;
    private float fr, fg, fb;
    private double radius;
    private double rpt;
    private int lifetime;
    private boolean gravity;
    private boolean shaded;
    private boolean collide;
    private float scale;
    private Entity entity;
    private float yaw, pitch;
    private double tx, ty, tz;
    private double tvx, tvy, tvz;
    private Entity target;
    private long seed;
    private double length;

    // ============================================= Core builder methods =============================================

    public static ParticleBuilder create(Identifier type){
        return ParticleBuilder.instance.particle(type);
    }

    public ParticleBuilder particle(Identifier type){
        if(building) throw new IllegalStateException("Already building! Particle being built: " + getCurrentParticleString());
        this.type = type;
        this.building = true;
        return this;
    }




    // Methods for spawning specific effects (similar to the FX playing methods with the ids in RenderGlobal)

    /** Spawns spark and large smoke particles (8 of each) within a 1x1x1 volume centred on the given position. */
    public static void spawnShockParticles(World world, double x, double y, double z) {

        double px, py, pz;

        for(int i=0; i<8; i++){
            px = x + world.random.nextDouble() - 0.5;
            py = y + world.random.nextDouble() - 0.5;
            pz = z + world.random.nextDouble() - 0.5;
            // ParticleBuilder.create(Type.SPARK).pos(px, py, pz).spawn(world);
            px = x + world.random.nextDouble() - 0.5;
            py = y + world.random.nextDouble() - 0.5;
            pz = z + world.random.nextDouble() - 0.5;
            // world.addParticle(ParticleEffect.SMOKE_LARGE, px, py, pz, 0, 0, 0);
        }
    }

    /** Gets a readable string representation of the current builder parameters; used in error messages. */
    private String getCurrentParticleString(){
        return String.format("[ Type: %s, Position: (%s, %s, %s), Velocity: (%s, %s, %s), Colour: (%s, %s, %s), "
                        + "Fade Colour: (%s, %s, %s), Radius: %s, Revs/tick: %s, Lifetime: %s, Gravity: %s, Shaded: %s, "
                        + "Scale: %s, Entity: %s ]",
                type, x, y, z, vx, vy, vz, r, g, b, fr, fg, fb, radius, rpt, lifetime, gravity, shaded, scale, entity);
    }

    public ParticleBuilder pos(double x, double y, double z){
        if(!building) throw new IllegalStateException("Not building yet!");
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
}
