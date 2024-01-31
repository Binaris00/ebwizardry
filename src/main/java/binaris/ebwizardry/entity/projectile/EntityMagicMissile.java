package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
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
    protected void onEntityHit(EntityHitResult entityHitResult) {
        this.playSound(WizardrySounds.ENTITY_MAGIC_MISSILE_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        // TODO: FLASH PARTICLE
        //if(this.getWorld().isClient) ParticleBuilder.create(Type.FLASH).pos(posX, posY, posZ).clr(1, 1, 0.65f).spawn(world);
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        // Gets a position slightly away from the block hit so the particle doesn't get cut in half by the block face
        Vec3d vec = blockHitResult.getPos();
        // TODO: FLASH PARTICLE
        // ParticleBuilder.create(Type.FLASH).ve3d(vec).clr(1, 1, 0.65f).fade(0.85f, 0.5f, 0.8f).spawn(world);
        super.onBlockHit(blockHitResult);
    }

    @Override
    public void tick() {
        Wizardry.LOGGER.info("EntityMagicMissile tick");
        if(!this.inGround){
            getWorld().sendEntityStatus(this, (byte) 3);
            ParticleBuilder.create(WizardryParticles.SPARKLE, random, prevX, prevY, prevZ, 0.03, true).color(1, 1, 0.65f).time(20 + random.nextInt(10)).spawn(getWorld());
            Wizardry.LOGGER.info("EntityMagicMissile not in ground");

            if(this.getFireTicks() > 1){ // Don't spawn particles behind where it started!
                double x = prevX - getX() / 2;
                double y = prevY - getY() / 2;
                double z = prevZ - getZ() / 2;
                ParticleBuilder.create(WizardryParticles.SPARKLE, random, x, y, z, 0.03, true).color(1, 1, 0.65f).time(20 + random.nextInt(10)).spawn(getWorld());
            }
        }
        aim((LivingEntity) getOwner(), 1.5f);
        super.tick();
    }

    @Override
    public void handleStatus(byte status) {
        if(status == 3){
            ParticleBuilder.create(WizardryParticles.SPARKLE, random, prevX, prevY, prevZ, 0.03, true).color(1, 1, 0.65f).time(20 + random.nextInt(10)).spawn(getWorld());
            double x = prevX - getX() / 2;
            double y = prevY - getY() / 2;
            double z = prevZ - getZ() / 2;
            ParticleBuilder.create(WizardryParticles.SPARKLE, random, x, y, z, 0.03, true).color(1, 1, 0.65f).time(20 + random.nextInt(10)).spawn(getWorld());
        }
        super.handleStatus(status);
    }
}
