package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss;

import mods.thecomputerizer.dimhoppertweaks.client.DHTClient;
import mods.thecomputerizer.dimhoppertweaks.network.PacketRenderBossAttack;
import mods.thecomputerizer.dimhoppertweaks.registry.SoundRegistry;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.HomingProjectile;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.*;
import mods.thecomputerizer.dimhoppertweaks.registry.items.RealitySlasher;
import mods.thecomputerizer.dimhoppertweaks.util.DamageSourceFinalBoss;
import mods.thecomputerizer.theimpossiblelibrary.network.NetworkHandler;
import morph.avaritia.handler.AvaritiaEventHandler;
import morph.avaritia.item.tools.ItemSwordInfinity;
import morph.avaritia.util.DamageSourceInfinitySword;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.*;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.silentchaos512.scalinghealth.config.Config;
import org.apache.commons.lang3.mutable.MutableInt;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings("deprecation")
public class EntityFinalBoss extends EntityLiving implements IAnimatable {

    private static final DataParameter<String> ANIMATION_STATE = EntityDataManager.createKey(EntityFinalBoss.class,DataSerializers.STRING);
    private static final DataParameter<Integer> PHASE_STATE = EntityDataManager.createKey(EntityFinalBoss.class,DataSerializers.VARINT);
    private static final DataParameter<Boolean> SHIELD_STATE = EntityDataManager.createKey(EntityFinalBoss.class,DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CHARGING_STATE = EntityDataManager.createKey(EntityFinalBoss.class,DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TRACKED_ENTITY_ID = EntityDataManager.createKey(EntityFinalBoss.class,DataSerializers.VARINT);
    private static final DataParameter<Integer> PROJECTILE_CHARGE_STATE = EntityDataManager.createKey(EntityFinalBoss.class,DataSerializers.VARINT);
    private static final DataParameter<Integer> PROJECTILE_MAX_CHARGE = EntityDataManager.createKey(EntityFinalBoss.class,DataSerializers.VARINT);
    private final BossInfoServer bossInfo;
    private final AnimationFactory factory = new AnimationFactory(this);
    private final AnimationController<EntityFinalBoss> animationController;
    private final List<EntityPlayer> players;
    public final List<HomingProjectile> projectiles;
    private final Map<EntityPlayer,MutableInt> damageCooldown;
    private final List<DelayedAOE> aoeAttacks;
    public boolean boom;
    private Vec3d curLook;
    private Entity trackedEntity;

    public EntityFinalBoss(World worldIn) {
        super(worldIn);
        this.setHealth(this.getMaxHealth());
        this.setSize(1f,1.875f);
        this.setEntityInvulnerable(true);
        this.bossInfo = (BossInfoServer)new BossInfoServer(this.getDisplayName(),BossInfo.Color.RED,BossInfo.Overlay.NOTCHED_20).setDarkenSky(true);
        this.isImmuneToFire = true;
        this.experienceValue = 999;
        this.players = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.damageCooldown = new HashMap<>();
        this.aoeAttacks = new ArrayList<>();
        this.animationController = new AnimationController<>(this,"boss_controller",0,this::predicate);
        this.curLook = Vec3d.ZERO;
        Config.Player.Health.allowModify = false;
    }

    public void addAOECounter(List<Vec3d> vecList, int time, int range, int phase) {
        if(!vecList.isEmpty()) {
            this.aoeAttacks.add(new DelayedAOE(vecList,time,range,phase));
            NetworkHandler.sendToTracking(new PacketRenderBossAttack(vecList,time,range,this.getEntityId(),phase),this);
        }
    }

    @Override
    public void addTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
        if(this.players.isEmpty()) setPhase(0);
        this.players.add(player);
        if(getPhase()>0) setMaxPlayerHealth(player);
    }

    public void aoeAttack(Vec3d posVec, int range, boolean sound) {
        boolean hit = false;
        for(EntityPlayer player : this.players)
            if(isEntityCloseEnough(player,posVec,range) && subtractPlayerHealth(player,4d)) hit = true;
        if(!hit && sound) playSound(posVec,SoundEvents.ENTITY_ENDEREYE_DEATH);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(250d);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6d);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(256d);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1d);
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if(!isInvulnerable()) {
            if(source.getTrueSource() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)source.getTrueSource();
                if(player.getHeldItemMainhand().getItem() instanceof ItemSwordInfinity) {
                    this.damageBoss(new DamageSourceInfinitySword(player),amount,player);
                    return true;
                }
            }
        } else if(isPhase(5) && source.getTrueSource() instanceof EntityPlayer &&
                ((EntityPlayer)source.getTrueSource()).getHeldItemMainhand().getItem() instanceof RealitySlasher)
            this.boom = true;
        return false;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    public void chargingAttack() {
        double damageScale = Math.min(10d,Math.hypot(Math.hypot(this.motionX,this.motionY),this.motionZ)*10d);
        for(EntityPlayer player : this.players)
            if(isEntityCloseEnough(player,this.getPositionVector(),1))
                subtractPlayerHealth(player,damageScale);
    }

    private ITextComponent combineText(ITextComponent ... components) {
        StringBuilder builder = new StringBuilder();
        for(ITextComponent component : components)
            builder.append(component.getFormattedText());
        return new TextComponentString(builder.toString());
    }

    private void damageBoss(DamageSource source, float amount, EntityPlayer player) {
        amount = ForgeHooks.onLivingHurt(this,source,amount);
        this.getCombatTracker().trackDamage(source,this.getHealth(),amount);
        this.setHealth(MathHelper.clamp(this.getHealth()-amount,0f,this.getMaxHealth()));
        this.setCustomNameTag(this.getObfuscatedNameProgress());
        if(this.getHealth()<=0f) {
            SoundEvent sound = this.getDeathSound();
            if(Objects.nonNull(sound)) this.playSound(sound,this.getSoundVolume(),this.getSoundPitch());
            this.onDeath(source);
        }
        else this.playHurtSound(source);
        if(player instanceof EntityPlayerMP) CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((EntityPlayerMP)player,
                this,source,amount,amount,false);
        this.setAnimationState("damaged");
    }

    @Override
    protected void despawnEntity() {}

    private void dialogueMessage(int index) {
        Style style = new Style().setColor(TextFormatting.DARK_RED).setBold(true);
        for(EntityPlayer player : getTrackingPlayers())
            player.sendMessage(combineText(getDisplayName(),styleText(": ",style),
                    styleTranslation("entity.boss.dialogue_"+index,style)));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ANIMATION_STATE,"spawn");
        this.dataManager.register(PHASE_STATE,0);
        this.dataManager.register(SHIELD_STATE,false);
        this.dataManager.register(CHARGING_STATE,false);
        this.dataManager.register(TRACKED_ENTITY_ID,-1);
        this.dataManager.register(PROJECTILE_CHARGE_STATE,-1);
        this.dataManager.register(PROJECTILE_MAX_CHARGE,10);
    }

    public void finishPhase(int phase, boolean isShieldUp) {
        if(phase==7) this.onKillCommand();
        else {
            setCharging(false);
            setPhase(getPhase()+1);
            for(HomingProjectile projectile : this.projectiles) projectile.setDead();
            this.updateShield(isShieldUp);
            this.world.playSound(null,this.posX,this.posY,this.posZ,SoundRegistry.BELL,SoundCategory.HOSTILE,1f,1f);
        }
    }

    public String getAnimationState() {
        return this.dataManager.get(ANIMATION_STATE);
    }

    @Override
    public float getEyeHeight() {
        return 1.875f;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public float getHealthPercentage() {
        return getHealth()/getMaxHealth();
    }

    @Override
    public int getHorizontalFaceSpeed() {
        return 30;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@Nonnull DamageSource source) {
        return SoundRegistry.BOSS_HURT;
    }

    private String getObfuscatedNameProgress() {
        float health = getHealthPercentage();
        int progress = health<=0 ? 13 : MathHelper.clamp(((int)((1f-health)/(1f/13f)))+1,1,13);
        return I18n.format("entity.boss_name_"+progress+".name");
    }

    public int getPhase() {
        return this.dataManager.get(PHASE_STATE);
    }

    public float getProjectileChargePercent() {
        float max = this.dataManager.get(PROJECTILE_MAX_CHARGE);
        float cur = this.dataManager.get(PROJECTILE_CHARGE_STATE);
        return MathHelper.clamp(cur/max,0f,1f);
    }

    @Override
    public @Nonnull SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    public List<EntityPlayer> getTrackingPlayers() {
        return this.players;
    }

    public double getTrackingRange() {
        return this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
    }

    @Override
    public int getVerticalFaceSpeed() {
        return 120;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    public boolean hasShield() {
        return this.dataManager.get(SHIELD_STATE);
    }

    private boolean ignoresDamageAnimation(String animation) {
        return animation.equals("pointsword") || animation.equals("energyrelease");
    }

    public void incrementProjectileProgress() {
        this.dataManager.set(PROJECTILE_CHARGE_STATE,this.dataManager.get(PROJECTILE_CHARGE_STATE)+1);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0,new BossIntro(this));
        this.tasks.addTask(1,new PhaseOne(this));
        this.tasks.addTask(2,new PhaseTwo(this));
        this.tasks.addTask(3,new PhaseThree(this));
        this.tasks.addTask(4,new PhaseFour(this));
        this.tasks.addTask(5,new PhaseFive(this));
        this.tasks.addTask(6,new PhaseSix(this));
        this.tasks.addTask(7,new PhaseSeven(this));
        this.tasks.addTask(9,new EntityAIWatchClosest(this,EntityPlayer.class,64f));
    }

    public boolean isCharging() {
        return this.dataManager.get(CHARGING_STATE);
    }

    public boolean isChargingProjectile() {
        return this.dataManager.get(PROJECTILE_CHARGE_STATE)>=0;
    }

    protected boolean isEntityCloseEnough(Entity entity, Vec3d posVec, int max) {
        return entity.getDistance(posVec.x,posVec.y,posVec.z)<=max;
    }

    public boolean isInvulnerable() {
        return getPhase()<=0 || hasShield();
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    public boolean isPhase(int phase) {
        return getPhase()==phase;
    }

    private void lookAt() {
        this.getLookHelper().setLookPosition(this.curLook.x,this.curLook.y,this.curLook.z,
                (float)this.getHorizontalFaceSpeed(),(float)this.getVerticalFaceSpeed());
    }

    private void lookAtEntity() {
        this.getLookHelper().setLookPosition(this.trackedEntity.posX,this.trackedEntity.posY,this.trackedEntity.posZ,
                (float)this.getHorizontalFaceSpeed(),(float)this.getVerticalFaceSpeed());
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        Config.Player.Health.allowModify = this.dead;
        DHTClient.FOG_DENSITY_OVERRIDE = 0f;
    }

    @Override
    public void onKillCommand() {
        this.damageBoss(new DamageSourceInfinitySword(null),Float.MAX_VALUE,null);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        for(MutableInt timer : this.damageCooldown.values())
            if(timer.getValue()>0) timer.decrement();
        if(this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) teleportUp();
        if(this.world.isRemote) {
            if(isChargingProjectile()) incrementProjectileProgress();
        } else {
            if(this.curLook!=Vec3d.ZERO) lookAt();
            else if(Objects.nonNull(this.trackedEntity)) lookAtEntity();
            this.aoeAttacks.removeIf(attack -> attack.tick(this));
        }
        if(hasShield()) teleportForcefield();
        this.projectiles.removeIf(projectile -> projectile.isDead);
        this.bossInfo.setPercent(getHealthPercentage());
        if(isCharging()) this.chargingAttack();
    }

    private void playSound(Vec3d posVec, SoundEvent sound) {
        playSound(posVec.x,posVec.y,posVec.z,sound);
    }

    private void playSound(double x, double y, double z, SoundEvent sound) {
        this.world.playSound(null,x,y,z,sound,this.getSoundCategory(),1f,1f);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        String animation = getAnimationState();
        if(animation.equals("spawn"))
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(animation,false)
                    .playAndHold("sword")
                    .loop("idle"));
        else event.getController().setAnimation(new AnimationBuilder()
                .playOnce(animation)
                .loop("idle"));
        return PlayState.CONTINUE;
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        setPhase(tag.getInteger("bossPhase"));
        setShield(tag.getBoolean("hasShield"));
        this.curLook = readVec(tag.getCompoundTag("curLook"));
        this.setCustomNameTag(this.getObfuscatedNameProgress());
    }

    private Vec3d readVec(NBTTagCompound tag) {
        double x = tag.getDouble("vecX");
        double y = tag.getDouble("vecY");
        double z = tag.getDouble("vecZ");
        return x==0d && y==0d && z==0d ? Vec3d.ZERO : new Vec3d(x,y,z);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(this.animationController);
    }

    @Override
    public void removeTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
        this.players.remove(player);
        if(getTrackingPlayers().isEmpty()) {
            this.setHealth(this.getMaxHealth());
            setPhase(0);
        }
    }

    public void setAnimationState(String animation) {
        if(animation.equals("damaged")) {
            teleportRandomly();
            if(ignoresDamageAnimation(getAnimationState())) return;
        }
        this.dataManager.set(ANIMATION_STATE,animation);
    }

    public void setCharging(boolean isCharging) {
        this.dataManager.set(CHARGING_STATE,isCharging);
    }

    public void setCustomNameTag(@Nonnull String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public void setDead() {
        super.setDead();
        DHTClient.FOG_DENSITY_OVERRIDE = -1f;
        this.onAddedToWorld();
    }

    public void setMaxPlayerHealth(EntityPlayer player) {
        double health = GameStageHelper.hasStage(player,"hardcore") ? 1d : 100d;
        if(player.getMaxHealth()>health) {
            player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).removeAllModifiers();
            player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
        }
    }

    private void setPhase(int phase) {
        this.dataManager.set(PHASE_STATE,phase);
    }

    public void setProjectileCharge(int max) {
        this.dataManager.set(PROJECTILE_MAX_CHARGE,max);
        this.dataManager.set(PROJECTILE_CHARGE_STATE,max==0 ? -1 : 0);
    }

    public void setTrackingHealth() {
        for(EntityPlayer player : this.players) setMaxPlayerHealth(player);
    }

    public void setShield(boolean hasShield) {
        this.dataManager.set(SHIELD_STATE,hasShield);
    }

    public void spawnProjectile(Vec3d spawnPos, Vec3d target, float speed) {
        HomingProjectile projectile = new HomingProjectile(this.world);
        projectile.setPosition(spawnPos.x,spawnPos.y,spawnPos.z);
        this.world.spawnEntity(projectile);
        this.projectiles.add(projectile);
        if(target!=Vec3d.ZERO) projectile.setUpdate(this,target,speed);
    }

    @SuppressWarnings("SameParameterValue")
    private ITextComponent styleText(String literal, Style style) {
        return new TextComponentString(literal).setStyle(style);
    }

    private ITextComponent styleTranslation(String key, Style style) {
        return new TextComponentTranslation(key).setStyle(style);
    }

    public boolean subtractPlayerHealth(EntityPlayer player, double amount) {
        if(amount>0) {
            if(!AvaritiaEventHandler.isInfinite(player)) {
                player.onDeath(new DamageSourceFinalBoss(this));
                return true;
            }
            if(!this.damageCooldown.containsKey(player) || this.damageCooldown.get(player).getValue()<=0) {
                double newHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue()-amount;
                if(newHealth>0) {
                    player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(newHealth);
                    this.damageCooldown.put(player,new MutableInt(5));
                    playSound(player.getPositionVector(),SoundRegistry.BOSS_HURT);
                    return true;
                } else {
                    this.setDropItemsWhenDead(false);
                    player.onDeath(new DamageSourceFinalBoss(this));
                    this.setDead();
                    return false;
                }
            }
        }
        return false;
    }

    public void teleportBehindPlayer(EntityPlayer player) {
        Vec3d vec = player.getLookVec();
        double x = player.posX-(vec.x*4d);
        double y = player.posY;
        double z = player.posZ-(vec.z*4d);
        this.setPositionAndRotation(x,y,z,player.cameraYaw,player.cameraPitch);
        this.setVelocity(0d,0d,0d);
        this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT,1f,1f);
    }

    public void teleportForcefield() {
        for(EntityPlayer player : this.players) {
            if(!(isPhase(5) && player.getHeldItemMainhand().getItem() instanceof RealitySlasher)) {
                if(isEntityCloseEnough(player,this.getPositionVector(),8)) {
                    double x = this.posX+(-32d+(this.rand.nextDouble())*64d);
                    double y = this.posY+(-4d+(this.rand.nextDouble())*8d);
                    double z = this.posZ+(-32d+(this.rand.nextDouble())*64d);
                    player.setPositionAndUpdate(x,y,z);
                    playSound(x,y,z,SoundEvents.ENTITY_ENDERMEN_TELEPORT);
                }
            }
        }
    }

    public void teleportRandomly() {
        double x = this.posX+(-32d+(this.rand.nextDouble())*64d);
        double y = this.posY+(-4d+(this.rand.nextDouble())*8d);
        double z = this.posZ+(-32d+(this.rand.nextDouble())*64d);
        this.setPositionAndUpdate(x,y,z);
        this.setVelocity(0d,0d,0d);
        this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT,1f,1f);
    }

    public void teleportUp() {
        double y = this.posY+(this.rand.nextDouble()*8d);
        this.setPositionAndUpdate(this.posX,y,this.posZ);
        this.setVelocity(0d,0d,0d);
        this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT,1f,1f);
    }

    public void updateLook(Vec3d newLook) {
        this.curLook = newLook;
    }

    public void updateShield(boolean hasShield) {
        setShield(hasShield);
        this.curLook = Vec3d.ZERO;
        if(!hasShield) this.setAnimationState("damaged");
        setProjectileCharge(0);
    }

    public void updateTrackedEntity(@Nullable Entity toTrack) {
        this.trackedEntity = toTrack;
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("bossPhase",getPhase());
        tag.setBoolean("hasShield",hasShield());
        tag.setTag("curLook",writeVec());
    }

    private NBTTagCompound writeVec() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setDouble("vecX",this.curLook.x);
        compound.setDouble("vecY",this.curLook.y);
        compound.setDouble("vecZ",this.curLook.z);
        return compound;
    }

    static class BossIntro extends EntityAIBase {
        private final EntityFinalBoss boss;

        public BossIntro(EntityFinalBoss boss) {
            this.setMutexBits(7);
            this.boss = boss;
        }

        @Override
        public boolean shouldExecute() {
            return this.boss.isPhase(0);
        }
        @Override
        public boolean shouldContinueExecuting() {
            return this.boss.isPhase(0);
        }

        @Override
        public void updateTask() {
            if(this.boss.ticksExisted<329) {
                if(this.boss.ticksExisted==25) this.boss.setAnimationState("sword");
                if(this.boss.ticksExisted>189) {
                    this.boss.setVelocity(0d,0.1d,0d);
                    if(this.boss.ticksExisted==190) {
                        this.boss.setAnimationState("idle");
                        this.boss.dialogueMessage(1);
                        this.boss.updateTrackedEntity(this.boss.world.getClosestPlayerToEntity(this.boss,100d));
                    }
                }
            } else {
                this.boss.dialogueMessage(2);
                this.boss.setVelocity(0d,0d,0d);
                this.boss.setTrackingHealth();
                this.boss.finishPhase(0,true);
                this.boss.addPotionEffect(new PotionEffect(MobEffects.GLOWING,Integer.MAX_VALUE,5,false,false));
            }
        }
    }
}
