package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public final class SpawnProjectile extends Action {

    private Vec3d targetVec = Vec3d.ZERO;

    public SpawnProjectile(int activeTicks, boolean singleton, int activePhase) {
        super(activeTicks,singleton,activePhase,"CLOSEST");
    }

    @Override
    public void startAction(EntityFinalBoss boss) {
        List<EntityPlayer> targets = findPlayerTargets(boss);
        if(!targets.isEmpty()) {
            this.targetVec = targets.get(0).getPositionVector();
            boss.updateLook(this.targetVec);
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
        Vec3d spawnVec = eyePos.add(lookVec.x*8.5d,lookVec.y*8.5d,lookVec.z*8.5d);
        boss.spawnProjectile(spawnVec,this.targetVec,3.5f);
    }
}
