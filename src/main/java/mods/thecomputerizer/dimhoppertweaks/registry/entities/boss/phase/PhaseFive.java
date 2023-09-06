package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions.Action;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions.IndiscriminateAOE;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions.MovePlayers;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions.Teleport;

public class PhaseFive extends PhaseBase {

    public PhaseFive(EntityFinalBoss boss) {
        super(boss,5);
    }

    @Override
    protected Action[] orderedActions() {
        MovePlayers move = new MovePlayers(10,true,this.phase);
        IndiscriminateAOE indiscriminate = new IndiscriminateAOE(220,false,this.phase,26,
                20d,15,5,4);
        Teleport teleport = new Teleport(20,false,this.phase);
        return new Action[]{move,indiscriminate,teleport};
    }

    @Override
    protected boolean checkPhaseComplete() {
        return boss.getHealthPercentage()<=0.45f;
    }

    @Override
    protected boolean dropShieldWhenCompleted() {
        return true;
    }
}
