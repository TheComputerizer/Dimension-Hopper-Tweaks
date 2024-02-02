package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public final class Beam extends Action {
    private final int chargeTicks;

    public Beam(int activeTicks, boolean singleton, int activePhase, int chargeTicks) {
        super(activeTicks, singleton, activePhase, "RANDOM");
        this.chargeTicks = chargeTicks;
    }

    @Override
    public void startAction(EntityFinalBoss boss) {
        List<EntityPlayer> targets = findPlayerTargets(boss);
        if(!targets.isEmpty()) {
            boss.getLookHelper().setLookPositionWithEntity(targets.get(0), boss.getHorizontalFaceSpeed(), boss.getVerticalFaceSpeed());
            boss.setAnimationState("pointsword");
        }
    }

    @Override
    public void continueAction(EntityFinalBoss boss, int activeProgress) {
        if(activeProgress>=this.chargeTicks) {
            List<Vec3d> vecList = new ArrayList<>();
            for(EntityPlayer player : findPlayerTargets(boss))
                vecList.add(getBeamProgress(boss.getPositionVector(),player.getPositionVector(),activeProgress));
            boss.addAOECounter(vecList,(this.activeTicks-this.chargeTicks)-(activeProgress-this.chargeTicks),10,this.activePhase);
        }
    }

    private Vec3d getBeamProgress(Vec3d bossVec, Vec3d targetVec, int activeProgress) {
        double timeScale = this.activeTicks-this.chargeTicks;
        double curScale = activeProgress-this.chargeTicks;
        double total = bossVec.distanceTo(targetVec)*10;
        return bossVec.crossProduct(targetVec).normalize().scale((curScale/timeScale)*total);
    }
}
