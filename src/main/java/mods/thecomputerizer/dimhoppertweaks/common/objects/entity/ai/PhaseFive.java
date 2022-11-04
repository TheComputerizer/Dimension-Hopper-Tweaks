package mods.thecomputerizer.dimhoppertweaks.common.objects.entity.ai;

import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.EntityFinalBoss;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Random;

public class PhaseFive extends EntityAIBase {
    private final EntityFinalBoss boss;
    private boolean active;
    private final World world;
    private final Random rand;
    private final HashMap<Integer, BlockPos> savedPositions;
    private int attackTimer;
    private int attackStartCounter;
    private int attackFinishCounter;

    public PhaseFive(EntityFinalBoss boss) {
        this.setMutexBits(7);
        this.rand = new Random();
        this.boss = boss;
        this.world = boss.getEntityWorld();
        this.active = boss.phase<=5;
        this.savedPositions = new HashMap<>();
        this.attackTimer = -1;
        this.attackStartCounter = 1;
        this.attackFinishCounter = 1;
    }

    @Override
    public boolean shouldExecute() {
        return this.active;
    }

    @Override
    public void startExecuting() {
        if(!this.boss.getShieldUp()) this.boss.updateShield(true);
        this.boss.teleportForcefield();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.active;
    }

    @Override
    public void updateTask() {
        this.active = boss.phase<=5;
        if(this.active) {
            this.boss.setVelocity(0,0,0);
            this.attackTimer++;
            if (this.attackTimer % 10 == 0) {
                double x = this.boss.posX + (-32 + (this.rand.nextDouble()) * 16.0D);
                double y = this.boss.posY + (-4 + (this.rand.nextDouble()) * 8.0D);
                double z = this.boss.posZ + (-32 + (this.rand.nextDouble()) * 16.0D);
                this.savedPositions.put(this.attackStartCounter, new BlockPos(x,y,z));
                this.attackStartCounter++;
            }
            if (this.attackTimer - 10 >= 0 && (this.attackTimer - 10) % 20 == 0) {
                for (EntityPlayer player : this.boss.getTrackingPlayers())
                    this.boss.areaAttackSmall(player, this.savedPositions.get(this.attackFinishCounter), 5);
                this.attackFinishCounter++;
            }
        }
    }
}
