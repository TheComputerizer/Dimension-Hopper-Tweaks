package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.util.MobileVec3d;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public final class Beam extends Action {
    private final int chargeTicks;
    private final List<MobileVec3d> targets;

    public Beam(int activeTicks, boolean singleton, int activePhase, int chargeTicks) {
        super(activeTicks,singleton,activePhase,"RANDOM");
        this.chargeTicks = chargeTicks;
        this.targets = new ArrayList<>();
    }

    @Override
    public void startAction(EntityFinalBoss boss) {
        List<EntityPlayer> targets = findPlayerTargets(boss);
        if(!targets.isEmpty()) {
            boss.getLookHelper().setLookPositionWithEntity(targets.get(0),boss.getHorizontalFaceSpeed(),boss.getVerticalFaceSpeed());
            boss.setAnimationState("pointsword");
            calculateTargets(boss.getPositionVector(),targets);
            boss.setProjectileCharge(this.chargeTicks);
        }
    }

    @Override
    public void continueAction(EntityFinalBoss boss, int activeProgress) {
        if(activeProgress>=this.chargeTicks) {
            if(activeProgress==this.chargeTicks) boss.setProjectileCharge(0);
            List<Vec3d> vecList = (List<Vec3d>)MobileVec3d.stepAll(new ArrayList<>(),this.targets);
            boss.addAOECounter(vecList,this.activeTicks-activeProgress,10,this.activePhase);
        }
    }

    @Override
    public void finishAction(EntityFinalBoss boss) {
        super.finishAction(boss);
        this.targets.clear();
    }

    private void calculateTargets(Vec3d bossVec, List<EntityPlayer> players) {
        for(EntityPlayer player : players) {
            Vec3d beamStep = calculateTargetStep(bossVec,player.getPositionVector());
            this.targets.add(new MobileVec3d(bossVec.add(beamStep),beamStep));
        }
    }

    private Vec3d calculateTargetStep(Vec3d bossVec, Vec3d target) {
        double timeScale = 2d/(this.activeTicks-this.chargeTicks);
        return target.subtract(bossVec).scale(timeScale);
    }
}
