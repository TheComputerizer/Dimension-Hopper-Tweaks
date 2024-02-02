package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.network.PacketBossClientEffects;
import mods.thecomputerizer.theimpossiblelibrary.network.NetworkHandler;
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
                vecList.add(i,getRandomPos(boss.getPositionVector(),boss.world.rand,activeProgress));
            boss.addAOECounter(vecList,this.aoeTime,this.aoeRange,this.activePhase);
            if(!this.skySwapped) {
                NetworkHandler.sendToTracking(new PacketBossClientEffects(true,0f),boss);
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

    private Vec3d getRandomPos(Vec3d initial, Random rand, int activeProgress) {
        double randX = getRandomDouble(rand);
        double randY = getRandomDouble(rand);
        double randZ = getRandomDouble(rand);
        if(activeProgress%5==0) {
            randY = 0;
            double xDif = randX-initial.x;
            if(Math.abs(xDif)<3d) {
                randX+=(xDif<0 ? -2d : 2d);
            }
            double zDif = randZ-initial.z;
            if(Math.abs(zDif)<3d) {
                randZ+=(zDif<0 ? -3d : 3d);
            }
        }
        return new Vec3d(initial.x+randX,initial.y+randY,initial.z+randZ);
    }

    private double getRandomDouble(Random rand) {
        double offSet = rand.nextDouble()*this.spawnRadius;
        return offSet<=(this.spawnRadius/2d) ? (-1*offSet)-4d : (offSet/2d)+4d;
    }
}
