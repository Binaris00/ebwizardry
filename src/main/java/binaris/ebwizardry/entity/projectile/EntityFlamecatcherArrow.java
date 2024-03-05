package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFlamecatcherArrow extends EntityMagicArrow{
    public static final float SPEED = 3;
    public EntityFlamecatcherArrow(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
    public EntityFlamecatcherArrow(World world) {
        super(WizardryEntities.FLAMECATCHER_ARROW, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    @Override
    public Identifier getTexture() {
        return new Identifier("ebwizardry", "textures/entity/flamecatcher_arrow.png");
    }

    @Override
    public double getDamage() {
        return Spells.FLAMECATCHER.getFloatProperty(Spell.DAMAGE);
    }

    @Override
    public int getLifetime() {
        return (int) (Spells.FLAMECATCHER.getFloatProperty(Spell.RANGE) / SPEED);
    }

    @Override
    public boolean doDeceleration() {
        return false;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if(entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
            livingEntity.setOnFireFor(Spells.FLAMECATCHER.getIntProperty(Spell.BURN_DURATION));
            this.playSound(WizardrySounds.ENTITY_FLAMECATCHER_ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if(this.getWorld().isClient) {
                // TODO: Flash particle
                // ParticleBuilder.create(Type.FLASH).pos(getX(), getY(), getZ()).clr(0xff6d00).spawn(level);
            }
        }

        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if(this.getWorld().isClient) {
            Vec3d vec = blockHitResult.getPos().add(new Vec3d(blockHitResult.getSide().getUnitVector()).multiply(0.15));
            // TODO: Flash particle
            // ParticleBuilder.create(Type.FLASH).pos(vec).clr(0xff6d00).fade(0.85f, 0.5f, 0.8f).spawn(level);
        }

        super.onBlockHit(blockHitResult);
    }

    @Override
    public void ticksInAir() {
        if(this.getWorld().isClient) {
            ParticleBuilder.create(WizardryParticles.MAGIC_FIRE, this.random, this.getX(), this.getY(), this.getZ(), 0.03, false)
                    .time(20 + this.random.nextInt(10)).spawn(this.getWorld());

            if(this.getLifetime() > 1) {
                double x = this.getX() - this.getVelocity().x / 2;
                double y = this.getY() - this.getVelocity().y / 2;
                double z = this.getZ() - this.getVelocity().z / 2;
                ParticleBuilder.create(WizardryParticles.MAGIC_FIRE, this.random, x, y, z, 0.03, false)
                        .time(20 + this.random.nextInt(10)).spawn(this.getWorld());
            }

        }
        super.ticksInAir();
    }
}
