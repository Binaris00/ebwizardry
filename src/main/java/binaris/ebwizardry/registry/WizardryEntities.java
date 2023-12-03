package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.entity.projectile.EntitySparkBomb;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class WizardryEntities {
    public static final EntityType<EntitySparkBomb> entitySparkBomb = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "spark_bomb"),
            FabricEntityTypeBuilder.<EntitySparkBomb>create(SpawnGroup.MISC, EntitySparkBomb::new)
                    .build());
}
