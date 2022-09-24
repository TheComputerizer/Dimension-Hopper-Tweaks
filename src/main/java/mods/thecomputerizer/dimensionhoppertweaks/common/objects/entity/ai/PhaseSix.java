package mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.ai;

import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityFinalBoss;
import net.minecraft.entity.ai.EntityAIBase;

public class PhaseSix extends EntityAIBase {
    private final EntityFinalBoss boss;
    private boolean active;

    public PhaseSix(EntityFinalBoss boss) {
        this.setMutexBits(7);
        this.boss = boss;
        this.active = boss.phase<=6;
    }

    @Override
    public boolean shouldExecute() {
        return this.active;
    }

    @Override
    public void startExecuting() {
        if(this.boss.getShieldUp()) this.boss.updateShield(false);
        this.boss.teleportForcefield();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.active;
    }

    @Override
    public void updateTask() {
        this.active = boss.phase<=6;
        if(this.active) {
            //TODO modified beam attack
            if(boss.getHealthPercentage()<=0.25f) {
                this.boss.setPhaseSixComplete();
                this.active = false;
            }
        }
    }
}
