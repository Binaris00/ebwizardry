package binaris.ebwizardry.entity.construct;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.EntityUtil;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityFireSigil extends EntityScaledConstruct {
    public EntityFireSigil(EntityType<?> type, World world) {
        super(type, world);
    }
    public EntityFireSigil(World world) {
        super(WizardryEntities.FIRE_SIGIL, world);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.changing(Spells.FIRE_SIGIL.getFloatProperty(Spell.EFFECT_RADIUS) * 2, 0.2f);
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            List<LivingEntity> targets = EntityUtil.getLivingEntitiesInRange(getWorld(), getX(), getY(), getZ(), getWidth() / 2);

            for (LivingEntity target : targets) {
                if (this.isValidTarget(target)) {
                    Vec3d originalVec = target.getVelocity();
                    target.damage(this.getDamageSources().indirectMagic(this, this.getCaster()), Spells.FIRE_SIGIL.getFloatProperty(Spell.DAMAGE) * damageMultiplier);

                    target.setVelocity(originalVec);
                    target.setOnFireFor(Spells.FIRE_SIGIL.getIntProperty(Spell.BURN_DURATION));

                    this.playSound(WizardrySounds.ENTITY_FIRE_SIGIL_TRIGGER, 1, 1);

                    this.discard();
                }
            }
        } else if (this.random.nextInt(15) == 0) {
            double radius = (0.5 + random.nextDouble() * 0.3) * getWidth() / 2;
            float angle = random.nextFloat() * (float) Math.PI * 2;
            getWorld().addParticle(ParticleTypes.FLAME, this.getX() + radius * MathHelper.cos(angle), this.getY() + 0.1, this.getZ() + radius * MathHelper.sin(angle), 0, 0, 0);
        }
    }
}
