package mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.util.PacketHandler;
import mods.thecomputerizer.dimensionhoppertweaks.util.packets.PacketRenderBossAttack;
import mods.thecomputerizer.dimensionhoppertweaks.util.packets.PacketUpdateBossShield;
import morph.avaritia.item.tools.ItemSwordInfinity;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class EntityFinalBoss extends EntityLiving {
    private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_20)).setDarkenSky(true);
    private final BossDialogue dialogueController;
    public int phase;
    private boolean doneWithIntro;
    private boolean invulnerable;
    private boolean isShieldUp;
    private final List<EntityPlayer> players;
    public final List<HomingProjectile> projectiles;
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
        this.projectiles = new ArrayList<>();
        this.savedPlayerHealth = new HashMap<>();
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new BossIntro(this));
        this.tasks.addTask(1, new EntityFinalBoss.PhaseOne(this));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 64.0F));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6000000238418579D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(150.0D);
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
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
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
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
        this.players.remove(player);
        if(this.savedPlayerHealth.containsKey(player.getUniqueID().toString()))
            player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.savedPlayerHealth.get(player.getUniqueID().toString()));
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

    public void setPhaseOnceComplete() {
        if(this.phase==1) this.phase++;
    }

    protected boolean isEntityCloseEnough(Entity p, BlockPos pos, int max) {
        return isEntityCloseEnough(p,pos.getX(),pos.getY(),pos.getZ(),max);
    }

    protected boolean isEntityCloseEnough(Entity p, int x, int y, int z, int max) {
        return p.getPosition().getDistance(x, y, z) <= max;
    }

    protected void teleportRandomly() {
        double d0 = this.posX + (-32 + (this.rand.nextDouble()) * 64.0D);
        double d1 = this.posY + (-4 + (this.rand.nextDouble()) * 8.0D);
        double d2 = this.posZ + (-32 + (this.rand.nextDouble()) * 64.0D);
        this.setPosition(d0, d1, d2);
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

    protected void teleportForcefield() {
        for (EntityPlayer p : this.players) {
            if (isEntityCloseEnough(p, (int) this.posX, (int) this.posY, (int) this.posZ, 8)) {
                double x = this.posX + (-32 + (this.rand.nextDouble()) * 64.0D);
                double y = this.posY + (-4 + (this.rand.nextDouble()) * 8.0D);
                double z = this.posZ + (-32 + (this.rand.nextDouble()) * 64.0D);
                p.setPositionAndUpdate(x, y, z);
                this.world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            }
        }
    }

    protected void areaAttackSmall(EntityPlayer player, BlockPos pos) {
        if (isEntityCloseEnough(player, pos.getX(), pos.getY(), pos.getZ(), 4)) {
            subtractPlayerHealth(player,4d);
            this.world.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_WITHER_BREAK_BLOCK, this.getSoundCategory(), 1.0F, 1.0F);
        } else
            this.world.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ENDEREYE_DEATH, this.getSoundCategory(), 1.0F, 1.0F);
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

    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    private String getObfuscatedNameProgress() {
        return I18n.format("boss_name_"+(int) MathHelper.clamp((1f/(this.getHealth()/this.getMaxHealth()))/1.5f,1f,13f)+".name");
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
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
    public void writeEntityToNBT(NBTTagCompound compound) {
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
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(!this.invulnerable && !this.isShieldUp) {
            if (!(source instanceof EntityDamageSourceIndirect) && !source.isProjectile() && source.getTrueSource() instanceof EntityPlayer) {
                EntityPlayer p = (EntityPlayer) source.getTrueSource();
                if (p.getHeldItemMainhand().getItem() instanceof ItemSwordInfinity) {
                    amount = 10f;
                    source = new DamageSource("infinity");
                    this.damageBoss(source, amount, p);
                    return true;
                }
            }
        }
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
        public boolean shouldExecute() {
            return this.active;
        }
        @Override
        public boolean shouldContinueExecuting() {
            return this.active;
        }

        @Override
        public void updateTask() {
            if(this.active) {
                if (this.boss.ticksExisted < 400) {
                    this.boss.setVelocity(0, 0.1, 0);
                    if (this.boss.ticksExisted == 10) {
                        for (EntityPlayer p : this.boss.players)
                            if (p.world.provider.getDimension() == this.boss.world.provider.getDimension())
                                this.boss.dialogueController.introOne(p);
                    } else if (this.boss.ticksExisted == 110) {
                        for (EntityPlayer p : this.boss.players)
                            if (p.world.provider.getDimension() == this.boss.world.provider.getDimension())
                                this.boss.dialogueController.introTwo(p);
                    } else if (this.boss.ticksExisted == 210) {
                        for (EntityPlayer p : this.boss.players)
                            if (p.world.provider.getDimension() == this.boss.world.provider.getDimension())
                                this.boss.dialogueController.introThree(p);
                    } else if (this.boss.ticksExisted == 310) {
                        for (EntityPlayer p : this.boss.players)
                            if (p.world.provider.getDimension() == this.boss.world.provider.getDimension())
                                this.boss.dialogueController.introFour(p);
                    }
                } else {
                    DimensionHopperTweaks.LOGGER.info("Finished invulnerable phase");
                    this.boss.setVelocity(0, 0, 0);
                    for(EntityPlayer player : this.boss.players) this.boss.savedPlayerHealth.put(player.getUniqueID().toString(),player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue());
                    this.boss.doneWithIntro = true;
                    this.active = false;
                    this.boss.phase++;
                }
            }
        }
    }

    class PhaseOne extends EntityAIBase {
        private final EntityFinalBoss boss;
        private final HashMap<Integer, HashMap<EntityPlayer, BlockPos>> savedPositions;
        private final HashMap<BlockPos, Integer> explosionPositions;
        private boolean active;
        private int attackTimer;
        private int attackStartCounter;
        private int attackFinishCounter;
        private boolean teleported;
        private int attackLoop;

        public PhaseOne(EntityFinalBoss boss) {
            this.setMutexBits(7);
            this.boss = boss;
            this.savedPositions = new HashMap<>();
            this.explosionPositions = new HashMap<>();
            this.active = boss.phase<=1;
            this.attackTimer = -1;
            this.attackStartCounter = 1;
            this.attackFinishCounter = 1;
            this.teleported = false;
            this.attackLoop = 1;
        }

        @Override
        public boolean shouldExecute() {
            return this.active;
        }

        @Override
        public void startExecuting() {
            this.boss.updateShield(true);
        }

        @Override
        public boolean shouldContinueExecuting() {
            return this.active;
        }

        @Override
        public void updateTask() {
            if (attackLoop <= 3) {
                this.attackTimer++;
                if (this.attackTimer % 20 == 0 && this.attackStartCounter <= 3) {
                    this.teleported = false;
                    this.savedPositions.put(this.attackStartCounter, new HashMap<>());
                    for (EntityPlayer player : this.boss.players) {
                        this.savedPositions.get(this.attackStartCounter).put(player, player.getPosition());
                        if(player instanceof EntityPlayerMP)
                            PacketHandler.NETWORK.sendTo(new PacketRenderBossAttack.PacketRenderBossAttackMessage(player.getPosition(),0,4),(EntityPlayerMP) player);
                    }
                    this.attackStartCounter++;
                }
                if (this.attackTimer - 20 >= 0 && (this.attackTimer - 20) % 30 == 0 && this.attackFinishCounter <= 3) {
                    for (EntityPlayer player : this.savedPositions.get(this.attackFinishCounter).keySet()) {
                        this.boss.areaAttackSmall(player, this.savedPositions.get(this.attackFinishCounter).get(player));
                        this.explosionPositions.put(this.savedPositions.get(this.attackFinishCounter).get(player), 5);
                    }
                    this.attackFinishCounter++;
                }
                if (this.attackFinishCounter > 3 && !this.teleported) {
                    this.boss.teleportRandomly();
                    this.teleported = true;
                    this.attackStartCounter = 1;
                    this.attackFinishCounter = 1;
                    this.attackTimer = -1;
                    attackLoop++;
                }
            }
            else {
                for (EntityPlayer player : this.boss.players) {
                    player.sendMessage(new TextComponentString("spawning new homing thing"));
                    HomingProjectile projectile = new HomingProjectile(this.boss.world);
                    this.boss.world.spawnEntity(projectile);
                    projectile.setPositionAndUpdate(this.boss.posX, this.boss.posY + 8, this.boss.posZ);
                    this.boss.teleportOtherEntityForcefield(projectile);
                    projectile.setUpdate(player,this.boss,1);
                    this.boss.projectiles.add(projectile);
                }
                this.attackLoop = 1;
            }
            for(HomingProjectile projectile : this.boss.projectiles) if(!projectile.updateBlocked) projectile.updateCustom();
            for (BlockPos pos : this.explosionPositions.keySet())
                world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), explosionPositions.get(pos), true);
            this.explosionPositions.clear();
            if(this.boss.phase>1) {
                this.boss.updateShield(false);
                this.boss.invulnerable = false;
                this.active = false;
                this.boss.world.playSound(this.boss.posX,this.boss.posY,this.boss.posZ,SoundEvents.ENTITY_WITHER_SPAWN,SoundCategory.MASTER,1f,1f,false);
            }
        }
    }
}
