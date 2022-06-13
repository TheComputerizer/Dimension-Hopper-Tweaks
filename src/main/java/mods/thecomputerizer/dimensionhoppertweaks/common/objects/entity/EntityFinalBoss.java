package mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.client.entity.render.RenderEvents;
import mods.thecomputerizer.dimensionhoppertweaks.util.PacketHandler;
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
    private final Random random;
    private int phase;
    private boolean doneWithIntro;
    private boolean invulnerable;
    private boolean isShieldUp;
    private final List<EntityPlayer> players;
    private final HashMap<String,Double> savedPlayerHealth;

    public EntityFinalBoss(World worldIn) {
        super(worldIn);
        this.setHealth(this.getMaxHealth());
        this.setSize(1F, 1.875F);
        this.isImmuneToFire = true;
        this.experienceValue = 999;
        this.dialogueController = new BossDialogue();
        this.random = new Random();
        this.phase = 0;
        this.doneWithIntro = false;
        this.invulnerable = true;
        this.isShieldUp = false;
        this.players = new ArrayList<>();
        this.savedPlayerHealth = new HashMap<>();
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityFinalBoss.BossIntro(this));
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

    private void subtractPlayerHealth(EntityPlayer player, double amount) {
        if(player.getMaxHealth()-(amount+1)<=0d) player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(player.getMaxHealth()-amount);
        else {
            this.setDropItemsWhenDead(false);
            this.isShieldUp = false;
            RenderEvents.bossShields.put(this.getUniqueID().toString(),this.isShieldUp);
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
        this.isShieldUp = compound.getBoolean("DimensionHopperBoss_Shield");
        updateShield(this.getShieldUp());
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

    class BossIntro extends EntityAIBase {
        private final EntityFinalBoss boss;
        private boolean active;
        public BossIntro(EntityFinalBoss boss) {
            this.setMutexBits(7);
            this.boss = boss;
            this.active = true;
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
                }
            }
        }
    }

    class PhaseOne extends EntityAIBase {
        private final EntityFinalBoss boss;
        private final HashMap<Integer, HashMap<EntityPlayer, BlockPos>> savedPositions;
        private final HashMap<EntityPlayer, BlockPos.MutableBlockPos> homingPositions;
        private final HashMap<BlockPos, String> particlePositions;
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
            this.homingPositions = new HashMap<>();
            this.particlePositions = new HashMap<>();
            this.explosionPositions = new HashMap<>();
            this.active = true;
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
            this.boss.isShieldUp = true;
            this.boss.updateShield(this.boss.getShieldUp());
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
                        this.particlePositions.put(player.getPosition(), "red");
                    }
                    this.attackStartCounter++;
                }
                if (this.attackTimer - 20 >= 0 && (this.attackTimer - 20) % 30 == 0 && this.attackFinishCounter <= 3) {
                    for (EntityPlayer player : this.savedPositions.get(this.attackFinishCounter).keySet()) {
                        this.boss.areaAttackSmall(player, this.savedPositions.get(this.attackFinishCounter).get(player));
                        this.explosionPositions.put(this.savedPositions.get(this.attackFinishCounter).get(player), 5);
                        this.particlePositions.remove(this.savedPositions.get(this.attackFinishCounter).get(player));
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
            } else if (this.homingPositions.isEmpty() || this.homingPositions.keySet().size()!=this.boss.players.size()) {
                for (EntityPlayer player : this.boss.players) {
                    if(!this.homingPositions.containsKey(player)) {
                        BlockPos.MutableBlockPos spawn = new BlockPos.MutableBlockPos((int) this.boss.posX, (int) this.boss.posY + 8, (int) this.boss.posZ);
                        this.homingPositions.put(player, spawn);
                    }
                }
                this.attackLoop = 1;
            }
            List<EntityPlayer> toRemove = new ArrayList<>();
            for (EntityPlayer player : this.homingPositions.keySet()) {
                this.homingPositions.get(player).setPos(((player.posX - this.homingPositions.get(player).getX()) / 10), ((player.posY - this.homingPositions.get(player).getY()) / 10), ((player.posZ - this.homingPositions.get(player).getZ()) / 10));
                if (this.boss.isEntityCloseEnough(player, this.homingPositions.get(player), 2)) {
                    this.explosionPositions.put(this.homingPositions.get(player),5);
                    this.particlePositions.remove(this.homingPositions.get(player));
                    this.boss.subtractPlayerHealth(player,10d);
                    toRemove.add(player);
                }
                else if (this.boss.isEntityCloseEnough(this.boss, this.homingPositions.get(player), 4)) {
                    this.explosionPositions.put(this.homingPositions.get(player),5);
                    this.particlePositions.remove(this.homingPositions.get(player));
                    toRemove.add(player);
                    this.active = false;
                    this.boss.isShieldUp = false;
                    this.boss.updateShield(this.boss.getShieldUp());
                    this.boss.invulnerable = false;
                    this.boss.world.playSound(this.boss.posX, this.boss.posY, this.boss.posZ, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1.0F, 1.0F, false);
                }
                else this.particlePositions.put(this.homingPositions.get(player),"white");
            }
            for(EntityPlayer player : toRemove) this.homingPositions.remove(player);
            for (BlockPos.MutableBlockPos pos : this.homingPositions.values()) particlePositions.put(pos, "white");
            for (BlockPos pos : this.particlePositions.keySet())
                world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, pos.getX(), pos.getY(), pos.getZ(), 0.0d, 0.0d, 0.0d);
                //world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 1, false);
            for (BlockPos pos : this.explosionPositions.keySet())
                world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), explosionPositions.get(pos), true);
            this.explosionPositions.clear();
        }
    }
}
