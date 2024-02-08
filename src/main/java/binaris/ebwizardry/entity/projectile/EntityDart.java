package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class EntityDart extends EntityMagicArrow{
    public EntityDart(World world) {
        super(WizardryEntities.ENTITY_DART, world);
    }

    public EntityDart(EntityType<EntityDart> entityDartEntityType, World world) {
        super(entityDartEntityType, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    @Override
    public double getDamage() {
        return Spells.DART.getDoubleProperty(Spell.DAMAGE);
    }

    @Override
    public int getLifetime() {
        return -1;
    }

    @Override
    public Identifier getTexture() {
        return new Identifier("ebwizardry", "textures/entity/dart.png");
    }

    @Override
    public boolean doDeceleration() {
        return true;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        // Adds a weakness effect to the target.
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, Spells.DART.getIntProperty(Spell.EFFECT_DURATION),
                    Spells.DART.getIntProperty(Spell.EFFECT_STRENGTH), false, false));
        }
        this.playSound(WizardrySounds.ENTITY_DART_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.playSound(WizardrySounds.ENTITY_DART_HIT_BLOCK, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        super.onBlockHit(blockHitResult);
    }

    @Override
    public void ticksInAir() {
        if(this.getWorld().isClient){
            // FIXME: This color are originally from the leaf particle class, but it's not defined yet.
            ParticleBuilder.create(WizardryParticles.LEAF, this).time(10 + random.nextInt(5)).color(0.1f + 0.3f * random.nextFloat(), 0.5f + 0.3f * random.nextFloat(), 0.1f).spawn(getWorld());
        }
    }

    @Override
    public void tickInGround() {
        if(this.ticksInGround > 60){
            this.discard();
        }
    }
}
