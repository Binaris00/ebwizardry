package binaris.ebwizardry.entity.construct;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEffects;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.GeometryUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityIceSpike extends EntityMagicConstruct{
    private Direction facing;

    public EntityIceSpike(EntityType<?> type, World world) {
        super(type, world);
    }

    public EntityIceSpike(World world) {
        super(WizardryEntities.ICE_SPIKE, world);
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
        this.setRotation(-facing.asRotation(), GeometryUtils.getPitch(facing));
        float yaw = (-facing.asRotation()) * (float) Math.PI / 180;
        float pitch = (GeometryUtils.getPitch(facing) - 90) * (float) Math.PI / 180;
        Vec3d min = this.getPos().add(new Vec3d(-getWidth() / 2, 0, -getWidth() / 2).rotateX(pitch).rotateY(yaw));
        Vec3d max = this.getPos().add(new Vec3d(getWidth() / 2, getHeight(), getWidth() / 2).rotateX(pitch).rotateY(yaw));
        this.setBoundingBox(new Box(min.x, min.y, min.z, max.x, max.y, max.z));
    }

    public Direction getFacing() {
        return facing;
    }

    @Override
    public void tick() {
        double extensionSpeed = 0;

        if (!getWorld().isClient) {
            if (lifetime - this.age < 15) {
                extensionSpeed = -0.01 * (this.age - (lifetime - 15));
            } else if (lifetime - this.age < 25) {
                extensionSpeed = 0;
            } else if (lifetime - this.age < 28) {
                extensionSpeed = 0.25;
            }

            if (facing != null) {
                this.move(MovementType.SELF, new Vec3d(this.facing.getOffsetX() * extensionSpeed, this.facing.getOffsetY() * extensionSpeed, this.facing.getOffsetZ() * extensionSpeed));
            }
        }

        if (lifetime - this.age == 30) this.playSound(WizardrySounds.ENTITY_ICE_SPIKE_EXTEND, 1, 2.5f);

        if (!this.getWorld().isClient) {
            for (Object entity : this.getWorld().getOtherEntities(this, this.getBoundingBox())) {
                if (entity instanceof LivingEntity livingEntity && this.isValidTarget((LivingEntity) entity)) {
                    livingEntity.damage(getDamageSources().indirectMagic(this.getCaster(), livingEntity), Spells.ICE_SPIKES.getFloatProperty(Spell.DAMAGE) * this.damageMultiplier);
                    livingEntity.addStatusEffect(new StatusEffectInstance(WizardryEffects.FROST, Spells.ICE_SPIKES.getIntProperty(Spell.EFFECT_DURATION), Spells.ICE_SPIKES.getIntProperty(Spell.EFFECT_STRENGTH)));
                }
            }
        }

        super.tick();
    }
}
