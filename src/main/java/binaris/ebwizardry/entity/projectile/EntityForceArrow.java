package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.item.ManaStoringItem;
import binaris.ebwizardry.item.SpellCastingItem;
import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.registry.WizardrySounds;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.InventoryUtils;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Arrays;

public class EntityForceArrow extends EntityMagicArrow{
    /** The mana used to cast this force arrow, used for artefacts. */
    private int mana = 0;

    public EntityForceArrow(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntityForceArrow(World world) {
        super(WizardryEntities.FORCE_ARROW, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        this.playSound(WizardrySounds.ENTITY_FORCE_ARROW_HIT, 1.0F, 1.0F);
        if (this.getWorld().isClient)
            ParticleBuilder.create(WizardryParticles.FLASH).pos(getX(), getY(), getZ()).scale(1.3f).color(0.75f, 1, 0.85f).spawn(getWorld());

        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.playSound(WizardrySounds.ENTITY_FORCE_ARROW_HIT, 1.0F, 1.0F);
        if(this.getWorld().isClient){
            Vec3d vec = blockHitResult.getPos().add(new Vec3d(blockHitResult.getSide().getUnitVector()).multiply(0.15));
            ParticleBuilder.create(WizardryParticles.FLASH).pos(vec).scale(1.3f).color(0.75f, 1, 0.85f).spawn(getWorld());
        }

        super.onBlockHit(blockHitResult);
    }

    @Override
    public void tickInGround(){
        returnManaToCaster();
        this.discard();
    }

    @Override
    public void tick() {
        if(getLifetime() >= 0 && this.age > getLifetime()){
            returnManaToCaster();
        }

        super.tick();
    }

    private void returnManaToCaster() {
        if (mana > 0 && getOwner() instanceof PlayerEntity player) {
            // TODO: Item Artifacts
            // && ItemArtefact.isArtefactActive(player, WizardryItems.RING_MANA_RETURN.get()
            if (!player.getAbilities().creativeMode) {
                for (ItemStack stack : InventoryUtils.getPrioritisedHotBarAndOffhand(player)) {
                    if (stack.getItem() instanceof SpellCastingItem && stack.getItem() instanceof ManaStoringItem && Arrays.asList(((SpellCastingItem) stack.getItem()).getSpells(stack)).contains(Spells.FORCE_ARROW)) {
                        ((ManaStoringItem) stack.getItem()).rechargeMana(stack, mana);
                    }
                }
            }
        }
    }

    @Override
    public double getDamage() {
        return Spells.FORCE_ARROW.getFloatProperty(Spell.DAMAGE);
    }

    @Override
    public int getLifetime() {
        return 20;
    }

    @Override
    public Identifier getTexture() {
        return new Identifier("ebwizardry", "textures/entity/force_arrow.png");
    }
}
