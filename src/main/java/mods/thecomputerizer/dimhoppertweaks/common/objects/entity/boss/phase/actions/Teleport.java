package mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;
import net.minecraft.entity.player.EntityPlayer;

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
