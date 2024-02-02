package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DelayedAOE extends Action {
    private final int range;

    public DelayedAOE(int activeTicks, boolean singleton, int activePhase, int range) {
        super(activeTicks, singleton, activePhase, "ALL");
        this.range = range;
    }

    @Override
    public void startAction(EntityFinalBoss boss) {
        List<EntityPlayer> targets = findPlayerTargets(boss);
        if(!targets.isEmpty()) {
            List<Vec3d> vecList = new ArrayList<>();
            EntityPlayer lookAtMe = null;
            for(EntityPlayer target : targets) {
                vecList.add(target.getPositionVector());
                if(Objects.isNull(lookAtMe) || target.getDistance(boss)<lookAtMe.getDistance(boss))
                    lookAtMe = target;
            }
            boss.addAOECounter(vecList,this.activeTicks,this.range,this.activePhase);
            boss.updateLook(lookAtMe.getPositionVector());
            boss.setAnimationState("point");
        }
    }

    @Override
    public void continueAction(EntityFinalBoss boss, int activeProgress) {

    }

    @Override
    public void finishAction(EntityFinalBoss boss) {
        super.finishAction(boss);
        boss.updateLook(Vec3d.ZERO);
    }
}
