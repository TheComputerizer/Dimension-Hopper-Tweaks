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
    private int generalTimer = 0;
    private long curTime = 0;
    private long initTime = 0;
    private boolean allowTeleport = false;
    List<EntityPlayer> players = new ArrayList<>();

    public EntityFinalBoss(World worldIn)
    {
        super(worldIn);
        this.setHealth(this.getMaxHealth());
        this.setSize(1F, 1.875F);
        this.isImmuneToFire = true;
        this.experienceValue = 999999;
    }

    protected void entityInit()
    {
       super.entityInit();
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityFinalBoss.AIDoNothing());
        this.tasks.addTask(1, new EntityFinalBoss.AITeleport());
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

    /*
    @Override
    public void onKillCommand()
    {
       if(EntityFinalBoss.this.introTime() >= 5) {
           playSound((SoundEvents.ENTITY_WITHER_HURT), 1.0F, 1.0F);
           this.setHealth(this.getHealth() - 10.0F);
       }
    }
     */

    @Override
    public boolean canDespawn()
    {
        return false;
    }

    protected void updateAITasks()
    {
        super.updateAITasks();
        if (this.ticksExisted < 2)
        {
            initTime = world.getTotalWorldTime();
        }
        if (this.ticksExisted % 20 == 0)
        {
            generalTimer++;
        }
        if (allowTeleport)
        {
            teleportRandomly();
        }
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public long introTime()
    {
        return generalTimer;
    }

    public void addTrackingPlayer(EntityPlayerMP player)
    {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
        players.add(player);
    }

    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source)
    {
       return true;
    }

    public boolean isNonBoss()
    {
        return false;
    }

    protected void teleportRandomly()
    {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D;
        double d1 = this.posY + (this.rand.nextDouble() - 0.5D) * 8.0D;
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D;
        this.setPosition(d0, d1, d2);
        this.world.playSound(null, d0, d1, d2, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
        this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
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

    class AIDoNothing extends EntityAIBase
    {
        public AIDoNothing()
        {
            this.setMutexBits(7);
        }

        public boolean shouldExecute()

        {
            if(EntityFinalBoss.this.introTime() < 5)
            {
                if(EntityFinalBoss.this.introTime() == 1)
                {
                    for (EntityPlayer p : players)
                    {
                        curTime = world.getTotalWorldTime();
                        BossDialogue.summonDialogue(p,curTime,initTime);
                    }
                }
                return true;
            }
            return false;
        }
    }

    class AITeleport extends EntityAIBase
    {
        public AITeleport()
        {
            this.setMutexBits(7);
        }

        public boolean shouldExecute()
        {
            if (EntityFinalBoss.this.introTime() % 2 == 0)
            {
                allowTeleport = true;
                return true;
            }
            allowTeleport = false;
            return false;
        }
    }
}
