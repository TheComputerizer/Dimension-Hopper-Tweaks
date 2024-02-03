package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;

public final class Teleport extends Action {

    public Teleport(int activeTicks, boolean singleton, int activePhase) {
        this(activeTicks,singleton,activePhase,true);
    }

    public Teleport(int activeTicks, boolean singleton, int activePhase, boolean idleOnFinish) {
        super(activeTicks,singleton,activePhase,"CLOSEST",idleOnFinish);
    }

    @Override
    public void startAction(EntityFinalBoss boss) {
        boss.teleportRandomly();
    }

    @Override
    public void continueAction(EntityFinalBoss boss, int activeProgress) {

    }
}
