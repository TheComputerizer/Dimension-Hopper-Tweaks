package mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.network.NetworkHandler;
import mods.thecomputerizer.dimhoppertweaks.network.packets.PacketBossClientEffects;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class IndiscriminateAOE extends Action {

    private final int windupDelay;
    private final double spawnRadius;
    private final int aoeTime;
    private final int aoeRange;
    private final int spawnsPerTick;
    private boolean skySwapped = false;
    private boolean shieldDropped = false;

    public IndiscriminateAOE(int activeTicks, boolean singleton, int activePhase, int windupDelay, double spawnRadius,
                             int aoeTime, int aoeRange, int spawnsPerTick) {
        super(activeTicks, singleton, activePhase, "ALL");
        this.windupDelay = windupDelay;
        this.spawnRadius = spawnRadius;
        this.aoeTime = aoeTime;
        this.aoeRange = aoeRange;
        this.spawnsPerTick = spawnsPerTick;
    }

    @Override
    public void startAction(EntityFinalBoss boss) {
        boss.setAnimation("energyrelease",true);
    }

    @Override
    public void continueAction(EntityFinalBoss boss, int activeProgress) {
        if(activeProgress>=this.windupDelay) {
            List<Vec3d> vecList = new ArrayList<>();
            for(int i=0;i<spawnsPerTick;i++)
                vecList.add(i,getRandomPos(boss.getPositionVector(),boss.world.rand));
            boss.addAOECounter(vecList,this.aoeTime,this.aoeRange,this.activePhase);
            if(!this.skySwapped) {
                NetworkHandler.sendToTracking(new PacketBossClientEffects.Message(true,0f),boss);
                this.skySwapped = true;
            }
            if(!this.shieldDropped) {
                boss.updateShield(false);
                this.shieldDropped = true;
            }
        }
    }

    @Override
    public void finishAction(EntityFinalBoss boss) {
        super.finishAction(boss);
        this.shieldDropped = false;
        boss.updateShield(true);
    }

    private Vec3d getRandomPos(Vec3d initial, Random rand) {
        return new Vec3d(initial.x+getRandomDouble(rand),initial.y+getRandomDouble(rand),initial.z+getRandomDouble(rand));
    }

    private double getRandomDouble(Random rand) {
        double offSet = rand.nextDouble()*this.spawnRadius;
        return offSet<=(this.spawnRadius/2d) ? (-1*offSet)-4d : (offSet/2d)+4d;
    }
}
