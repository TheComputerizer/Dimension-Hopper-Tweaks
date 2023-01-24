package mods.thecomputerizer.dimhoppertweaks.common.objects.entity;

import mods.thecomputerizer.dimhoppertweaks.DimHopperTweaks;
import mods.thecomputerizer.dimhoppertweaks.common.objects.DimensionHopperSounds;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.ai.*;
import mods.thecomputerizer.dimhoppertweaks.common.objects.items.RealitySlasher;
import mods.thecomputerizer.dimhoppertweaks.network.PacketHandler;
import mods.thecomputerizer.dimhoppertweaks.network.packets.PacketUpdateBossShield;
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
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class EntityFinalBoss extends EntityLiving implements IAnimatable {
    private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_20)).setDarkenSky(true);
    private final AnimationFactory factory = new AnimationFactory(this);
    private String currentAnimation = "spawn";
    private final BossDialogue dialogueController;
    public int phase;
    private boolean doneWithIntro;
    private boolean invulnerable;
    private boolean isShieldUp;
    private final List<EntityPlayer> players;
    public final HashMap<HomingProjectile, UUID> projectiles;
    private final HashMap<String,Double> savedPlayerHealth;

    public EntityFinalBoss(World worldIn) {
        super(worldIn);
        this.setHealth(this.getMaxHealth());
        this.setSize(1F, 1.875F);
        this.isImmuneToFire = true;
        this.experienceValue = 999;
        this.dialogueController = new BossDialogue();
        this.phase = 0;
        this.doneWithIntro = false;
        this.invulnerable = true;
        this.isShieldUp = false;
        this.players = new ArrayList<>();
        this.projectiles = new HashMap<>();
        this.savedPlayerHealth = new HashMap<>();
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
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 64.0F));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6000000238418579D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(256.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1.0D);
    }

    @Override
    public float getEyeHeight() {
        return 1.875F;
    }

    @Override
    public void onKillCommand() {
        this.attackEntityFrom(new DamageSource("infinity"), Float.MAX_VALUE);
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (this.isShieldUp) teleportForcefield();
        this.projectiles.entrySet().removeIf(entry -> entry.getKey().isDead);
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if (this.isEntityInsideOpaqueBlock()) this.setPosition(this.posX, this.posY+8, this.posZ);
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public List<EntityPlayer> getTrackingPlayers() {
        return this.players;
    }

    @Override
    public void addTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
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
        if(getTrackingPlayers().size()==0) setDead();
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    public void updateShieldForPlayer(EntityPlayerMP player, boolean isShieldUp) {
        PacketHandler.NETWORK.sendTo(new PacketUpdateBossShield.PacketUpdateBossShieldMessage(this.getUniqueID().toString(),isShieldUp),player);
        this.isShieldUp = isShieldUp;
    }

    public void updateShield(boolean isShieldUp) {
        for(EntityPlayer player : this.players) if(player instanceof EntityPlayerMP) updateShieldForPlayer((EntityPlayerMP)player,isShieldUp);
    }

    public boolean getShieldUp() {
        return this.isShieldUp;
    }

    public void setIntroComplete() {
        if(this.phase==0) this.phase++;
        this.addPotionEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(24)),Integer.MAX_VALUE,5,false,false));
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1f, 1f);
    }

    public void setPhaseOneComplete() {
        if(this.phase==1) this.phase++;
        for (HomingProjectile projectile : this.projectiles.keySet()) projectile.setDead();
        this.updateShield(false);
        this.setInvulnerable(false);
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1f, 1f);
        this.setAnimation("damaged");
    }

    public void setPhaseTwoComplete() {
        if(this.phase==2) this.phase++;
        this.updateShield(true);
        this.setInvulnerable(true);
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1f, 1f);
    }

    public void setPhaseThreeComplete() {
        if(this.phase==3) this.phase++;
        for (HomingProjectile projectile : this.projectiles.keySet()) projectile.setDead();
        this.updateShield(false);
        this.setInvulnerable(false);
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1f, 1f);
        this.setAnimation("damaged");
    }

    public void setPhaseFourComplete() {
        if(this.phase==4) this.phase++;
        entityDropItem(getHeldItemMainhand(),0f);
        this.updateShield(true);
        this.setInvulnerable(true);
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1f, 1f);
    }

    public void setPhaseFiveComplete() {
        if(this.phase==5) this.phase++;
        this.updateShield(false);
        this.setInvulnerable(false);
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1f, 1f);
        this.setAnimation("damaged");
    }

    public void setPhaseSixComplete() {
        if(this.phase==6) this.phase++;
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1f, 1f);
    }

    public void setPhaseSevenComplete() {
        if(this.phase==7) this.onKillCommand();
    }

    protected boolean isEntityCloseEnough(Entity p, BlockPos pos, int max) {
        return isEntityCloseEnough(p,pos.getX(),pos.getY(),pos.getZ(),max);
    }

    protected boolean isEntityCloseEnough(Entity p, int x, int y, int z, int max) {
        return p.getPosition().getDistance(x, y, z) <= max;
    }

    public void teleportBehindPlayer(EntityPlayer player) {
        Vec3d vec = player.getLookVec();
        double dx = player.posX - (vec.x * 4);
        double dy = player.posY;
        double dz = player.posZ - (vec.z * 4);
        this.setPositionAndRotation(dx, dy, dz, player.cameraYaw, player.cameraPitch);
        this.setVelocity(0,0,0);
        this.world.playSound(null, dx, dy, dz, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
    }

    public void teleportRandomly() {
        double d0 = this.posX + (-32 + (this.rand.nextDouble()) * 64.0D);
        double d1 = this.posY + (-4 + (this.rand.nextDouble()) * 8.0D);
        double d2 = this.posZ + (-32 + (this.rand.nextDouble()) * 64.0D);
        this.setPosition(d0, d1, d2);
        this.setVelocity(0,0,0);
        this.world.playSound(null, d0, d1, d2, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
    }

    public void teleportOtherEntityForcefield(Entity entity) {
        if (isEntityCloseEnough(entity, (int) this.posX, (int) this.posY, (int) this.posZ, 8)) {
            double x = this.posX + (-32 + (this.rand.nextDouble()) * 64.0D);
            double y = this.posY + (-4 + (this.rand.nextDouble()) * 8.0D);
            double z = this.posZ + (-32 + (this.rand.nextDouble()) * 64.0D);
            entity.setPositionAndUpdate(x, y, z);
        }
    }

    public void teleportForcefield() {
        for (EntityPlayer p : this.players) {
            if(!(this.phase==5 && p.getHeldItemMainhand().getItem() instanceof RealitySlasher)) {
                if (isEntityCloseEnough(p, (int) this.posX, (int) this.posY, (int) this.posZ, 8)) {
                    double x = this.posX + (-32 + (this.rand.nextDouble()) * 64.0D);
                    double y = this.posY + (-4 + (this.rand.nextDouble()) * 8.0D);
                    double z = this.posZ + (-32 + (this.rand.nextDouble()) * 64.0D);
                    p.setPositionAndUpdate(x, y, z);
                    this.world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
                }
            }
        }
    }

    public void areaAttackSmall(EntityPlayer player, BlockPos pos, int phase) {
        if(phase==this.phase) {
            if (isEntityCloseEnough(player, pos.getX(), pos.getY(), pos.getZ(), 4)) {
                subtractPlayerHealth(player, 4d);
                this.world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), DimensionHopperSounds.SHORT_STATIC, this.getSoundCategory(), 1.0F, 1.0F);
            } else
                this.world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ENDEREYE_DEATH, this.getSoundCategory(), 1.0F, 1.0F);
        }
    }

    public float getHealthPercentage() {
        return getHealth()/getMaxHealth();
    }

    public void subtractPlayerHealth(EntityPlayer player, double amount) {
        if((player.getMaxHealth()-(amount+1))>0d) player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(player.getMaxHealth()-amount);
        else {
            this.setDropItemsWhenDead(false);
            this.updateShield(false);
            this.setDead();
        }
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
        return ""+ TextFormatting.DARK_PURPLE+I18n.format("entity.boss_name_"+(int) MathHelper.clamp((1f/(this.getHealth()/this.getMaxHealth()))/1.5f,1f,13f)+".name");
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.phase = compound.getInteger("DimensionHopperBoss_Phase");
        this.invulnerable = compound.getBoolean("DimensionHopperBoss_Invulnerable");
        updateShield(compound.getBoolean("DimensionHopperBoss_Shield"));
        this.doneWithIntro = compound.getBoolean("DimensionHopperBoss_Intro");
        int size = compound.getInteger("PlayerHealth_Size");
        NBTTagCompound compound1 = compound.getCompoundTag("DimensionHopperBoss_PlayerHealth");
        for(int i=1;i<size;i++) this.savedPlayerHealth.put(compound1.getString("PlayerHealth_UUID_"+i),compound1.getDouble("PlayerHealth_Health_"+i));
        this.setCustomNameTag(this.getObfuscatedNameProgress());
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("DimensionHopperBoss_Phase",this.phase);
        compound.setBoolean("DimensionHopperBoss_Invulnerable",this.invulnerable);
        compound.setBoolean("DimensionHopperBoss_Shield",this.isShieldUp);
        compound.setBoolean("DimensionHopperBoss_Intro",this.doneWithIntro);
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

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if(!this.invulnerable && !this.isShieldUp) {
            if (source instanceof DamageSourceInfinitySword && source.getTrueSource() instanceof EntityPlayer) {
                EntityPlayer p = (EntityPlayer) source.getTrueSource();
                if (p.getHeldItemMainhand().getItem() instanceof ItemSwordInfinity) {
                    this.damageBoss(source, amount, p);
                    return true;
                }
            }
        }
        else if(this.phase==5 && source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer) source.getTrueSource()).getHeldItemMainhand().getItem() instanceof RealitySlasher)
            setPhaseFiveComplete();
        else if(amount==Float.MAX_VALUE) this.damageBoss(source, amount, null);
        return false;
    }

    private void damageBoss(DamageSource source, float amount, EntityPlayer player) {
        this.damageEntity(source, amount);
        this.setCustomNameTag(this.getObfuscatedNameProgress());
        if (this.getHealth() <= 0.0F) {
            SoundEvent soundevent = this.getDeathSound();
            if (soundevent != null) this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
            this.onDeath(source);
        }
        else this.playHurtSound(source);
        if (player instanceof EntityPlayerMP) CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((EntityPlayerMP)player, this, source, amount, amount, false);
        this.setAnimation("damaged");
    }

    @Override
    @Nonnull
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSourceIn) {
        return DimensionHopperSounds.SHORT_STATIC;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "boss_controller",
                0, this::predicate));
    }

    public void setAnimation(String animation) {
        this.currentAnimation = animation;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(!this.currentAnimation.matches("idle"))
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(this.currentAnimation, ILoopType.EDefaultLoopTypes.PLAY_ONCE)
                    .addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        else if(event.getController().getCurrentAnimation()==null)
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    static class BossIntro extends EntityAIBase {
        private final EntityFinalBoss boss;
        private boolean active;
        public BossIntro(EntityFinalBoss boss) {
            this.setMutexBits(7);
            this.boss = boss;
            this.active = boss.phase==0;
        }

        @Override
        public void startExecuting() {

        }

        @Override
        public boolean shouldExecute() {
            return this.active;
        }
        @Override
        public boolean shouldContinueExecuting() {
            return this.active;
        }

        @Override
        public void updateTask() {
            this.active = boss.phase==0;
            if(this.active) {
                if (this.boss.ticksExisted < 600) {
                    if (this.boss.ticksExisted > 280) {
                        this.boss.setVelocity(0, 0.1, 0);
                        if (this.boss.ticksExisted == 290) {
                            for (EntityPlayer p : this.boss.players)
                                if (p.world.provider.getDimension() == this.boss.world.provider.getDimension())
                                    this.boss.dialogueController.introOne(p);
                        } else if (this.boss.ticksExisted == 390) {
                            for (EntityPlayer p : this.boss.players)
                                if (p.world.provider.getDimension() == this.boss.world.provider.getDimension())
                                    this.boss.dialogueController.introTwo(p);
                        } else if (this.boss.ticksExisted == 490) {
                            for (EntityPlayer p : this.boss.players)
                                if (p.world.provider.getDimension() == this.boss.world.provider.getDimension())
                                    this.boss.dialogueController.introThree(p);
                        } else if (this.boss.ticksExisted == 590) {
                            for (EntityPlayer p : this.boss.players)
                                if (p.world.provider.getDimension() == this.boss.world.provider.getDimension())
                                    this.boss.dialogueController.introFour(p);
                        }
                    }
                } else {
                    DimHopperTweaks.LOGGER.info("Finished invulnerable phase");
                    this.boss.setVelocity(0, 0, 0);
                    for (EntityPlayer player : this.boss.players)
                        this.boss.savedPlayerHealth.put(player.getUniqueID().toString(), player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue());
                    this.boss.doneWithIntro = true;
                    this.active = false;
                    this.boss.setIntroComplete();
                }
            }
        }
    }
}
