package binaris.ebwizardry.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ParticleGuardianBeam extends ParticleTargeted {
    private static final float THICKNESS = 0.15f;

    private static final Identifier TEXTURE = new Identifier("minecraft:textures/entity/guardian_beam.png");
    public ParticleGuardianBeam(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, false);
        this.setColor(1, 1, 1);
        this.setMaxAge(3);
        this.scale = 1;
    }

    @Override
    protected void draw(MatrixStack stack, Tessellator tessellator, float length, float tickDelta) {
        float scale = this.scale;

        BufferBuilder buffer = tessellator.getBuffer();

        stack.push();

        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MinecraftClient.getInstance().player.age + tickDelta));

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderTexture(0, TEXTURE);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        float t = THICKNESS * scale;
        float v1 = 2 * (age + tickDelta) / maxAge;
        float v2 = v1 + length * 2;

        buffer.vertex(stack.peek().getPositionMatrix(), -t, 0, 0).texture(0, v1).color(red, green, blue, 1).next();
        buffer.vertex(stack.peek().getPositionMatrix(), t, 0, 0).texture(0.5f, v1).color(red, green, blue, 1).next();
        buffer.vertex(stack.peek().getPositionMatrix(), t, 0, length).texture(0.5f, v2).color(red, green, blue, 1).next();
        buffer.vertex(stack.peek().getPositionMatrix(), -t, 0, length).texture(0, v2).color(red, green, blue, 1).next();

        buffer.vertex(stack.peek().getPositionMatrix(), 0, -t, 0).texture(0, v1).color(red, green, blue, 1).next();
        buffer.vertex(stack.peek().getPositionMatrix(), 0, t, 0).texture(0.5f, v1).color(red, green, blue, 1).next();
        buffer.vertex(stack.peek().getPositionMatrix(), 0, t, length).texture(0.5f, v2).color(red, green, blue, 1).next();
        buffer.vertex(stack.peek().getPositionMatrix(), 0, -t, length).texture(0, v2).color(red, green, blue, 1).next();

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        stack.pop();
    }

    @Deprecated
    public static class GuardianBeamFactory implements ParticleFactory<DefaultParticleType> {
        static SpriteProvider spriteProvider;

        public GuardianBeamFactory(SpriteProvider sprite) {
            spriteProvider = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ParticleGuardianBeam(world, x, y, z, spriteProvider);
        }

        public static ParticleWizardry createParticle(ClientWorld clientWorld, Vec3d vec3d) {
            return new ParticleGuardianBeam(clientWorld, vec3d.x, vec3d.y, vec3d.z, spriteProvider);
        }
    }
}
