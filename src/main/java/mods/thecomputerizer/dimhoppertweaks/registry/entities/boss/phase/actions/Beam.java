package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import net.minecraft.entity.player.EntityPlayer;

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
            boss.setAnimation("pointsword", true);
        }
    }

    @Override
    public void continueAction(EntityFinalBoss boss, int activeProgress) {

    }
}
