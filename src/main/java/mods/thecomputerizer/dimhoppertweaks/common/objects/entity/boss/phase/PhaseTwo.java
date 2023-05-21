package mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase;

import mods.thecomputerizer.dimhoppertweaks.Constants;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions.Action;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions.DelayedAOE;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions.SpawnProjectile;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions.Teleport;

public class PhaseTwo extends PhaseBase {

    public PhaseTwo(EntityFinalBoss boss) {
        super(boss,2);
    }

    @Override
    protected Action[] orderedActions() {
        DelayedAOE aoe = new DelayedAOE(15,false,this.phase,2);
        Teleport teleport = new Teleport(40,false,this.phase);
        return new Action[]{aoe,aoe,teleport};
    }

    @Override
    protected boolean checkPhaseComplete() {
        return this.boss.getHealthPercentage()<=0.75f;
    }

    @Override
    protected boolean dropShieldWhenCompleted() {
        return false;
    }
}
