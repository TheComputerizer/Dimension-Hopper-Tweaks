package mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Objects;

public final class Charge extends Action {
    private final int windupTicks;
    private final double impulse;
    private EntityPlayer target;
    boolean wasShieldUp = false;

    public Charge(int activeTicks, boolean singleton, int activePhase, int windupTicks, double impulse) {
        super(activeTicks, singleton, activePhase, "RANDOM");
        this.windupTicks = windupTicks;
        this.impulse = impulse;
    }

    @Override
    public void startAction(EntityFinalBoss boss) {
        this.wasShieldUp = boss.getShieldUp();
        boss.updateShield(false);
        List<EntityPlayer> targets = findPlayerTargets(boss);
        if(!targets.isEmpty()) {
            this.target = targets.get(0);
            boss.teleportBehindPlayer(this.target);
            boss.setAnimation("charge", true);
        }
    }

    @Override
    public void continueAction(EntityFinalBoss boss, int activeProgress) {
        if(Objects.nonNull(this.target) && activeProgress==this.windupTicks) {
            Vec3d posVec = this.target.getPositionVector().subtract(boss.getPositionVector()).normalize();
            boss.motionX += posVec.x*this.impulse;
            boss.motionY += posVec.y*this.impulse;
            boss.motionZ += posVec.z*this.impulse;
            boss.isCharging = true;
        }
    }

    @Override
    public void finishAction(EntityFinalBoss boss) {
        super.finishAction(boss);
        boss.isCharging = false;
        boss.updateShield(this.wasShieldUp);
    }
}
