package binaris.ebwizardry.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ParticleFlash extends ParticleWizardry{
    public ParticleFlash(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, false);
        this.setColor(1, 1, 1);
        this.scale = 0.6f;
        this.maxAge = 6;
    }

    @Override
    protected void drawParticle(VertexConsumer buffer, Camera camera, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        //not sure necessary
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        Vec3d vec3 = camera.getPos();
        float f4 = 0.1f * scale * MathHelper.sin(((float) this.age + partialTicks - 1.0F) / maxAge * (float) Math.PI);
        this.setAlpha(0.6F - ((float) this.age + partialTicks - 1.0F) / maxAge * 0.5F);

        float f5 = (float) (MathHelper.lerp(partialTicks, this.prevPosX, this.x) - vec3.getX());
        float f6 = (float) (MathHelper.lerp(partialTicks, this.prevPosY, this.y) - vec3.getY());
        float f7 = (float) (MathHelper.lerp(partialTicks, this.prevPosZ, this.z) - vec3.getZ());

        int i = this.getBrightness(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        buffer.vertex(f5 - rotationX * f4 - rotationXY * f4, f6 - rotationZ * f4, f7 - rotationYZ * f4 - rotationXZ * f4).texture(0.5F, 0.375F).color(this.red, this.green, this.blue, this.alpha).light(j, k).next();
        buffer.vertex(f5 - rotationX * f4 + rotationXY * f4, f6 + rotationZ * f4, f7 - rotationYZ * f4 + rotationXZ * f4).texture(0.5F, 0.125F).color(this.red, this.green, this.blue, this.alpha).light(j, k).next();
        buffer.vertex(f5 + rotationX * f4 + rotationXY * f4, f6 + rotationZ * f4, f7 + rotationYZ * f4 + rotationXZ * f4).texture(0.25F, 0.125F).color(this.red, this.green, this.blue, this.alpha).light(j, k).next();
        buffer.vertex(f5 + rotationX * f4 - rotationXY * f4, f6 - rotationZ * f4, f7 + rotationYZ * f4 - rotationXZ * f4).texture(0.25F, 0.375F).color(this.red, this.green, this.blue, this.alpha).light(j, k).next();
    }

    @Deprecated
    public static class FlashFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public FlashFactory(SpriteProvider sprite) {
            spriteProvider = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleFlash(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld world, double x, double y, double z) {
            return new ParticleFlash(world, x, y, z, spriteProvider);
        }
    }
}
