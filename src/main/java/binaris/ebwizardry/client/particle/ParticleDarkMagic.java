package binaris.ebwizardry.client.particle;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

public class ParticleDarkMagic extends ParticleWizardry {

    public ParticleDarkMagic(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider);
    }




    public void spawn(){
        MinecraftClient.getInstance().particleManager.addParticle(this);
    }
}
