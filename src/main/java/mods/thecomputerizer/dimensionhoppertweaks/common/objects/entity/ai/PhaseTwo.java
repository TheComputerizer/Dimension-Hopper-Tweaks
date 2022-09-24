package mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.ai;

import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityFinalBoss;
import mods.thecomputerizer.dimensionhoppertweaks.util.PacketHandler;
import mods.thecomputerizer.dimensionhoppertweaks.util.packets.PacketRenderBossAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

public class PhaseTwo extends EntityAIBase {
    private final EntityFinalBoss boss;
    private final World world;
    private final HashMap<Integer, HashMap<EntityPlayer, BlockPos>> savedPositions;
    private boolean active;
    private int attackTimer;
    private int attackStartCounter;
    private int attackFinishCounter;
    private int teleportCounter;

    public PhaseTwo(EntityFinalBoss boss) {
        this.setMutexBits(7);
        this.boss = boss;
        this.world = boss.getEntityWorld();
        this.active = boss.phase<=2;
        this.savedPositions = new HashMap<>();
        this.attackTimer = -1;
        this.attackStartCounter = 1;
        this.attackFinishCounter = 1;
        this.teleportCounter = 1;
    }

    @Override
    public boolean shouldExecute() {
        return this.active;
    }

    @Override
    public void startExecuting() {
        if(this.boss.getShieldUp()) this.boss.updateShield(false);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.active;
    }

    @Override
    public void updateTask() {
        this.attackTimer++;
        if (this.attackTimer % 15 == 0 && this.attackStartCounter <= 2) {
            this.savedPositions.put(this.attackStartCounter, new HashMap<>());
            for (EntityPlayer player : this.boss.getTrackingPlayers()) {
                this.savedPositions.get(this.attackStartCounter).put(player, player.getPosition());
                if (player instanceof EntityPlayerMP)
                    PacketHandler.NETWORK.sendTo(new PacketRenderBossAttack.PacketRenderBossAttackMessage(player.getPosition(), 5, 4), (EntityPlayerMP) player);
            }
            this.attackStartCounter++;
        }
        if (this.attackTimer - 20 >= 0 && (this.attackTimer - 20) % 25 == 0 && this.attackFinishCounter <= 2) {
            for (EntityPlayer player : this.savedPositions.get(this.attackFinishCounter).keySet())
                this.boss.areaAttackSmall(player, this.savedPositions.get(this.attackFinishCounter).get(player),2);
            this.attackFinishCounter++;
        }
        if(this.attackFinishCounter > 2) {
            if (this.teleportCounter <= 2) {
                this.boss.teleportRandomly();
                this.teleportCounter++;
            }
            else {
                this.attackStartCounter = 1;
                this.attackFinishCounter = 1;
                this.attackTimer = -1;
                this.teleportCounter = 1;
            }
        }
        if(boss.getHealthPercentage()<=0.75f) {
            this.boss.setPhaseTwoComplete();
            this.active = false;
        }
    }
}
