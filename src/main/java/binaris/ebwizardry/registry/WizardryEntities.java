package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.entity.projectile.EntityFireBomb;
import binaris.ebwizardry.entity.projectile.EntityPoisonBomb;
import binaris.ebwizardry.entity.projectile.EntitySmokeBomb;
import binaris.ebwizardry.entity.projectile.EntitySparkBomb;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class WizardryEntities {
    public static final EntityType<EntitySparkBomb> ENTITY_SPARK_BOMB = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "spark_bomb"),
            FabricEntityTypeBuilder.<EntitySparkBomb>create(SpawnGroup.MISC, EntitySparkBomb::new)
                    .build());

    public static final EntityType<EntityFireBomb> ENTITY_FIRE_BOMB = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "fire_bomb"),
            FabricEntityTypeBuilder.<EntityFireBomb>create(SpawnGroup.MISC, EntityFireBomb::new)
                    .build());

    public static final EntityType<EntityPoisonBomb> ENTITY_POISON_BOMB = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "poison_bomb"),
            FabricEntityTypeBuilder.<EntityPoisonBomb>create(SpawnGroup.MISC, EntityPoisonBomb::new)
                    .build());
    public static final EntityType<EntitySmokeBomb> ENTITY_SMOKE_BOMB = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "smoke_bomb"),
            FabricEntityTypeBuilder.<EntitySmokeBomb>create(SpawnGroup.MISC, EntitySmokeBomb::new)
                    .build());

    public static void registerClient(){
        EntityRendererRegistry.register(WizardryEntities.ENTITY_SPARK_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_FIRE_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_POISON_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_SMOKE_BOMB, FlyingItemEntityRenderer::new);
    }
}
