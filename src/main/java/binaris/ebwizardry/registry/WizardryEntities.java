package binaris.ebwizardry.registry;

import binaris.ebwizardry.Wizardry;
import binaris.ebwizardry.client.renderer.*;
import binaris.ebwizardry.entity.construct.*;
import binaris.ebwizardry.entity.living.BlazeMinionEntity;
import binaris.ebwizardry.entity.living.EntitySkeletonMinion;
import binaris.ebwizardry.entity.living.IceWraithEntity;
import binaris.ebwizardry.entity.living.ZombieMinionEntity;
import binaris.ebwizardry.entity.projectile.*;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.*;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**Class used to register all the entities from the Electroblob's Wizardry mod*/
public final class WizardryEntities {
    // ================== TRACKING TYPES ==================
    // Just for the reference, originally from Electroblob's Wizardry 1.12.2
    // LIVING -> Range: 80, Interval: 3, Track Velocity: true
    // PROJECTILE -> Range: 64, Interval: 10, Track Velocity: true
    // CONSTRUCT -> Range: 160, Interval: 10, Track Velocity: false
    // ===================================================

    public static final EntityType<EntitySparkBomb> ENTITY_SPARK_BOMB = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "spark_bomb"),
            FabricEntityTypeBuilder.<EntitySparkBomb>create(SpawnGroup.MISC, EntitySparkBomb::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());

    public static final EntityType<EntityFireBomb> ENTITY_FIRE_BOMB = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "fire_bomb"),
            FabricEntityTypeBuilder.<EntityFireBomb>create(SpawnGroup.MISC, EntityFireBomb::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());

    public static final EntityType<EntityPoisonBomb> ENTITY_POISON_BOMB = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "poison_bomb"),
            FabricEntityTypeBuilder.<EntityPoisonBomb>create(SpawnGroup.MISC, EntityPoisonBomb::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());
    public static final EntityType<EntitySmokeBomb> ENTITY_SMOKE_BOMB = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "smoke_bomb"),
            FabricEntityTypeBuilder.<EntitySmokeBomb>create(SpawnGroup.MISC, EntitySmokeBomb::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());
    public static final EntityType<EntityThunderbolt> ENTITY_THUNDERBOLT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "thunderbolt"),
            FabricEntityTypeBuilder.<EntityThunderbolt>create(SpawnGroup.MISC, EntityThunderbolt::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());
    public static final EntityType<EntityMagicMissile> ENTITY_MAGIC_MISSILE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "magic_missile"),
            FabricEntityTypeBuilder.<EntityMagicMissile>create(SpawnGroup.MISC, EntityMagicMissile::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());
    public static final EntityType<EntityDart> ENTITY_DART = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "dart"),
            FabricEntityTypeBuilder.<EntityDart>create(SpawnGroup.MISC, EntityDart::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());

    public static final EntityType<EntityMagicFireball> ENTITY_MAGIC_FIREBALL = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "magic_fireball"),
            FabricEntityTypeBuilder.<EntityMagicFireball>create(SpawnGroup.MISC, EntityMagicFireball::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());
    public static final EntityType<EntityFireBolt> ENTITY_FIRE_BOLT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "fire_bolt"),
            FabricEntityTypeBuilder.<EntityFireBolt>create(SpawnGroup.MISC, EntityFireBolt::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());

    public static final EntityType<EntityIceShard> ENTITY_ICE_SHARD = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "ice_shard"),
            FabricEntityTypeBuilder.<EntityIceShard>create(SpawnGroup.MISC, EntityIceShard::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());
    public static final EntityType<EntitySpark> ENTITY_SPARK = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "spark"),
            FabricEntityTypeBuilder.<EntitySpark>create(SpawnGroup.MISC, EntitySpark::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());
    public static final EntityType<EntityLightningArrow> ENTITY_LIGHTNING_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "lightning_arrow"),
            FabricEntityTypeBuilder.<EntityLightningArrow>create(SpawnGroup.MISC, EntityLightningArrow::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());
    public static final EntityType<EntityIceCharge> ENTITY_ICE_CHARGE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "ice_charge"),
            FabricEntityTypeBuilder.<EntityIceCharge>create(SpawnGroup.MISC, EntityIceCharge::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());
    public static final EntityType<EntityIceLance> ENTITY_ICE_LANCE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "ice_lance"),
            FabricEntityTypeBuilder.<EntityIceLance>create(SpawnGroup.MISC, EntityIceLance::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
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
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());

    public static final EntityType<EntityConjuredArrow> CONJURED_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "conjured_arrow"),
            FabricEntityTypeBuilder.<EntityConjuredArrow>create(SpawnGroup.MISC, EntityConjuredArrow::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());

    public static final EntityType<EntityIceBall> ICE_BALL = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "ice_ball"),
            FabricEntityTypeBuilder.<EntityIceBall>create(SpawnGroup.MISC, EntityIceBall::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());

    public static final EntityType<EntityForceArrow> FORCE_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "force_arrow"),
            FabricEntityTypeBuilder.<EntityForceArrow>create(SpawnGroup.MISC, EntityForceArrow::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build());

    public static final EntityType<EntityFireSigil> FIRE_SIGIL = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "fire_sigil"),
            FabricEntityTypeBuilder.<EntityFireSigil>create(SpawnGroup.MISC, EntityFireSigil::new)
                    .dimensions(EntityDimensions.fixed(0.2f, 0.2f))
                    .trackRangeBlocks(160)
                    .trackedUpdateRate(1)
                    .build());

    public static final EntityType<EntityFrostSigil> FROST_SIGIL = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "frost_sigil"),
            FabricEntityTypeBuilder.<EntityFrostSigil>create(SpawnGroup.MISC, EntityFrostSigil::new)
                    .dimensions(EntityDimensions.fixed(0.2f, 0.2f))
                    .trackRangeBlocks(160)
                    .trackedUpdateRate(1)
                    .build());

    public static final EntityType<EntityLightningSigil> LIGHTNING_SIGIL = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "lightning_sigil"),
            FabricEntityTypeBuilder.<EntityLightningSigil>create(SpawnGroup.MISC, EntityLightningSigil::new)
                    .dimensions(EntityDimensions.fixed(0.2f, 0.2f))
                    .trackRangeBlocks(160)
                    .trackedUpdateRate(1)
                    .build());

    public static final EntityType<EntityBlizzard> BLIZZARD = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "blizzard"),
            FabricEntityTypeBuilder.<EntityBlizzard>create(SpawnGroup.MISC, EntityBlizzard::new)
                    .dimensions(EntityDimensions.fixed(3, 3))
                    .trackRangeBlocks(160)
                    .trackedUpdateRate(1)
                    .build());
    public static final EntityType<EntityIceSpike> ICE_SPIKE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Wizardry.MODID, "ice_spike"),
            FabricEntityTypeBuilder.<EntityIceSpike>create(SpawnGroup.MISC, EntityIceSpike::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 1.0f))
                    .trackRangeBlocks(160)
                    .trackedUpdateRate(1)
                    .build());

    public static void registerClient(){
        EntityRendererRegistry.register(WizardryEntities.ENTITY_SPARK_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_FIRE_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_POISON_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_SMOKE_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_THUNDERBOLT, BlankRender::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_MAGIC_MISSILE, MagicArrowRenderer<EntityMagicMissile>::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_DART, MagicArrowRenderer<EntityDart>::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_FIRE_BOLT, BlankRender::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_ICE_SHARD, MagicArrowRenderer<EntityIceShard>::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_SPARK, BlankRender::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_LIGHTNING_ARROW, MagicArrowRenderer<EntityLightningArrow>::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_ICE_CHARGE, (ctx -> new MagicProjectileRenderer<>(ctx,
                new Identifier(Wizardry.MODID, "textures/entity/ice_charge.png"))));
        EntityRendererRegistry.register(WizardryEntities.ENTITY_MAGIC_FIREBALL, (ctx -> new MagicProjectileRenderer<>(ctx,
                new Identifier(Wizardry.MODID, "textures/entity/magic_fireball.png"))));
        EntityRendererRegistry.register(WizardryEntities.ICE_BALL, (ctx -> new MagicProjectileRenderer<>(ctx,
                new Identifier(Wizardry.MODID, "textures/entity/ice_ball.png"))));


        EntityRendererRegistry.register(WizardryEntities.ENTITY_ICE_LANCE, MagicArrowRenderer<EntityIceLance>::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_ZOMBIE_MINION, ZombieEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_BLAZE_MINION, BlazeEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_SKELETON_MINION, SkeletonEntityRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.FIRE_SIGIL, (ctx -> new SigilRenderer(ctx,
                new Identifier(Wizardry.MODID, "textures/entity/fire_sigil.png"),0, true)));
        EntityRendererRegistry.register(WizardryEntities.FROST_SIGIL, (ctx -> new SigilRenderer(ctx,
                new Identifier(Wizardry.MODID, "textures/entity/frost_sigil.png"),0, true)));
        EntityRendererRegistry.register(WizardryEntities.LIGHTNING_SIGIL, (ctx -> new SigilRenderer(ctx,
                new Identifier(Wizardry.MODID, "textures/entity/lightning_sigil.png"),0, true)));
        EntityRendererRegistry.register(WizardryEntities.BLIZZARD, BlankRender::new);
        EntityRendererRegistry.register(WizardryEntities.ICE_SPIKE, IceSpikeRenderer::new);
        // TODO: ForceArrow Entity Renderer
        EntityRendererRegistry.register(WizardryEntities.FORCE_ARROW, MagicArrowRenderer<EntityForceArrow>::new);
        EntityRendererRegistry.register(WizardryEntities.ENTITY_ICE_WRAITH, (ctx -> new BlazeEntityRenderer(ctx){
            @Override
            public Identifier getTexture(BlazeEntity blazeEntity) {
                return new Identifier(Wizardry.MODID, "textures/entity/ice_wraith.png");
            }
        }));
        EntityRendererRegistry.register(WizardryEntities.CONJURED_ARROW, ConjureArrowRenderer::new);
        EntityRendererRegistry.register(WizardryEntities.FLAMECATCHER_ARROW, MagicArrowRenderer<EntityFlamecatcherArrow>::new);
        FabricDefaultAttributeRegistry.register(ENTITY_SKELETON_MINION, EntitySkeletonMinion.createAbstractSkeletonAttributes());
        FabricDefaultAttributeRegistry.register(ENTITY_ZOMBIE_MINION, ZombieMinionEntity.createZombieAttributes());
        FabricDefaultAttributeRegistry.register(ENTITY_BLAZE_MINION, BlazeMinionEntity.createBlazeAttributes());
        FabricDefaultAttributeRegistry.register(ENTITY_ICE_WRAITH, IceWraithEntity.createBlazeAttributes());
    }
}
