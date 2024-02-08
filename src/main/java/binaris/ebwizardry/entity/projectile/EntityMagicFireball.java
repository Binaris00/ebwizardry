package binaris.ebwizardry.entity.projectile;

import binaris.ebwizardry.registry.Spells;
import binaris.ebwizardry.registry.WizardryEntities;
import binaris.ebwizardry.registry.WizardryParticles;
import binaris.ebwizardry.spell.Spell;
import binaris.ebwizardry.util.ParticleBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityMagicFireball extends EntityMagicProjectile{

    protected static final int ACCELERATION_CONVERSION_FACTOR = 10;

    /** The damage dealt by this fireball. If this is -1, the damage for the fireball spell will be used instead;
     * this is for when the fireball is not from a spell (i.e. a vanilla fireball replacement). */
    protected float damage = -1;
    /** The number of seconds entities are set on fire by this fireball. If this is -1, the damage for the fireball
     * spell will be used instead; this is for when the fireball is not from a spell (i.e. a vanilla fireball replacement). */
    protected int burnDuration = -1;
    /** The lifetime of this fireball in ticks. This needs to be stored so that it can be changed for vanilla replacements,
     * or mobs that shoot fireballs would have severely reduced range! */
    protected int lifetime = 16;
    public EntityMagicFireball(World world) {
        super(WizardryEntities.ENTITY_MAGIC_FIREBALL, world);
    }

    public EntityMagicFireball(EntityType<EntityMagicFireball> entityMagicMissileEntityType, World world) {
        super(entityMagicMissileEntityType, world);
    }

    public void setDamage(float damage){
        this.damage = damage;
    }

    public void setBurnDuration(int burnDuration){
        this.burnDuration = burnDuration;
    }

    public double getDamage(){
        // I'm lazy, I'd rather not have an entire fireball spell class just to set two fields on the entity
        return damage == -1 ? Spells.FIRE_BALL.getFloatProperty(Spell.DAMAGE) : damage;
    }

    public int getBurnDuration(){
        return burnDuration == -1 ? Spells.FIRE_BALL.getIntProperty(Spell.BURN_DURATION) : burnDuration;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!getWorld().isClient) {
            if (hitResult instanceof EntityHitResult entityHitResult) {
                Entity entity = entityHitResult.getEntity();
                if (entity != null) {
                    float damage = (float) (getDamage() * 1.0F);

                    entity.damage(entity.getDamageSources().indirectMagic(this, entity), damage);
                    entity.setOnFireFor(getBurnDuration());
                }
            } else if (hitResult instanceof BlockHitResult blockHitResult) {
                BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());

                // Remember that canPlaceBlock should ALWAYS be the last thing that gets checked, or it risks other mods
                // thinking the block was placed even when a later condition prevents it, which may have side-effects
                this.getWorld().setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);
            }

            //this.playSound(WizardrySounds.ENTITY_MAGIC_FIREBALL_HIT, 2, 0.8f + rand.nextFloat() * 0.3f);

            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if(getWorld().isClient){
            for(int i=0; i<5; i++){
                // FIXME: SPAWNING PARTICLES
                double dx = (random.nextDouble() - 0.5) * getWidth();
                double dy = (random.nextDouble() - 0.5) * getHeight() + this.getHeight() / 2 - 0.1;
                double dz = (random.nextDouble() - 0.5) * getWidth();
                double v = 0.06;
                ParticleBuilder.create(WizardryParticles.MAGIC_FIRE)
                        .pos(this.getBlockPos().add((int) (dx - this.getVelocity().x/2), (int) dy, (int) (dz - this.getVelocity().z/2)))
                        .velocity(-v * dx, -v * dy, -v * dz).scale(getWidth()*2).time(10).spawn(getWorld());

                if(age > 1){
                    dx = (random.nextDouble() - 0.5) * getWidth();
                    dy = (random.nextDouble() - 0.5) * getHeight() + this.getHeight() / 2 - 0.1;
                    dz = (random.nextDouble() - 0.5) * getWidth();
                    ParticleBuilder.create(WizardryParticles.MAGIC_FIRE)
                            .pos(this.getBlockPos().add((int) (dx - this.getVelocity().x), (int) dy, (int) (dz - this.getVelocity().z)))
                            .velocity(-v * dx, -v * dy, -v * dz).scale(getWidth()*2).time(10).spawn(getWorld());
                }
            }
        }
    }

    @Override
    public boolean collidesWith(Entity other) {
        return true;
    }

    @Override
    public float getTargetingMargin() {
        return 1.0F;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if(this.isInvulnerableTo(source)){return false;} else {
            this.scheduleVelocityUpdate();
            if(source.getAttacker() != null){
                Vec3d vec3d = source.getAttacker().getPos();

                if(vec3d != null){
                    double speed = MathHelper.sqrt((float) (getVelocity().x * getVelocity().x + getVelocity().y * getVelocity().y + getVelocity().z * getVelocity().z));

                    double x = vec3d.x * speed;
                    double y = vec3d.y * speed;
                    double z = vec3d.z * speed;
                    this.setVelocity(x, y, z);

                    this.lifetime = 160;
                }
                if(source.getAttacker() instanceof LivingEntity livingEntity){
                    this.setOwner((livingEntity));
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public void setLifetime(int lifetime){
        this.lifetime = lifetime;
    }

    @Override
    public boolean hasNoGravity(){
        return true;
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putFloat("lifetime", lifetime);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        lifetime = nbt.getInt("lifetime");
        super.readNbt(nbt);
    }
}
