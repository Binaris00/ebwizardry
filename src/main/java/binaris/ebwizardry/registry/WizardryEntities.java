package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.client.renderer.ArrowRenderer;
import binaris.ebwizardry.client.renderer.BlankRender;
import binaris.ebwizardry.entity.projectile.*;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
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
    public static final EntityType<EntityThunderbolt> ENTITY_THUNDERBOLT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "thunderbolt"),
            FabricEntityTypeBuilder.<EntityThunderbolt>create(SpawnGroup.MISC, EntityThunderbolt::new)
                    .build());
    public static final EntityType<EntityMagicMissile> ENTITY_MAGIC_MISSILE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "magic_missile"),
            FabricEntityTypeBuilder.<EntityMagicMissile>create(SpawnGroup.MISC, EntityMagicMissile::new)
                    .build());

    public static void registerClient(){
        EntityRendererRegistry.register(WizardryEntities.ENTITY_SPARK_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_FIRE_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_POISON_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_SMOKE_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_THUNDERBOLT, BlankRender::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_MAGIC_MISSILE, ArrowRenderer::new);
    }
}
