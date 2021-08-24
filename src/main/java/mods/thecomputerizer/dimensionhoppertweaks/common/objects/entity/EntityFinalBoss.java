package mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityFinalBoss extends EntityLiving {
    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_20)).setDarkenSky(true);
    private boolean allowTeleport = false;
    public static boolean allowForcefield = false;
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
        this.tasks.addTask(2, new EntityFinalBoss.AITeleport());
    }

    @Override
    protected void applyEntityAttributes()
    {
       super.applyEntityAttributes();
       this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
       this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6000000238418579D);
       this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
       this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1.0D);
    }

    @Override
    public float getEyeHeight()
    {
        return 1.875F;
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

    protected void teleportRandomly()
    {
        double d0 = this.posX + (this.rand.nextDouble()) * 8.0D;
        double d1 = this.posY + (this.rand.nextDouble()) * 8.0D;
        double d2 = this.posZ + (this.rand.nextDouble()) * 8.0D;
        this.setPosition(d0, d1, d2);
        this.world.playSound(null, d0, d1, d2, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
    }

    protected void teleportForcefield()
    {
        for (EntityPlayer p : players)
        {
            if(p.getPosition().getDistance(((int) this.posX),((int)this.posY),((int)this.posZ))<=8)
            {
                double x = this.posX+ (this.rand.nextDouble()) * 32.0D;
                double y = this.posY+ (this.rand.nextDouble()) * 4.0D;
                double z = this.posZ+ (this.rand.nextDouble()) * 32.0D;
                p.setPositionAndUpdate(x,y,z);
                this.world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            }
        }
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
            if (EntityFinalBoss.this.ticksExisted % 3 == 0)
            {
                allowTeleport = true;
                return true;
            }
            allowTeleport = false;
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
            allowForcefield = true;
            return true;
        }
    }
}
