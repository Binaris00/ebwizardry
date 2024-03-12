package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class EntityLightningArrow extends EntityMagicArrow{
    public EntityLightningArrow(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntityLightningArrow(World world) {
        super(WizardryEntities.ENTITY_LIGHTNING_ARROW, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    @Override
    public double getDamage() {
        return Spells.LIGHTNING_ARROW.getFloatProperty(Spell.DAMAGE);
    }

    @Override
    public int getLifetime() {
        return 20;
    }

    @Override
    public Identifier getTexture() {
        return new Identifier("ebwizardry", "textures/entity/lightning_arrow.png");
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        for(int i = 0; i < 8; i++){
            if(getWorld().isClient) {
                ParticleBuilder.create(WizardryParticles.SPARK, this.random, this.prevX, this.prevY + this.getHeight() / 2, this.prevY, 1, false).spawn(this.getWorld());
            }
        }
        this.playSound(WizardrySounds.ENTITY_LIGHTNING_ARROW_HIT, 1.0F, 1.0F);
        super.onEntityHit(entityHitResult);
    }

    @Override
    public void tick() {
        if(!this.inGround){
            if(this.age > 1) {
                ParticleBuilder.create(WizardryParticles.SPARK).pos(this.prevX, this.prevY, this.prevZ).spawn(this.getWorld());
            }
        }
        super.tick();
    }
}
