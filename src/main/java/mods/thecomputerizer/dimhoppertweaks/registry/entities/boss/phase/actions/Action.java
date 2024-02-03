package mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.phase.actions;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import net.minecraft.entity.player.EntityPlayer;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Action {
    protected final int activeTicks;
    private final boolean singleton;
    protected final int activePhase;
    private final String target;
    private final boolean idleOnFinish;

    protected Action(int activeTicks, boolean singleton, int activePhase, String target) {
        this(activeTicks,singleton,activePhase,target,true);
    }

    protected Action(int activeTicks, boolean singleton, int activePhase, String target, boolean idleOnFinish) {
        this.activeTicks = activeTicks;
        this.singleton = singleton;
        this.activePhase = activePhase;
        this.target = target;
        this.idleOnFinish = idleOnFinish;
    }

    protected List<EntityPlayer> findPlayerTargets(EntityFinalBoss boss) {
        switch(this.target) {
            case "ALL" : return nonNullPlayers(boss.getTrackingPlayers().toArray(new EntityPlayer[0]));
            case "RANDOM" : return nonNullPlayers(getRandomPlayer(boss));
            case "CLOSEST" : return nonNullPlayers(getClosestPlayer(boss));
            default : return nonNullPlayers(getFirstPlayer(boss.getTrackingPlayers()));
        }
    }

    private List<EntityPlayer> nonNullPlayers(EntityPlayer ... players) {
        return Arrays.stream(players).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private EntityPlayer getRandomPlayer(EntityFinalBoss boss) {
        Random rand = boss.world.rand;
        List<EntityPlayer> players = nonNullPlayers(boss.getTrackingPlayers().toArray(new EntityPlayer[0]));
        return players.isEmpty() ? null : players.size()==1 ? players.get(0) : players.get(rand.nextInt(players.size()));
    }

    private EntityPlayer getClosestPlayer(EntityFinalBoss boss) {
        return boss.world.getClosestPlayer(boss.posX,boss.posY,boss.posZ,boss.getTrackingRange(),false);
    }

    private EntityPlayer getFirstPlayer(List<EntityPlayer> players) {
        for(EntityPlayer player : players)
            if(Objects.nonNull(player))
                return player;
        return null;
    }

    public abstract void startAction(EntityFinalBoss boss);

    public abstract void continueAction(EntityFinalBoss boss, int activeProgress);

    public void finishAction(EntityFinalBoss boss) {
        if(this.idleOnFinish) boss.setAnimationState("idle");
    }

    public int getActiveTicks() {
        return this.activeTicks;
    }

    public boolean isSingleton() {
        return this.singleton;
    }
}
