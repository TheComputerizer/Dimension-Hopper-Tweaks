package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.HomingProjectile;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Objects;

public final class SpawnProjectile extends Action {

    public SpawnProjectile(int activeTicks, boolean singleton, int activePhase) {
        super(activeTicks,singleton,activePhase,"CLOSEST");
    }

    @Override
    public void startAction(EntityFinalBoss boss) {
        List<EntityPlayer> targets = findPlayerTargets(boss);
        if(!targets.isEmpty()) {
            boss.updateLook(targets.get(0).getPositionVector());
            boss.setAnimationState("pointsword");
            boss.setProjectileCharge(this.activeTicks);
        }
    }

    @Override
    public void continueAction(EntityFinalBoss boss, int activeProgress) {

    }

    @Override
    public void finishAction(EntityFinalBoss boss) {
        super.finishAction(boss);
        boss.updateLook(Vec3d.ZERO);
        boss.setProjectileCharge(0);
        Vec3d eyePos = boss.getPositionEyes(1f);
        Vec3d lookVec = boss.getLookVec();
        Vec3d spawnVec = eyePos.add(lookVec.x*8.5,lookVec.y*8.5,lookVec.z*8.5);
        HomingProjectile projectile = new HomingProjectile(boss.world);
        projectile.setPosition(spawnVec.x,spawnVec.y,spawnVec.z);
        boss.world.spawnEntity(projectile);
        EntityPlayer player = boss.getTrackingPlayers().get(0);
        if(Objects.nonNull(player)) projectile.setUpdate(boss,this.activePhase,3.5f);
        boss.projectiles.add(projectile);
    }
}
