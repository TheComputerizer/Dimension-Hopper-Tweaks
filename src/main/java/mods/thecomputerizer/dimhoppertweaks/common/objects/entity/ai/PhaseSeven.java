package mods.thecomputerizer.dimhoppertweaks.common.objects.entity.ai;

import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.EntityFinalBoss;
import net.minecraft.entity.ai.EntityAIBase;

public class PhaseSeven extends EntityAIBase {
    private final EntityFinalBoss boss;
    private boolean active;

    public PhaseSeven(EntityFinalBoss boss) {
        this.setMutexBits(7);
        this.boss = boss;
        this.active = boss.phase<=7;
    }

    @Override
    public boolean shouldExecute() {
        return this.active;
    }

    @Override
    public void startExecuting() {
        if(this.boss.getShieldUp()) this.boss.updateShield(false);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.active;
    }

    @Override
    public void updateTask() {
        this.active = boss.phase<=7;
        if(this.active) {

        }
    }
}
