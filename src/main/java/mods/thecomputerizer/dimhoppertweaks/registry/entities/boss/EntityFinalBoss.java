package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.client.DHTClient;
import mods.thecomputerizer.dimhoppertweaks.registry.SoundRegistry;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.HomingProjectile;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.*;
import mods.thecomputerizer.dimhoppertweaks.registry.items.RealitySlasher;
import mods.thecomputerizer.dimhoppertweaks.network.PacketRenderBossAttack;
import mods.thecomputerizer.dimhoppertweaks.network.PacketUpdateBossRender;
import mods.thecomputerizer.theimpossiblelibrary.network.NetworkHandler;
import morph.avaritia.item.tools.ItemSwordInfinity;
import morph.avaritia.util.DamageSourceInfinitySword;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class EntityFinalBoss extends EntityLiving implements IAnimatable {
    private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_20)).setDarkenSky(true);
    private final AnimationFactory factory = new AnimationFactory(this);
    private final AnimationController<EntityFinalBoss> animationController;
    private String currentAnimation = "spawn";
    public int phase;
    public boolean boom;
    private boolean doneWithIntro;
    private boolean bossInvulnerable;
    private boolean isShieldUp;
    private final List<EntityPlayer> players;
    public final List<HomingProjectile> projectiles;
    private final HashMap<String,Double> savedPlayerHealth;
    private final HashMap<EntityPlayer, MutableInt> damageCooldown;
    private final List<DelayedAOE> aoeAttacks;
    private int projectileChargeTime;
    private int projectileChargeProgress;
    private Vec3d curLook;
    private Entity trackedEntity;
    public boolean isCharging;

    public EntityFinalBoss(World worldIn) {
        super(worldIn);
        this.setHealth(this.getMaxHealth());
        this.setSize(1f,1.875f);
        this.isImmuneToFire = true;
        this.experienceValue = 999;
        this.phase = 0;
        this.doneWithIntro = false;
        this.setEntityInvulnerable(true);
        this.bossInvulnerable = true;
        this.isShieldUp = false;
        this.players = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.savedPlayerHealth = new HashMap<>();
        this.damageCooldown = new HashMap<>();
        this.aoeAttacks = new ArrayList<>();
        this.animationController = new AnimationController<>(this, "boss_controller", 0, this::predicate);
        this.curLook = Vec3d.ZERO;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new BossIntro(this));
        this.tasks.addTask(1, new PhaseOne(this));
        this.tasks.addTask(2, new PhaseTwo(this));
        this.tasks.addTask(3, new PhaseThree(this));
        this.tasks.addTask(4, new PhaseFour(this));
        this.tasks.addTask(5, new PhaseFive(this));
        this.tasks.addTask(6, new PhaseSix(this));
        this.tasks.addTask(7, new PhaseSeven(this));
        this.tasks.addTask(9, new EntityAIWatchClosest(this,EntityPlayer.class,64f));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100d);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6000000238418579d);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(256d);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1d);
    }

    public double getTrackingRange() {
        return this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
    }

    @Override
    public float getEyeHeight() {
        return 1.875f;
    }

    @Override
    public int getVerticalFaceSpeed() {
        return 120;
    }

    @Override
    public int getHorizontalFaceSpeed() {
        return 30;
    }

    @Override
    public void onKillCommand() {
        this.damageBoss(new DamageSourceInfinitySword(null),Float.MAX_VALUE,null);
    }

    @Override
    protected void despawnEntity() {}

    @Override
    public void setDead() {
        super.setDead();
        DHTClient.FOG_DENSITY_OVERRIDE = -1f;
        this.onAddedToWorld();
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        DHTClient.FOG_DENSITY_OVERRIDE = 0f;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        for(MutableInt timer : this.damageCooldown.values())
            if(timer.getValue()>0) timer.decrement();
        if(this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) teleportUp();
        if(this.world.isRemote) {
            if(isChargingProjectile())
                this.projectileChargeProgress++;
        } else {
            if(this.curLook!=Vec3d.ZERO) lookAt();
            else if(Objects.nonNull(this.trackedEntity)) lookAtEntity();
            this.aoeAttacks.removeIf(attack -> attack.tick(this));
        }
        if(this.isShieldUp) teleportForcefield();
        this.projectiles.removeIf(projectile -> projectile.isDead);
        this.bossInfo.setPercent(getHealthPercentage());
        if(this.isCharging) this.chargingAttack();
    }

    public boolean isChargingProjectile() {
        return this.projectileChargeTime>0;
    }

    public void setProjectileCharge(int max) {
        if(this.projectileChargeTime!=max) this.projectileChargeProgress = 0;
        this.projectileChargeTime = max;
        if(!this.world.isRemote)
            NetworkHandler.sendToTracking(new PacketUpdateBossRender(this.getEntityId(),this.phase,
                    this.isShieldUp,this.currentAnimation,this.projectileChargeTime),this);
    }

    public float getProjectileChargePercent() {
        float max = this.projectileChargeTime;
        float cur = this.projectileChargeProgress;
        return MathHelper.clamp(cur/max,0f,1f);
    }

    public List<EntityPlayer> getTrackingPlayers() {
        return this.players;
    }

    @Override
    public void addTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
        if(this.players.isEmpty()) this.phase = 0;
        this.players.add(player);
        if(this.doneWithIntro) {
            this.savedPlayerHealth.put(player.getUniqueID().toString(),player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue());
            player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100d);
        }
        this.updateShieldForPlayer(player,this.getShieldUp());
    }

    @Override
    public void removeTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
        this.players.remove(player);
        if(this.savedPlayerHealth.containsKey(player.getUniqueID().toString()))
            player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.savedPlayerHealth.get(player.getUniqueID().toString()));
        if(getTrackingPlayers().isEmpty()) {
            this.setHealth(this.getMaxHealth());
            this.phase = -1;
        }
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    public void updateShieldForPlayer(EntityPlayerMP player, boolean isShieldUp) {
        new PacketUpdateBossRender(this.getEntityId(),this.phase,isShieldUp, this.currentAnimation,
                this.projectileChargeTime).addPlayers(player).send();
        this.isShieldUp = isShieldUp;
    }

    public void updateShield(boolean isShieldUp) {
        this.isShieldUp = isShieldUp;
        this.bossInvulnerable = this.phase==0 || isShieldUp;
        this.curLook = Vec3d.ZERO;
        if(!isShieldUp) {
            this.currentAnimation = "damaged";
            if(!this.world.isRemote) teleportRandomly();
        }
        this.projectileChargeTime = 0;
        if(!this.world.isRemote)
            NetworkHandler.sendToTracking(new PacketUpdateBossRender(this.getEntityId(),this.phase, isShieldUp,
                    this.currentAnimation,this.projectileChargeTime),this);
    }

    @SideOnly(Side.CLIENT)
    public void updateShieldClient(boolean isShieldUp) {
        this.isShieldUp = isShieldUp;
    }

    public boolean getShieldUp() {
        return this.isShieldUp;
    }

    public void finishPhase(int phase, boolean isShieldUp) {
        if(phase==7) this.onKillCommand();
        else {
            this.isCharging = false;
            this.phase = phase + 1;
            for (HomingProjectile projectile : this.projectiles) projectile.setDead();
            this.updateShield(isShieldUp);
            playSound(SoundEvents.ENTITY_WITHER_SPAWN,1f,1f);
        }
    }

    protected boolean isEntityCloseEnough(Entity entity, Vec3d posVec, int max) {
        return entity.getDistance(posVec.x,posVec.y,posVec.z)<=max;
    }

    public void teleportUp() {
        double y = this.posY+(this.rand.nextDouble()*8d);
        this.setPositionAndUpdate(this.posX,y,this.posZ);
        this.setVelocity(0,0,0);
        this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT,1f,1f);
    }

    public void teleportBehindPlayer(EntityPlayer player) {
        Vec3d vec = player.getLookVec();
        double x = player.posX-(vec.x*4);
        double y = player.posY;
        double z = player.posZ-(vec.z*4);
        this.setPositionAndRotation(x, y, z, player.cameraYaw, player.cameraPitch);
        this.setVelocity(0,0,0);
        this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT,1f,1f);
    }

    public void teleportRandomly() {
        double x = this.posX+(-32+(this.rand.nextDouble())*64d);
        double y = this.posY+(-4+(this.rand.nextDouble())*8d);
        double z = this.posZ+(-32+(this.rand.nextDouble())*64d);
        this.setPositionAndUpdate(x, y, z);
        this.setVelocity(0,0,0);
        this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT,1f,1f);
    }

    public void teleportForcefield() {
        for(EntityPlayer player : this.players) {
            if(!(this.phase==5 && player.getHeldItemMainhand().getItem() instanceof RealitySlasher)) {
                if (isEntityCloseEnough(player,this.getPositionVector(),8)) {
                    double x = this.posX+(-32+(this.rand.nextDouble())*64d);
                    double y = this.posY+(-4+(this.rand.nextDouble())*8d);
                    double z = this.posZ+(-32+(this.rand.nextDouble())*64d);
                    player.setPositionAndUpdate(x,y,z);
                    playSound(x,y,z,SoundEvents.ENTITY_ENDERMEN_TELEPORT);
                }
            }
        }
    }

    public void aoeAttack(Vec3d posVec, int range, boolean sound) {
        boolean hit = false;
        for(EntityPlayer player : this.players) {
            if(isEntityCloseEnough(player, posVec, range))
                if(subtractPlayerHealth(player,4d)) hit = true;
        }
        if(!hit && sound) playSound(posVec,SoundEvents.ENTITY_ENDEREYE_DEATH);
    }

    public void addAOECounter(List<Vec3d> vecList, int time, int range, int phase) {
        if(!vecList.isEmpty()) {
            this.aoeAttacks.add(new DelayedAOE(vecList,time,range,phase));
            NetworkHandler.sendToTracking(new PacketRenderBossAttack(vecList,time,range,this.getEntityId(),phase),this);
        }
    }

    public void chargingAttack() {
        double damageScale = Math.min(10d,Math.hypot(Math.hypot(this.motionX,this.motionY),this.motionZ)*10d);
        for(EntityPlayer player : this.players) {
            if(isEntityCloseEnough(player,this.getPositionVector(),1)) {
                DHTRef.LOGGER.error("CHARGING ATTACK DID {}",damageScale);
                subtractPlayerHealth(player,damageScale);
            }
        }
    }

    public float getHealthPercentage() {
        return getHealth()/getMaxHealth();
    }

    public boolean subtractPlayerHealth(EntityPlayer player, double amount) {
        if(amount>0) {
            if(!damageCooldown.containsKey(player) || damageCooldown.get(player).getValue()<=0) {
                double newHealth = player.getMaxHealth()-amount;
                if(newHealth>0) {
                    //player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(newHealth);
                    this.damageCooldown.put(player,new MutableInt(5));
                    playSound(player.getPositionVector(),SoundRegistry.BOSS_HURT);
                    return true;
                } else {
                    this.setDropItemsWhenDead(false);
                    //this.setDead();
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    public void setCustomNameTag(@Nonnull String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    private String getObfuscatedNameProgress() {
        float health = getHealthPercentage();
        int progress = health<=0 ? 13 : MathHelper.clamp(((int)((1f-health)/(1f/13f)))+1,1,13);
        return I18n.format("entity.boss_name_"+progress+".name");
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.phase = compound.getInteger("DimensionHopperBoss_Phase");
        updateShield(compound.getBoolean("DimensionHopperBoss_Shield"));
        this.doneWithIntro = compound.getBoolean("DimensionHopperBoss_Intro");
        this.curLook = readVec(compound.getCompoundTag("DimensionHopperBoss_CurLook"));
        int size = compound.getInteger("PlayerHealth_Size");
        NBTTagCompound compound1 = compound.getCompoundTag("DimensionHopperBoss_PlayerHealth");
        for(int i=1;i<size;i++)
            this.savedPlayerHealth.put(compound1.getString("PlayerHealth_UUID_"+i),compound1.getDouble("PlayerHealth_Health_"+i));
        this.setCustomNameTag(this.getObfuscatedNameProgress());
    }

    private Vec3d readVec(NBTTagCompound compound) {
        double x = compound.getDouble("vecX");
        double y = compound.getDouble("vecY");
        double z = compound.getDouble("vecZ");
        return x==0d && y==0d && z==0d ? Vec3d.ZERO : new Vec3d(x,y,z);
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("DimensionHopperBoss_Phase",this.phase);
        compound.setBoolean("DimensionHopperBoss_Shield",this.isShieldUp);
        compound.setBoolean("DimensionHopperBoss_Intro",this.doneWithIntro);
        compound.setTag("DimensionHopperBoss_CurLook",writeVec());
        NBTTagCompound compound1 = new NBTTagCompound();
        compound1.setInteger("PlayerHealth_Size",this.savedPlayerHealth.keySet().size());
        int index = 1;
        for(String uuid : this.savedPlayerHealth.keySet()) {
            compound1.setString("PlayerHealth_UUID_"+index,uuid);
            compound1.setDouble("PlayerHealth_Health_"+index,this.savedPlayerHealth.get(uuid));
            index++;
        }
        compound.setTag("DimensionHopperBoss_PlayerHealth",compound1);
    }

    private NBTTagCompound writeVec() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setDouble("vecX",this.curLook.x);
        compound.setDouble("vecY",this.curLook.y);
        compound.setDouble("vecZ",this.curLook.z);
        return compound;
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if(!this.bossInvulnerable && !this.isShieldUp) {
            if(source.getTrueSource() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)source.getTrueSource();
                if(player.getHeldItemMainhand().getItem() instanceof ItemSwordInfinity) {
                    this.damageBoss(new DamageSourceInfinitySword(player),amount,player);
                    return true;
                }
            }
        } else if(this.phase==5 && source.getTrueSource() instanceof EntityPlayer &&
                ((EntityPlayer)source.getTrueSource()).getHeldItemMainhand().getItem() instanceof RealitySlasher)
            this.boom = true;
        return false;
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
        this.setAnimation("damaged",true);
    }

    private void playSound(Vec3d posVec, SoundEvent sound) {
        playSound(posVec.x,posVec.y,posVec.z,sound);
    }

    private void playSound(double x, double y, double z, SoundEvent sound) {
        this.world.playSound(null,x,y,z,sound,this.getSoundCategory(),1f,1f);
    }

    @Override
    @Nonnull
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@Nonnull DamageSource source) {
        return SoundRegistry.BOSS_HURT;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(this.animationController);
    }

    public void setAnimation(String animation, boolean sendUpdate) {
        this.currentAnimation = animation;
        if(!this.world.isRemote) {
            if(animation.matches("damaged")) teleportRandomly();
            if(sendUpdate) NetworkHandler.sendToTracking(new PacketUpdateBossRender(this.getEntityId(),
                    this.phase,this.isShieldUp,animation,this.projectileChargeTime),this);
        }
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(this.currentAnimation.matches("spawn"))
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(this.currentAnimation, false)
                    .playAndHold("sword")
                    .loop("idle"));
        else event.getController().setAnimation(new AnimationBuilder()
                    .playOnce(this.currentAnimation)
                    .loop("idle"));
        return PlayState.CONTINUE;
    }

    private void lookAt() {
        this.getLookHelper().setLookPosition(this.curLook.x,this.curLook.y,this.curLook.z,
                (float)this.getHorizontalFaceSpeed(),(float)this.getVerticalFaceSpeed());
    }

    private void lookAtEntity() {
        this.getLookHelper().setLookPosition(this.trackedEntity.posX,this.trackedEntity.posY,this.trackedEntity.posZ,
                (float)this.getHorizontalFaceSpeed(),(float)this.getVerticalFaceSpeed());
    }

    public void updateLook(Vec3d newLook) {
        this.curLook = newLook;
    }

    public void updateTrackedEntity(@Nullable Entity toTrack) {
        this.trackedEntity = toTrack;
    }

    private ITextComponent combineText(ITextComponent ... components) {
        StringBuilder builder = new StringBuilder();
        for(ITextComponent component : components)
            builder.append(component.getFormattedText());
        return new TextComponentString(builder.toString());
    }

    @SuppressWarnings("SameParameterValue")
    private ITextComponent styleText(String literal, Style style) {
        return new TextComponentString(literal).setStyle(style);
    }

    private ITextComponent styleTranslation(String key, Style style) {
        return new TextComponentTranslation(key).setStyle(style);
    }

    private void dialogueMessage(int index) {
        Style style = new Style().setColor(TextFormatting.DARK_RED).setBold(true);
        for(EntityPlayer player : getTrackingPlayers())
            player.sendMessage(combineText(getDisplayName(),styleText(": ",style),
                    styleTranslation("entity.boss.dialogue_"+index,style)));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    static class BossIntro extends EntityAIBase {
        private final EntityFinalBoss boss;
        public BossIntro(EntityFinalBoss boss) {
            this.setMutexBits(7);
            this.boss = boss;
        }

        @Override
        public boolean shouldExecute() {
            return boss.phase==0;
        }
        @Override
        public boolean shouldContinueExecuting() {
            return boss.phase==0;
        }

        @Override
        public void updateTask() {
            if(this.boss.ticksExisted<329) {
                if(this.boss.ticksExisted==25) this.boss.setAnimation("sword",true);
                if(this.boss.ticksExisted>189) {
                    this.boss.setVelocity(0d,0.1d,0d);
                    if(this.boss.ticksExisted==190) {
                        this.boss.setAnimation("idle",true);
                        this.boss.dialogueMessage(1);
                        this.boss.updateTrackedEntity(this.boss.world.getClosestPlayerToEntity(this.boss,100d));
                    }
                }
            } else {
                this.boss.dialogueMessage(2);
                this.boss.setVelocity(0d,0d,0d);
                for(EntityPlayer player : this.boss.players)
                    this.boss.savedPlayerHealth.put(player.getUniqueID().toString(), player.getEntityAttribute(
                            SharedMonsterAttributes.MAX_HEALTH).getAttributeValue());
                this.boss.doneWithIntro = true;
                this.boss.finishPhase(0,true);
                this.boss.addPotionEffect(new PotionEffect(MobEffects.GLOWING,Integer.MAX_VALUE,5,false,false));
            }
        }
    }
}
