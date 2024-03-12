package binaris.ebwizardry.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ParticleScorch extends ParticleWizardry{
    public ParticleScorch(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, false);
        this.gravityStrength = 0;
        this.setMaxAge(100 + random.nextInt(40));
        this.scale(2);
        this.setColor(0, 0, 0);
        this.shaded = false;
        this.adjustQuadSize = false;

        // Set a random sprite from the spriteProvider
        this.setSprite(spriteProvider.getSprite(world.random));
    }

    @Override
    public void tick() {
        super.tick();

        float ageFraction = Math.min((float) this.age / ((float) this.maxAge * 0.5f), 1);

        this.red = this.initialRed + (this.fadeRed - this.initialRed) * ageFraction;
        this.green = this.initialGreen + (this.fadeGreen - this.initialGreen) * ageFraction;
        this.blue = this.initialBlue + (this.fadeBlue - this.initialBlue) * ageFraction;

        if (this.age > this.maxAge / 2) {
            this.setAlpha(1 - ((float) this.age - this.maxAge / 2f) / (this.maxAge / 2f));
        }

        Direction facing = Direction.fromRotation(yaw);
        if (pitch == 90) facing = Direction.UP;
        if (pitch == -90) facing = Direction.DOWN;

        if (!world.getBlockState(new BlockPos((int) x, (int) y, (int) z).offset(facing.getOpposite())).isSolid()) {
            this.markDead();
        }
    }

    public static class ScorchFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public ScorchFactory(SpriteProvider sprite) {
            spriteProvider = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleScorch(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld clientWorld, Vec3d vec3d) {
            return new ParticleScorch(clientWorld, vec3d.x, vec3d.y, vec3d.z, spriteProvider);
        }
    }
}
