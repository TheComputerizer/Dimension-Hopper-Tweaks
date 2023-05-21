package mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;
import net.minecraft.entity.player.EntityPlayer;

public final class AvoidPlayers extends Action {

    public AvoidPlayers(int activeTicks, boolean singleton, int activePhase) {
        super(activeTicks, singleton, activePhase, "ALL");
    }

    @Override
    public void startAction(EntityFinalBoss boss) {

    }

    @Override
    public void continueAction(EntityFinalBoss boss, int activeProgress) {
        for(EntityPlayer player : boss.getTrackingPlayers())
            if(boss.getDistance(player)<=4)
                boss.teleportRandomly();
    }
}
