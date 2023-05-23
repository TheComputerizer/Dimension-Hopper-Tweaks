package mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase;

import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions.*;

public class PhaseFour extends PhaseBase {

    public PhaseFour(EntityFinalBoss boss) {
        super(boss,4);
    }

    @Override
    protected Action[] orderedActions() {
        AvoidPlayers avoid = new AvoidPlayers(100,true,this.phase);
        Charge charge = new Charge(100,false,this.phase,15,25d);
        Teleport teleport = new Teleport(20,false,this.phase);
        return new Action[]{avoid,charge,teleport};
    }

    @Override
    protected boolean checkPhaseComplete() {
        return this.boss.getHealthPercentage()<=0.5f;
    }

    @Override
    protected boolean dropShieldWhenCompleted() {
        return false;
    }
}
