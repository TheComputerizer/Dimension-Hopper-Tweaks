package mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase;

import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions.Action;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions.DelayedAOE;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions.SpawnProjectile;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions.Teleport;

public class PhaseOne extends PhaseBase {

    public PhaseOne(EntityFinalBoss boss) {
        super(boss,1);
    }

    @Override
    protected Action[] orderedActions() {
        DelayedAOE aoe = new DelayedAOE(20,false,this.phase,2);
        Teleport teleport = new Teleport(20,false,this.phase);
        SpawnProjectile spawn = new SpawnProjectile(100,false,this.phase);
        return new Action[]{aoe,aoe,aoe,teleport,aoe,aoe,aoe,teleport,aoe,aoe,aoe,teleport,spawn};
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
