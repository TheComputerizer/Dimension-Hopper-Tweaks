package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;

public final class MovePlayers extends Action {

    public MovePlayers(int activeTicks, boolean singleton, int activePhase) {
        this(activeTicks,singleton,activePhase,true);
    }

    public MovePlayers(int activeTicks, boolean singleton, int activePhase, boolean idleOnFinish) {
        super(activeTicks,singleton,activePhase,"ALL",idleOnFinish);
    }

    @Override
    public void startAction(EntityFinalBoss boss) {
        boss.teleportForcefield();
    }

    @Override
    public void continueAction(EntityFinalBoss boss, int activeProgress) {

    }
}
