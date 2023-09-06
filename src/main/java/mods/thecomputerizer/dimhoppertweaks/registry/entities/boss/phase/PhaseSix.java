package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions.*;

public class PhaseSix extends PhaseBase {

    public PhaseSix(EntityFinalBoss boss) {
        super(boss,6);
    }

    @Override
    protected Action[] orderedActions() {
        MovePlayers move = new MovePlayers(10,true,this.phase);
        Beam beam = new Beam(200,false,this.phase,80);
        DelayedAOE aoe = new DelayedAOE(10,false,this.phase,3);
        Teleport teleport = new Teleport(10,false,this.phase);
        return new Action[]{move,beam,aoe,aoe,aoe,teleport,aoe,aoe,teleport,aoe,teleport};
    }

    @Override
    protected boolean checkPhaseComplete() {
        return this.boss.getHealthPercentage()<=0.25f;
    }

    @Override
    protected boolean dropShieldWhenCompleted() {
        return false;
    }
}
