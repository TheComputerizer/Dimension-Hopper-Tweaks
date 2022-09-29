package mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.ai;

import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityFinalBoss;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.HomingProjectile;
import mods.thecomputerizer.dimensionhoppertweaks.network.PacketHandler;
import mods.thecomputerizer.dimensionhoppertweaks.network.packets.PacketRenderBossAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

public class PhaseThree extends EntityAIBase {
    private final EntityFinalBoss boss;
    private final World world;
    private final HashMap<Integer, HashMap<EntityPlayer, BlockPos>> savedPositions;
    private boolean active;
    private int attackTimer;
    private int attackStartCounter;
    private int attackFinishCounter;
    private boolean teleported;
    private int attackLoop;

    public PhaseThree(EntityFinalBoss boss) {
        this.setMutexBits(7);
        this.boss = boss;
        this.world = boss.getEntityWorld();
        this.savedPositions = new HashMap<>();
        this.active = boss.phase<=3;
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
        if(!this.boss.getShieldUp()) this.boss.updateShield(true);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.active;
    }

    @Override
    public void updateTask() {
        this.active = boss.phase<=3;
        if(this.active) {
            if (attackLoop <= 5) {
                this.attackTimer++;
                if (this.attackTimer % 15 == 0 && this.attackStartCounter <= 5) {
                    this.teleported = false;
                    this.savedPositions.put(this.attackStartCounter, new HashMap<>());
                    for (EntityPlayer player : this.boss.getTrackingPlayers()) {
                        this.savedPositions.get(this.attackStartCounter).put(player, player.getPosition());
                        if (player instanceof EntityPlayerMP)
                            PacketHandler.NETWORK.sendTo(new PacketRenderBossAttack.PacketRenderBossAttackMessage(player.getPosition(), 5, 4), (EntityPlayerMP) player);
                    }
                    this.attackStartCounter++;
                }
                if (this.attackTimer - 20 >= 0 && (this.attackTimer - 20) % 25 == 0 && this.attackFinishCounter <= 5) {
                    for (EntityPlayer player : this.savedPositions.get(this.attackFinishCounter).keySet())
                        this.boss.areaAttackSmall(player, this.savedPositions.get(this.attackFinishCounter).get(player), 3);
                    this.attackFinishCounter++;
                }
                if (this.attackFinishCounter > 5 && !this.teleported) {
                    this.boss.teleportRandomly();
                    this.teleported = true;
                    this.attackStartCounter = 1;
                    this.attackFinishCounter = 1;
                    this.attackTimer = -1;
                    attackLoop++;
                }
            } else {
                //TODO beam attack
                for (EntityPlayer player : this.boss.getTrackingPlayers()) {
                    HomingProjectile projectile = new HomingProjectile(this.world);
                    projectile.setLocationAndAngles(this.boss.posX, this.boss.posY + 8, this.boss.posZ,0f,0f);
                    projectile.setUpdate(player, this.boss, 3,1.25f);
                    this.world.spawnEntity(projectile);
                    this.boss.teleportOtherEntityForcefield(projectile);
                    this.boss.projectiles.put(projectile, player.getUniqueID());
                }
                this.attackLoop = 1;
            }
            if (this.boss.phase > 3) this.active = false;
        }
    }
}
