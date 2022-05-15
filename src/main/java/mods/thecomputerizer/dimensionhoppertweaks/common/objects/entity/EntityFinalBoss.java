package mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity;

import codechicken.lib.vec.Translation;
import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import morph.avaritia.item.tools.ItemSwordInfinity;
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
import net.minecraft.realms.RealmsMth;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntityFinalBoss extends EntityLiving {
    private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_20)).setDarkenSky(true);
    private boolean allowTeleport = false;
    private boolean allowAreaAttackSmall = false;
    private boolean invulnerable = true;
    private int tickCounter = 0;
    private int attackSmallTicks = 0;
    private int teleportTicks = 0;
    private int teleportTimer = 0;
    private int smallAttackCounter = 0;
    private double delayedAreaAttackSmallx = 0;
    private double delayedAreaAttackSmally = 0;
    private double delayedAreaAttackSmallz = 0;
    public double delayedAreaAttackSmallRenderx = 0;
    public double delayedAreaAttackSmallRendery = 0;
    public double delayedAreaAttackSmallRenderz = 0;
    private int delayedAreaAttackSmallRender = 0;
    public boolean allowForcefield = false;
    public boolean renderSmallAttackArea = false;
    private boolean smallAttackOrderFix = true;
    private boolean spawnedWhiteNova = false;

    private EntityWhiteNova whitenova;
    List<EntityPlayer> players = new ArrayList<>();

    public EntityFinalBoss(World worldIn) {
        super(worldIn);
        this.setHealth(this.getMaxHealth());
        this.setSize(1F, 1.875F);
        this.isImmuneToFire = true;
        this.experienceValue = 999999;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityFinalBoss.AIBossIntro());
        this.tasks.addTask(1, new EntityFinalBoss.DoSmallAttack());
        this.tasks.addTask(2, new EntityFinalBoss.AITeleport());
        this.tasks.addTask(3, new EntityFinalBoss.LaunchWhiteNova());
        this.tasks.addTask(4, new EntityFinalBoss.CheckNova());
        this.tasks.addTask(5, new EntityFinalBoss.PhaseOneIdle());
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 64.0F));
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
        if (this.allowTeleport) {
            teleportRandomly();
        }
        if (this.allowForcefield) {
            teleportForcefield();
        }
        if (this.allowAreaAttackSmall) {
            delayedAreaAttackSmall();
        }
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
        this.players.add(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    protected boolean isEntityCloseEnough(Entity p, int x, int y, int z, int max) {
        return p.getPosition().getDistance(x, y, z) <= max;
    }

    protected void teleportRandomly() {
        if (this.teleportTicks == 0) {
            this.teleportTicks = EntityFinalBoss.this.ticksExisted;
        }
        if (this.ticksExisted % 3 == 0 && this.ticksExisted - this.teleportTicks <= 8) {
            this.allowForcefield = false;
            double d0 = this.posX + (-32 + (this.rand.nextDouble()) * 64.0D);
            double d1 = this.posY + (-4 + (this.rand.nextDouble()) * 8.0D);
            double d2 = this.posZ + (-32 + (this.rand.nextDouble()) * 64.0D);
            this.setPosition(d0, d1, d2);
            this.world.playSound(null, d0, d1, d2, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
        }
        if (this.ticksExisted - this.teleportTicks >= 27) {
            this.attackSmallTicks = 0;
            this.teleportTicks = 0;
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

    protected void delayedAreaAttackSmall() {
        List<EntityPlayer> playerList = this.players;
        int selectedPlayer = (int) (Math.random() * playerList.size());
        EntityPlayer theChosenOne = playerList.get(selectedPlayer);
        if (this.tickCounter == 0) {
            this.tickCounter = this.ticksExisted;
            this.delayedAreaAttackSmallx = theChosenOne.posX;
            this.delayedAreaAttackSmally = theChosenOne.posY;
            this.delayedAreaAttackSmallz = theChosenOne.posZ;
        }
        if ((this.ticksExisted - this.tickCounter) >= 20) {
            System.out.print(this.delayedAreaAttackSmallx + " " + this.delayedAreaAttackSmally + " " + this.delayedAreaAttackSmallz + "\n");
            if (isEntityCloseEnough(theChosenOne, (int) this.delayedAreaAttackSmallx, (int) this.delayedAreaAttackSmally, (int) this.delayedAreaAttackSmallz, 4)) {
                if ((theChosenOne.getMaxHealth() - 5.0D) <= 1) {
                    theChosenOne.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
                    this.setDropItemsWhenDead(false);
                    this.allowForcefield = false;
                    this.setDead();
                }
                theChosenOne.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(theChosenOne.getMaxHealth() - 5.0D);
                this.world.playSound(null, delayedAreaAttackSmallx, delayedAreaAttackSmally, delayedAreaAttackSmallz, SoundEvents.ENTITY_WITHER_BREAK_BLOCK, this.getSoundCategory(), 1.0F, 1.0F);
            } else {
                this.world.playSound(null, delayedAreaAttackSmallx, delayedAreaAttackSmally, delayedAreaAttackSmallz, SoundEvents.ENTITY_ENDEREYE_DEATH, this.getSoundCategory(), 1.0F, 1.0F);
            }
            this.tickCounter = 0;
            this.delayedAreaAttackSmallRenderx = this.delayedAreaAttackSmallx;
            this.delayedAreaAttackSmallRendery = this.delayedAreaAttackSmallx;
            this.delayedAreaAttackSmallRenderz = this.delayedAreaAttackSmallx;
            this.delayedAreaAttackSmallx = 0;
            this.delayedAreaAttackSmally = 0;
            this.delayedAreaAttackSmallz = 0;
            this.delayedAreaAttackSmallRender = this.ticksExisted;
            this.renderSmallAttackArea = true;
        }
        if ((this.ticksExisted - this.delayedAreaAttackSmallRender) >= 5 && this.delayedAreaAttackSmallRender != 0) {
            this.renderSmallAttackArea = false;
            this.delayedAreaAttackSmallRender = 0;
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
        this.setCustomNameTag(this.getObfuscatedNameProgress());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(!this.invulnerable && !this.allowForcefield) {
            if (source.getTrueSource() instanceof EntityPlayer) {
                EntityPlayer p = (EntityPlayer) source.getTrueSource();
                if (p.getHeldItemMainhand().getItem() instanceof ItemSwordInfinity) {
                    source = new DamageSource("infinity");
                }
            }
            if (Objects.equals(source.damageType, "infinity")) {
                this.damageEntity(source, 10);
                this.setCustomNameTag(this.getObfuscatedNameProgress());
                return true;
            }
        }
        return false;
    }

    class AIBossIntro extends EntityAIBase {
        public AIBossIntro() {
            this.setMutexBits(7);
        }

        @Override
        public boolean shouldExecute() {
            if (EntityFinalBoss.this.ticksExisted < 400) {
                EntityFinalBoss.this.setVelocity(0, 0.1, 0);
            }
            if (EntityFinalBoss.this.ticksExisted == 10) {
                for (EntityPlayer p : players) {
                    if (p.world.provider.getDimension() == EntityFinalBoss.this.world.provider.getDimension()) {
                        BossDialogue.dialogueOne(p);
                    }
                }
                return true;
            } else if (EntityFinalBoss.this.ticksExisted == 110) {
                for (EntityPlayer p : players) {
                    if (p.world.provider.getDimension() == EntityFinalBoss.this.world.provider.getDimension()) {
                        BossDialogue.dialogueTwo(p);
                    }
                }
                return true;
            } else if (EntityFinalBoss.this.ticksExisted == 210) {
                for (EntityPlayer p : players) {
                    if (p.world.provider.getDimension() == EntityFinalBoss.this.world.provider.getDimension()) {
                        BossDialogue.dialogueThree(p);
                    }
                }
                return true;
            } else if (EntityFinalBoss.this.ticksExisted == 310) {
                for (EntityPlayer p : players) {
                    if (p.world.provider.getDimension() == EntityFinalBoss.this.world.provider.getDimension()) {
                        BossDialogue.dialogueFour(p);
                    }
                }
                return true;
            } else if (EntityFinalBoss.this.ticksExisted < 400) {
                return true;
            } else {
                DimensionHopperTweaks.LOGGER.info("Finished invulnerable phase");
                EntityFinalBoss.this.setVelocity(0, 0, 0);
                EntityFinalBoss.this.invulnerable = false;
                return false;
            }
        }
    }

    class AITeleport extends EntityAIBase {
        public AITeleport() {
            this.setMutexBits(7);
        }

        @Override
        public boolean shouldExecute() {
            if (EntityFinalBoss.this.smallAttackCounter < 3) {
                if (EntityFinalBoss.this.teleportTimer == 0) {
                    EntityFinalBoss.this.teleportTimer = EntityFinalBoss.this.ticksExisted;
                }
                if ((EntityFinalBoss.this.ticksExisted - teleportTimer) <= 40) {
                    EntityFinalBoss.this.allowTeleport = true;
                    System.out.print("Small attack: " + (EntityFinalBoss.this.ticksExisted - EntityFinalBoss.this.teleportTimer) + "<55?\n");
                    return true;
                } else {
                    EntityFinalBoss.this.allowTeleport = false;
                    EntityFinalBoss.this.attackSmallTicks = 0;
                    EntityFinalBoss.this.tickCounter = 0;
                    EntityFinalBoss.this.smallAttackCounter++;
                    EntityFinalBoss.this.smallAttackOrderFix = true;
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    class DoSmallAttack extends EntityAIBase {
        public DoSmallAttack() {
            this.setMutexBits(7);
        }

        @Override
        public boolean shouldExecute() {
            if(EntityFinalBoss.this.whitenova!=null && !EntityFinalBoss.this.whitenova.isDead) return false;
            if (EntityFinalBoss.this.smallAttackCounter < 3 && EntityFinalBoss.this.smallAttackOrderFix) {
                if (EntityFinalBoss.this.attackSmallTicks == 0) {
                    EntityFinalBoss.this.attackSmallTicks = EntityFinalBoss.this.ticksExisted;
                    EntityFinalBoss.this.allowForcefield = true;
                }
                if ((EntityFinalBoss.this.ticksExisted - attackSmallTicks) <= 200) {
                    EntityFinalBoss.this.allowAreaAttackSmall = true;
                    return true;
                } else {
                    EntityFinalBoss.this.allowAreaAttackSmall = false;
                    EntityFinalBoss.this.smallAttackOrderFix = false;
                    EntityFinalBoss.this.teleportTicks = 0;
                    EntityFinalBoss.this.teleportTimer = 0;
                    return false;
                }
            } else return false;
        }
    }

    class LaunchWhiteNova extends EntityAIBase {
        public LaunchWhiteNova() {
            this.setMutexBits(7);
        }

        @Override
        public boolean shouldExecute() {
            if (EntityFinalBoss.this.whitenova != null && !EntityFinalBoss.this.whitenova.isDead) return false;
            EntityPlayer p = EntityFinalBoss.this.world.getClosestPlayerToEntity(EntityFinalBoss.this, 200);
            if (p != null) {
                double d0 = EntityFinalBoss.this.posX;
                double d1 = EntityFinalBoss.this.posY;
                double d2 = EntityFinalBoss.this.posZ;
                EntityFinalBoss.this.whitenova = new EntityWhiteNova(EntityFinalBoss.this.world, EntityFinalBoss.this, p);
                EntityFinalBoss.this.whitenova.setPositionAndUpdate(d0, d1, d2);
                EntityFinalBoss.this.world.spawnEntity(whitenova);
                EntityFinalBoss.this.spawnedWhiteNova = true;
            }
            System.out.print("Nova Pos: " + EntityFinalBoss.this.whitenova.posX + " " + EntityFinalBoss.this.whitenova.posY + " " + EntityFinalBoss.this.whitenova.posZ + "\n");
            return false;
        }
    }

    class CheckNova extends EntityAIBase {
        public CheckNova() {
            this.setMutexBits(7);
        }

        @Override
        public boolean shouldExecute() {
            if(!EntityFinalBoss.this.whitenova.isDead) return true;
            else {
                System.out.print("No nova\n");
                return false;
            }
        }
    }

    class PhaseOneIdle extends EntityAIBase {
        public PhaseOneIdle() {
            this.setMutexBits(7);
        }

        @Override
        public boolean shouldExecute() {
            EntityFinalBoss.this.smallAttackCounter = 0;
            EntityFinalBoss.this.allowAreaAttackSmall = false;
            EntityFinalBoss.this.smallAttackOrderFix = false;
            EntityFinalBoss.this.teleportTicks = 0;
            EntityFinalBoss.this.teleportTimer = 0;
            EntityFinalBoss.this.allowTeleport = false;
            EntityFinalBoss.this.attackSmallTicks = 0;
            EntityFinalBoss.this.tickCounter = 0;
            System.out.print("Idling\n");
            return false;
        }
    }
}
