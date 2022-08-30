package mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.ai;

import mods.thecomputerizer.dimensionhoppertweaks.common.objects.DimensionHopperItems;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityFinalBoss;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;

public class PhaseFour extends EntityAIBase {
    private final EntityFinalBoss boss;
    private boolean active;
    private int activeTimer;
    private int playerIndex;
    private int chargeStartLag;
    private int chargeEndLag;
    private boolean teleported;
    private boolean charged;

    public PhaseFour(EntityFinalBoss boss) {
        this.setMutexBits(7);
        this.boss = boss;
        this.active = boss.phase<=4;
        this.activeTimer = 0;
        this.chargeStartLag = 0;
        this.chargeEndLag = 0;
        this.teleported = false;
        this.charged = false;
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
    public void resetTask() {
        this.activeTimer = 0;
    }

    @Override
    public void updateTask() {
        this.active = boss.phase<=4;
        if(this.active) {
            if(this.activeTimer>100) {
                if(!teleported) {
                    this.boss.teleportBehindPlayer(this.boss.getTrackingPlayers().get(this.playerIndex));
                    this.playerIndex++;
                    if (this.playerIndex >= this.boss.getTrackingPlayers().size()) this.playerIndex = 0;
                    this.teleported = true;
                } else {
                    if(this.chargeStartLag>10) {
                        if(!this.charged) {
                            this.boss.setVelocity(this.boss.getLookVec().x * 2d, 0, this.boss.getLookVec().z * 2d);
                            this.charged = true;
                        }
                        else {
                            if(this.chargeEndLag>20) {
                                this.teleported = false;
                                this.chargeStartLag = 0;
                                this.charged = false;
                                this.chargeEndLag = 0;
                            } else this.chargeEndLag++;
                        }
                    } else this.chargeStartLag++;
                }
            } else {
                this.activeTimer++;
                for(EntityPlayer player : this.boss.getTrackingPlayers()) {
                    if(this.boss.getDistance(player)<=8)
                        this.boss.teleportRandomly();
                }
                if(this.activeTimer==90) {
                    this.boss.setHeldItem(EnumHand.MAIN_HAND, DimensionHopperItems.REALITY_SLASHER.getDefaultInstance());
                    this.boss.world.playSound(this.boss.posX, this.boss.posY, this.boss.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.MASTER, 1f, 1f, false);
                }

            }
            if(boss.getHealthPercentage()<=0.5f) {
                this.boss.setPhaseFourComplete();
                this.active = false;
            }
        }
    }
}
