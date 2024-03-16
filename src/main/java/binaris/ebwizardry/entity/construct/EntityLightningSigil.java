package binaris.ebwizardry.entity.construct;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.EntityUtil;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityLightningSigil extends EntityScaledConstruct {
    public static final String SECONDARY_RANGE = "secondary_range";
    public static final String SECONDARY_MAX_TARGETS = "secondary_max_targets";

    public EntityLightningSigil(EntityType<?> type, World world) {
        super(type, world);
    }

    public EntityLightningSigil(World world) {
        super(WizardryEntities.LIGHTNING_SIGIL, world);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.changing(Spells.FROST_SIGIL.getFloatProperty(Spell.EFFECT_RADIUS) * 2, 0.2f);
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age > 600 && this.getCaster() == null && !this.getWorld().isClient) {
            this.discard();
        }
        List<LivingEntity> targets = EntityUtil.getLivingEntitiesInRange(getWorld(), getX(), getY(), getZ(), getWidth() / 2);

        for (LivingEntity target : targets) {
            if (this.isValidTarget(target)) {
                Vec3d originalVec = target.getVelocity();

                if(target.damage(getDamageSources().indirectMagic(this.getCaster(), target), Spells.LIGHTNING_SIGIL.getFloatProperty(Spell.DIRECT_DAMAGE) * damageMultiplier)) {
                    target.setVelocity(originalVec);
                    this.playSound(WizardrySounds.ENTITY_LIGHTNING_SIGIL_TRIGGER, 1.0f, 1.0f);
                    double seekerRange = Spells.LIGHTNING_SIGIL.getFloatProperty(SECONDARY_RANGE);
                    List<LivingEntity> secondaryTargets = EntityUtil.getLivingEntitiesInRange(getWorld(), target.getX(), target.getY() + target.getHeight() / 2, target.getZ(), seekerRange);
                    for (int j = 0; j < Math.min(secondaryTargets.size(), Spells.LIGHTNING_SIGIL.getFloatProperty(SECONDARY_MAX_TARGETS)); j++) {
                        LivingEntity secondaryTarget = secondaryTargets.get(j);
                        if (secondaryTarget != target && this.isValidTarget(secondaryTarget)) {
                            if (this.getWorld().isClient) {
                                ParticleBuilder.create(WizardryParticles.LIGHTNING).entity(target).pos(0, target.getHeight() / 2, 0).target(secondaryTarget).spawn(getWorld());
                                ParticleBuilder.spawnShockParticles(getWorld(), secondaryTarget.getX(), secondaryTarget.getY() + secondaryTarget.getHeight() / 2, secondaryTarget.getZ());
                            }
                            secondaryTarget.playSound(WizardrySounds.ENTITY_LIGHTNING_SIGIL_TRIGGER, 1.0F, getWorld().random.nextFloat() * 0.4F + 1.5F);
                            secondaryTarget.damage(getDamageSources().indirectMagic(this.getCaster(), secondaryTarget), Spells.LIGHTNING_SIGIL.getFloatProperty(Spell.SPLASH_DAMAGE) * damageMultiplier);
                        }
                    }
                    this.discard();
                }
            }
        }

        if(this.getWorld().isClient && this.random.nextInt(15) == 0){
            double radius = (0.5 + random.nextDouble() * 0.3) * getWidth() / 2;
            float angle = random.nextFloat() * (float) Math.PI * 2;
            ParticleBuilder.create(WizardryParticles.SPARK)
                    .pos(this.getX() + radius * MathHelper.cos(angle), this.getY() + 0.1, this.getZ() + radius * MathHelper.sin(angle))
                    .spawn(getWorld());
        }
    }
}
