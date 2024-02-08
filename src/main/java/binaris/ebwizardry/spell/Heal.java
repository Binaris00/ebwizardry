package binaris.ebwizardry.spell;

import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;


public class Heal extends SpellBuff{
    public Heal() {
        super("heal", 1, 1, 0.3f);
    }


    @Override
    protected boolean applyEffects(LivingEntity caster, SpellModifiers modifiers){

        if(caster.getHealth() < caster.getMaxHealth() && caster.getHealth() > 0){
            heal(caster, getFloatProperty(HEALTH) * modifiers.get(SpellModifiers.POTENCY));
            return true;
        }

        return false;
    }

    /**
     * Heals the given entity by the given amount, accounting for special behaviour from artefacts. This does not check
     * whether the entity is already on full health or not.
     * @param entity The entity to heal
     * @param health The number of half-hearts to heal
     */
    public static void heal(LivingEntity entity, float health){

        float excessHealth = entity.getHealth() + health - entity.getMaxHealth();

        entity.heal(health);

        // If the player is able to heal, they can't possibly have absorption hearts, so no need to check!
        //TODO : ARTIFACTS
        // && ItemArtefact.isArtefactActive((EntityPlayer)entity, WizardryItems.amulet_absorption
        if(excessHealth > 0 && entity instanceof PlayerEntity){
            entity.setAbsorptionAmount(excessHealth);
        }

    }
}
