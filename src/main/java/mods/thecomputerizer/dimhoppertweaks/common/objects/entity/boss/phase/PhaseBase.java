package mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase;

import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions.Action;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Random;

public abstract class PhaseBase extends EntityAIBase {

    protected final EntityFinalBoss boss;
    protected final Random rand;
    protected final int phase;
    private final MutableInt timer;
    private final Action[] actionLoop;
    private int actionIndex;

    public PhaseBase(EntityFinalBoss boss, int phase) {
        this.setMutexBits(7);
        this.boss = boss;
        this.rand = boss.world.rand;
        this.phase = phase;
        this.timer = new MutableInt();
        this.actionLoop = orderedActions();
        this.actionIndex = 0;
    }

    protected abstract Action[] orderedActions();

    @Override
    public boolean shouldExecute() {
        return this.phase==this.boss.phase;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.phase==this.boss.phase;
    }

    @Override
    public void updateTask() {
        this.boss.setVelocity(0, 0, 0);
        Action current = this.actionLoop[this.actionIndex];
        if (this.timer.getValue() == 0)
            current.startAction(this.boss);
        if (this.timer.getValue() >= current.getActiveTicks()) {
            current.finishAction(this.boss);
            setNextActiveAction();
            this.timer.setValue(0);
        } else {
            this.timer.increment();
            current.continueAction(this.boss,this.timer.getValue());
        }
        if(checkPhaseComplete()) this.boss.finishPhase(this.phase,!dropShieldWhenCompleted());
    }

    private void setNextActiveAction() {
        this.actionIndex++;
        if(this.actionIndex>=this.actionLoop.length)
            this.actionIndex = 0;
        if(this.actionLoop[this.actionIndex].isSingleton()) setNextActiveAction();
    }

    protected abstract boolean checkPhaseComplete();

    protected abstract boolean dropShieldWhenCompleted();
}
