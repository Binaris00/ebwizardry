package binaris.ebwizardry.spell;

import binaris.ebwizardry.entity.projectile.EntityForceArrow;
import binaris.ebwizardry.util.SpellModifiers;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class ForceArrow extends SpellArrow<EntityForceArrow>{
    public ForceArrow() {
        super("force_arrow", EntityForceArrow::new);
    }

    @Override
    protected void addArrowExtras(EntityForceArrow arrow, @Nullable LivingEntity caster, SpellModifiers modifiers) {
        arrow.setMana((int) (this.getProperties().getCost() * modifiers.get(SpellModifiers.COST)));
    }
}
