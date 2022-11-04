package mods.thecomputerizer.dimhoppertweaks.common.objects.entity.ai;

import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.HomingProjectile;
import mods.thecomputerizer.dimhoppertweaks.network.PacketHandler;
import mods.thecomputerizer.dimhoppertweaks.network.packets.PacketRenderBossAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

public class PhaseOne extends EntityAIBase {
    private final EntityFinalBoss boss;
    private final World world;
    private final HashMap<Integer, HashMap<EntityPlayer, BlockPos>> savedPositions;
    private boolean active;
    private int attackTimer;
    private int executeAttackTimer;
    private int attackStartCounter;
    private int attackFinishCounter;
    private boolean teleported;
    private int attackLoop;

    public PhaseOne(EntityFinalBoss boss) {
        this.setMutexBits(7);
        this.boss = boss;
        this.world = boss.getEntityWorld();
        this.savedPositions = new HashMap<>();
        this.active = boss.phase<=1;
        this.attackTimer = 0;
        this.executeAttackTimer = -35;
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
        this.active = boss.phase<=1;
        if(this.active) {
            if (this.attackLoop <= 3) {
                this.attackTimer++;
                this.executeAttackTimer++;
                if (this.attackTimer % 20 == 0 && this.attackStartCounter <= 3) {
                    this.teleported = false;
                    this.savedPositions.put(this.attackStartCounter, new HashMap<>());
                    this.boss.setAnimation("point");
                    for (EntityPlayer player : this.boss.getTrackingPlayers()) {
                        this.savedPositions.get(this.attackStartCounter).put(player, player.getPosition());
                        if (player instanceof EntityPlayerMP)
                            PacketHandler.NETWORK.sendTo(new PacketRenderBossAttack.PacketRenderBossAttackMessage(player.getPosition(), 0, 4), (EntityPlayerMP) player);
                    }
                    this.attackStartCounter++;
                }
                if (this.executeAttackTimer>0 && executeAttackTimer % 20 == 0 && this.attackFinishCounter <= 3) {
                    for (EntityPlayer player : this.savedPositions.get(this.attackFinishCounter).keySet())
                        this.boss.areaAttackSmall(player, this.savedPositions.get(this.attackFinishCounter).get(player), 1);
                    this.attackFinishCounter++;
                }
                if (this.attackFinishCounter > 3 && !this.teleported) {
                    this.boss.teleportRandomly();
                    this.teleported = true;
                    this.attackStartCounter = 1;
                    this.attackFinishCounter = 1;
                    this.attackTimer = 0;
                    this.executeAttackTimer = -35;
                    attackLoop++;
                }
            } else {
                this.boss.setAnimation("projectiles");
                for (EntityPlayer player : this.boss.getTrackingPlayers()) {
                    HomingProjectile projectile = new HomingProjectile(this.world);
                    projectile.setLocationAndAngles(this.boss.posX, this.boss.posY + 8, this.boss.posZ,0f,0f);
                    projectile.setUpdate(player, this.boss, 1,1f);
                    this.world.spawnEntity(projectile);
                    this.boss.teleportOtherEntityForcefield(projectile);
                    this.boss.projectiles.put(projectile, player.getUniqueID());
                }
                this.attackLoop = 1;
            }
            if (this.boss.phase > 1) this.active = false;
        }
    }
}
