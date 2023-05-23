package mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase;

import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions.*;

public class PhaseThree extends PhaseBase {

    public PhaseThree(EntityFinalBoss boss) {
        super(boss,3);
    }

    @Override
    protected Action[] orderedActions() {
        DelayedAOE aoe = new DelayedAOE(15,false,this.phase,2);
        Teleport teleport = new Teleport(20,false,this.phase);
        Beam beam = new Beam(200,false,this.phase,100);
        SpawnProjectile spawn = new SpawnProjectile(100,false,this.phase);
        return new Action[]{aoe,teleport,aoe,teleport,aoe,teleport,aoe,teleport,aoe,teleport,beam,spawn};
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
