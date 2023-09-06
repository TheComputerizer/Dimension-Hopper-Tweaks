package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;

public final class Teleport extends Action {

    public Teleport(int activeTicks, boolean singleton, int activePhase) {
        super(activeTicks, singleton, activePhase, "CLOSEST");
    }

    @Override
    public void startAction(EntityFinalBoss boss) {
        boss.teleportRandomly();
    }

    @Override
    public void continueAction(EntityFinalBoss boss, int activeProgress) {

    }
}
