package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
public class EntityMagicMissile extends EntityMagicArrow{
    public EntityMagicMissile(World world) {
        super(WizardryEntities.ENTITY_MAGIC_MISSILE, world);
    }

    public EntityMagicMissile(EntityType<EntityMagicMissile> entityMagicMissileEntityType, World world) {
        super(entityMagicMissileEntityType, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    @Override
    public double getDamage() {
        return Spells.MAGIC_MISSILE.getFloatProperty(Spell.DAMAGE);
    }

    @Override
    public int getLifetime() {
        return 12;
    }

    @Override
    public Identifier getTexture() {
        return new Identifier("ebwizardry", "textures/entity/magic_missile.png");
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        this.playSound(WizardrySounds.ENTITY_MAGIC_MISSILE_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));

        if(this.getWorld().isClient) ParticleBuilder.create(WizardryParticles.FLASH).pos(prevX, prevY, prevZ).color(1, 1, 0.65f).spawn(getWorld());
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        // Gets a position slightly away from the block hit so the particle doesn't get cut in half by the block face
        Vec3d vec = blockHitResult.getPos();
        ParticleBuilder.create(WizardryParticles.FLASH).pos(vec).color(1, 1, 0.65f).fade(0.85f, 0.5f, 0.8f).spawn(getWorld());
        super.onBlockHit(blockHitResult);
    }

    @Override
    public void tick() {
        if(!this.inGround){
            if(this.age > 1) {
                ParticleBuilder.create(WizardryParticles.SPARKLE, random, prevX, prevY, prevZ, 0.03, true).color(1, 1, 0.65f)
                        .time(20 + random.nextInt(10)).fade(0.7f, 0, 1).spawn(getWorld());

                double x = prevX - getVelocity().getX() / 2;
                double y = prevY - getVelocity().getY() / 2;
                double z = prevZ - getVelocity().getZ() / 2;

                ParticleBuilder.create(WizardryParticles.SPARKLE, random, x, y, z, 0.03, true).color(1, 1, 0.65f)
                        .time(20 + random.nextInt(10)).fade(0.7f, 0, 1).spawn(getWorld());
            }
        }
        super.tick();
    }
}
