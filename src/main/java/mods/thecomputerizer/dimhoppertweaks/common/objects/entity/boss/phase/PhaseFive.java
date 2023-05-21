package mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase;

import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions.*;

public class PhaseFive extends PhaseBase {

    public PhaseFive(EntityFinalBoss boss) {
        super(boss,5);
    }

    @Override
    protected Action[] orderedActions() {
        MovePlayers move = new MovePlayers(10,true,this.phase);
        IndiscriminateAOE indiscriminate = new IndiscriminateAOE(220,false,this.phase,26,
                20d,15,5,2);
        Teleport teleport = new Teleport(20,false,this.phase);
        return new Action[]{move,indiscriminate,teleport};
    }

    @Override
    protected boolean checkPhaseComplete() {
        boolean done = this.boss.boom;
        this.boss.boom = false;
        return done;
    }

    @Override
    protected boolean dropShieldWhenCompleted() {
        return true;
    }
}
