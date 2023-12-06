package binaris.ebwizardry.util;

import binaris.ebwizardry.registry.WizardryParticles;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
/**
 * ParticleBuilder is a class that allows you to create particles easily.
 * Deprecated because I'm not sure if I'll use it.
 *
 * */
@Deprecated
public class ParticleBuilder {
    public static ParticleBuilder instance = new ParticleBuilder();
    public DefaultParticleType particle;
    public World world;
    public boolean building;
    public double x;
    public double y;
    public double z;

    public ParticleBuilder create(DefaultParticleType particle){
        this.particle = particle;
        this.building = true;
        return this;
    }

    public ParticleBuilder pos(double x, double y, double z){
        if(!building) throw new IllegalStateException("Not building yet!");
        this.x = x;
        this.y = y;
        this.z = z;

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
            py = y + world.random.nextDouble() - 0.5;
            pz = z + world.random.nextDouble() - 0.5;
            ParticleBuilder.instance.create(WizardryParticles.LIGHTNING).pos(px, py, pz).spawn(world);

            px = x + world.random.nextDouble() - 0.5;
            py = y + world.random.nextDouble() - 0.5;
            pz = z + world.random.nextDouble() - 0.5;
            ParticleBuilder.instance.create(ParticleTypes.LARGE_SMOKE).pos(px, py, pz).spawn(world);
        }
    }






    public void reset(){
        this.particle = null;
        this.world = null;
        this.building = false;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
}
