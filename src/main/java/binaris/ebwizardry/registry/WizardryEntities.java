package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.client.renderer.MagicArrowRenderer;
import binaris.ebwizardry.client.renderer.BlankRender;
import binaris.ebwizardry.client.renderer.MagicProjectileRenderer;
import binaris.ebwizardry.entity.living.BlazeMinionEntity;
import binaris.ebwizardry.entity.living.EntitySkeletonMinion;
import binaris.ebwizardry.entity.living.IceWraithEntity;
import binaris.ebwizardry.entity.living.ZombieMinionEntity;
import binaris.ebwizardry.entity.projectile.*;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.BlazeEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**Class used to register all the entities from the Electroblob's Wizardry mod*/
public final class WizardryEntities {
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
    public static final EntityType<EntityDart> ENTITY_DART = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "dart"),
            FabricEntityTypeBuilder.<EntityDart>create(SpawnGroup.MISC, EntityDart::new)
                    .build());

    @Deprecated
    // FIXME: Render
    public static final EntityType<EntityMagicFireball> ENTITY_MAGIC_FIREBALL = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "magic_fireball"),
            FabricEntityTypeBuilder.<EntityMagicFireball>create(SpawnGroup.MISC, EntityMagicFireball::new)
                    .build());
    @Deprecated
    // FIXME: Render
    public static final EntityType<EntityFireBolt> ENTITY_FIRE_BOLT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "fire_bolt"),
            FabricEntityTypeBuilder.<EntityFireBolt>create(SpawnGroup.MISC, EntityFireBolt::new)
                    .build());

    public static final EntityType<EntityIceShard> ENTITY_ICE_SHARD = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "ice_shard"),
            FabricEntityTypeBuilder.<EntityIceShard>create(SpawnGroup.MISC, EntityIceShard::new)
                    .build());
    @Deprecated
    // FIXME: Render
    public static final EntityType<EntitySpark> ENTITY_SPARK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "spark"),
            FabricEntityTypeBuilder.<EntitySpark>create(SpawnGroup.MISC, EntitySpark::new)
                    .build());
    public static final EntityType<EntityLightningArrow> ENTITY_LIGHTNING_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "lightning_arrow"),
            FabricEntityTypeBuilder.<EntityLightningArrow>create(SpawnGroup.MISC, EntityLightningArrow::new)
                    .build());
    public static final EntityType<EntityIceCharge> ENTITY_ICE_CHARGE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "ice_charge"),
            FabricEntityTypeBuilder.<EntityIceCharge>create(SpawnGroup.MISC, EntityIceCharge::new)
                    .build());
    public static final EntityType<EntityIceLance> ENTITY_ICE_LANCE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "ice_lance"),
            FabricEntityTypeBuilder.<EntityIceLance>create(SpawnGroup.MISC, EntityIceLance::new)
                    .build());


    public static final EntityType<ZombieMinionEntity> ENTITY_ZOMBIE_MINION = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "zombie_minion"),
            FabricEntityTypeBuilder.<ZombieMinionEntity>create(SpawnGroup.MISC, ZombieMinionEntity::new).dimensions(EntityType.ZOMBIE.getDimensions())
                    .build());
    public static final EntityType<BlazeMinionEntity> ENTITY_BLAZE_MINION = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "blaze_minion"),
            FabricEntityTypeBuilder.<BlazeMinionEntity>create(SpawnGroup.MISC, BlazeMinionEntity::new).dimensions(EntityType.BLAZE.getDimensions())
                    .build());
    public static final EntityType<IceWraithEntity> ENTITY_ICE_WRAITH = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "ice_wraith"),
            FabricEntityTypeBuilder.<IceWraithEntity>create(SpawnGroup.MISC, IceWraithEntity::new).dimensions(EntityType.BLAZE.getDimensions())
                    .build());
    public static final EntityType<EntitySkeletonMinion> ENTITY_SKELETON_MINION = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "skeleton_minion"),
            FabricEntityTypeBuilder.<EntitySkeletonMinion>create(SpawnGroup.MISC, EntitySkeletonMinion::new).dimensions(EntityType.SKELETON.getDimensions())
                    .build());

    public static final EntityType<EntityFlamecatcherArrow> FLAMECATCHER_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "flamecatcher_arrow"),
            FabricEntityTypeBuilder.<EntityFlamecatcherArrow>create(SpawnGroup.MISC, EntityFlamecatcherArrow::new)
                    .build());

    public static void registerClient(){
        EntityRendererRegistry.register(WizardryEntities.ENTITY_SPARK_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_FIRE_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_POISON_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_SMOKE_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_THUNDERBOLT, BlankRender::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_MAGIC_MISSILE, MagicArrowRenderer<EntityMagicMissile>::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_DART, MagicArrowRenderer<EntityDart>::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_MAGIC_FIREBALL, MagicProjectileRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_FIRE_BOLT, BlankRender::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_ICE_SHARD, MagicArrowRenderer<EntityIceShard>::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_SPARK, BlankRender::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_LIGHTNING_ARROW, MagicArrowRenderer<EntityLightningArrow>::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_ICE_CHARGE, MagicProjectileRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_ICE_LANCE, MagicArrowRenderer<EntityIceLance>::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_ZOMBIE_MINION, ZombieEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_BLAZE_MINION, BlazeEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_SKELETON_MINION, SkeletonEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_ICE_WRAITH, (ctx -> new BlazeEntityRenderer(ctx){
            @Override
            public Identifier getTexture(BlazeEntity blazeEntity) {
                return new Identifier(Wizardry.MODID, "textures/entity/ice_wraith.png");
            }
        }));
        EntityRendererRegistry.register(WizardryEntities.FLAMECATCHER_ARROW, MagicArrowRenderer<EntityFlamecatcherArrow>::new);
        FabricDefaultAttributeRegistry.register(ENTITY_SKELETON_MINION, EntitySkeletonMinion.createAbstractSkeletonAttributes());
        FabricDefaultAttributeRegistry.register(ENTITY_ZOMBIE_MINION, ZombieMinionEntity.createZombieAttributes());
        FabricDefaultAttributeRegistry.register(ENTITY_BLAZE_MINION, BlazeMinionEntity.createBlazeAttributes());
        FabricDefaultAttributeRegistry.register(ENTITY_ICE_WRAITH, IceWraithEntity.createBlazeAttributes());
    }
}
