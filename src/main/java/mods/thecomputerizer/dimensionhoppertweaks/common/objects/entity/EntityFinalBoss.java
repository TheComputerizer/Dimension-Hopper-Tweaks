package mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityFinalBoss extends EntityLiving {
    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_20)).setDarkenSky(true);
    private boolean allowTeleport = false;
    private boolean allowAreaAttackSmall = false;
    private int tickCounter = 0;
    public static int attackSmallTicks = 0;
    private int teleportTicks = 0;
    private double delayedAreaAttackSmallx = 0;
    private double delayedAreaAttackSmally = 0;
    private double delayedAreaAttackSmallz = 0;
    public static double delayedAreaAttackSmallRenderx = 0;
    public static double delayedAreaAttackSmallRendery = 0;
    public static double delayedAreaAttackSmallRenderz = 0;
    private int delayedAreaAttackSmallRender = 0;
    public static boolean allowForcefield = false;
    public static boolean renderSmallAttackArea = false;
    List<EntityPlayer> players = new ArrayList<>();

    public EntityFinalBoss(World worldIn)
    {
        super(worldIn);
        this.setHealth(this.getMaxHealth());
        this.setSize(1F, 1.875F);
        this.isImmuneToFire = true;
        this.experienceValue = 999999;
    }

    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityFinalBoss.AIBossIntro());
        this.tasks.addTask(1, new EntityFinalBoss.ApplyForcefield());
        this.tasks.addTask(2, new EntityFinalBoss.DoSmallAttack());
        this.tasks.addTask(3, new EntityFinalBoss.AITeleport());
    }

    @Override
    protected void applyEntityAttributes()
    {
       super.applyEntityAttributes();
       this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
       this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6000000238418579D);
       this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(200.0D);
       this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1.0D);
    }

    @Override
    public float getEyeHeight()
    {
        return 1.875F;
    }

    @Override
    public void onKillCommand()
    {
        this.attackEntityFrom(new DamageSource("infinity"), Float.MAX_VALUE);
    }

    @Override
    public boolean canDespawn()
    {
        return false;
    }

    @Override
    protected void updateAITasks()
    {
        super.updateAITasks();
        if (allowTeleport)
        {
            teleportRandomly();
        }
        if (allowForcefield)
        {
            teleportForcefield();
        }
        if (allowAreaAttackSmall)
        {
            delayedAreaAttackSmall();
        }
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player)
    {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
        players.add(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public boolean isNonBoss()
    {
        return false;
    }

    protected boolean isPlayerCloseEnough(EntityPlayer p, int x, int y, int z, int max) {
        return p.getPosition().getDistance(x, y, z) <= max;
    }

    protected void teleportRandomly()
    {
        if(teleportTicks==0) {
            teleportTicks=EntityFinalBoss.this.ticksExisted;
        }
        if(this.ticksExisted%3==0 && this.ticksExisted-teleportTicks<=8) {
            allowForcefield=false;
            double d0 = this.posX + (this.rand.nextDouble()) * 32.0D;
            double d1 = this.posY + (this.rand.nextDouble()) * 4.0D;
            double d2 = this.posZ + (this.rand.nextDouble()) * 32.0D;
            this.setPosition(d0, d1, d2);
            this.world.playSound(null, d0, d1, d2, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
        }
        if(this.ticksExisted-teleportTicks>=27) {
            attackSmallTicks=0;
            teleportTicks=0;
        }
    }

    protected void teleportForcefield()
    {
        for (EntityPlayer p : players)
        {
            if(isPlayerCloseEnough(p,(int)this.posX,(int)this.posY,(int)this.posZ,8))
            {
                double x = this.posX+ (this.rand.nextDouble()) * 32.0D;
                double y = this.posY+ (this.rand.nextDouble()) * 4.0D;
                double z = this.posZ+ (this.rand.nextDouble()) * 32.0D;
                p.setPositionAndUpdate(x,y,z);
                this.world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            }
        }
    }

    protected void delayedAreaAttackSmall() {
        List<EntityPlayer> playerList = players;
        int selectedPlayer = (int)(Math.random()*playerList.size());
        EntityPlayer theChosenOne = playerList.get(selectedPlayer);
        if (tickCounter == 0) {
            tickCounter = this.ticksExisted;
            delayedAreaAttackSmallx = theChosenOne.posX;
            delayedAreaAttackSmally = theChosenOne.posY;
            delayedAreaAttackSmallz = theChosenOne.posZ;
        }
        if((this.ticksExisted-tickCounter)>=20) {
            System.out.print(delayedAreaAttackSmallx+" "+delayedAreaAttackSmally+" "+delayedAreaAttackSmallz+"\n");
            if(isPlayerCloseEnough(theChosenOne,(int)delayedAreaAttackSmallx,(int)delayedAreaAttackSmally,(int)delayedAreaAttackSmallz,4)) {
                if((theChosenOne.getMaxHealth()-5.0D)<=1) {
                    theChosenOne.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
                    this.setDropItemsWhenDead(false);
                    allowForcefield=false;
                    this.setDead();
                }
                theChosenOne.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(theChosenOne.getMaxHealth()-5.0D);
                this.world.playSound(null, delayedAreaAttackSmallx, delayedAreaAttackSmally, delayedAreaAttackSmallz, SoundEvents.ENTITY_WITHER_BREAK_BLOCK, this.getSoundCategory(), 1.0F, 1.0F);
            }
            else {
                this.world.playSound(null, delayedAreaAttackSmallx, delayedAreaAttackSmally, delayedAreaAttackSmallz, SoundEvents.ENTITY_ENDEREYE_DEATH, this.getSoundCategory(), 1.0F, 1.0F);
            }
            tickCounter=0;
            delayedAreaAttackSmallRenderx = delayedAreaAttackSmallx;
            delayedAreaAttackSmallRendery = delayedAreaAttackSmallx;
            delayedAreaAttackSmallRenderz = delayedAreaAttackSmallx;
            delayedAreaAttackSmallx=0;
            delayedAreaAttackSmally=0;
            delayedAreaAttackSmallz=0;
            delayedAreaAttackSmallRender=this.ticksExisted;
            renderSmallAttackArea=true;
        }
        if((this.ticksExisted-delayedAreaAttackSmallRender)>=5 && delayedAreaAttackSmallRender!=0) {
            renderSmallAttackArea=false;
            delayedAreaAttackSmallRender=0;
        }
        System.out.print(tickCounter+"\n");
    }

    @Override
    public boolean hasNoGravity()
    {
      return true;
    }

    public void setCustomNameTag(String name)
    {
      super.setCustomNameTag(name);
      this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
       super.readEntityFromNBT(compound);
       if (this.hasCustomName())
       {
          this.bossInfo.setName(this.getDisplayName());
       }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
       super.writeEntityToNBT(compound);
    }

    class AIBossIntro extends EntityAIBase
    {
        public AIBossIntro() { this.setMutexBits(7); }

        @Override
        public boolean shouldExecute()
        {
            if (EntityFinalBoss.this.ticksExisted < 400) { EntityFinalBoss.this.setVelocity(0, 0.1, 0); }
            if(EntityFinalBoss.this.ticksExisted == 10)
            {
                for (EntityPlayer p : players)
                {
                    if (p.world.provider.getDimension()==EntityFinalBoss.this.world.provider.getDimension()) {
                        BossDialogue.dialogueOne(p);
                    }
                }
                return true;
            }
            else if(EntityFinalBoss.this.ticksExisted == 110)
            {
                for (EntityPlayer p : players)
                {
                    if (p.world.provider.getDimension()==EntityFinalBoss.this.world.provider.getDimension()) {
                        BossDialogue.dialogueTwo(p);
                    }
                }
                return true;
            }
            else if(EntityFinalBoss.this.ticksExisted == 210)
            {
                for (EntityPlayer p : players)
                {
                    if (p.world.provider.getDimension()==EntityFinalBoss.this.world.provider.getDimension()) {
                        BossDialogue.dialogueThree(p);
                    }
                }
                return true;
            }
            else if(EntityFinalBoss.this.ticksExisted == 310) {
                for (EntityPlayer p : players) {
                    if (p.world.provider.getDimension() == EntityFinalBoss.this.world.provider.getDimension()) {
                        BossDialogue.dialogueFour(p);
                    }
                }
                return true;
            }
            else if (EntityFinalBoss.this.ticksExisted < 400) {
                return true;
            }
            else {EntityFinalBoss.this.setVelocity(0, 0, 0);}
            return false;
        }
    }

    class AITeleport extends EntityAIBase
    {
        public AITeleport()
        {
            this.setMutexBits(7);
        }

        @Override
        public boolean shouldExecute()
        {
            allowTeleport = true;
            return false;
        }
    }

    class ApplyForcefield extends EntityAIBase
    {
        public ApplyForcefield()
        {
            this.setMutexBits(7);
        }

        @Override
        public boolean shouldExecute()
        {
            if(EntityFinalBoss.this.ticksExisted<500) {
                allowForcefield = true;
                return true;
            }
            return false;
        }
    }

    class DoSmallAttack extends EntityAIBase
    {
        public DoSmallAttack() {this.setMutexBits(7);}

        @Override
        public boolean shouldExecute()
        {
            if(attackSmallTicks==0) {
                attackSmallTicks=EntityFinalBoss.this.ticksExisted;
            }
            if((EntityFinalBoss.this.ticksExisted-attackSmallTicks)<=100) {
                allowAreaAttackSmall = true;
                return true;
            }
            else {
                return false;
            }
        }
    }
}
