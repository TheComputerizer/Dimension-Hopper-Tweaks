package mods.thecomputerizer.dimhoppertweaks.registry.entities;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nonnull;
import java.util.Objects;

public class HomingProjectile extends Entity {

    private static final DataParameter<Integer> PHASE_STATE = EntityDataManager.createKey(HomingProjectile.class,DataSerializers.VARINT);
    private final MutableInt moveCounter = new MutableInt();
    private EntityFinalBoss boss;
    private boolean synced;
    @SideOnly(Side.CLIENT)
    private float speed = 1f;

    public HomingProjectile(World world) {
        super(world);
        this.isImmuneToFire = true;
        this.setSize(0.5f,0.5f);
        this.setEntityInvulnerable(true);
        this.synced = false;
    }

    public int getPhase() {
        return this.dataManager.get(PHASE_STATE);
    }

    public void setPhase(int phase) {
        this.dataManager.set(PHASE_STATE,phase);
    }

    public void setUpdate(EntityFinalBoss boss, Vec3d initialTarget, float speed) {
        this.boss = boss;
        setPhase(boss.getPhase());
        this.speed = speed;
        calculateMove(initialTarget);
        this.glowing = true;
        this.synced = true;
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(PHASE_STATE,0);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return source.canHarmInCreative();
    }

    @Override
    public void onUpdate() {
        if(this.world.isRemote) {
            this.world.spawnParticle(EnumParticleTypes.PORTAL,this.posX,this.posY,this.posZ,
                    (this.rand.nextDouble()-0.5d)*2d,-1d*this.rand.nextDouble(),(this.rand.nextDouble()-0.5d)*2d);
        } else {
            if(this.moveCounter.getValue()>=50) calculateMove();
            if(this.moveCounter.getValue()<20) this.moveSimple();
            else {
                this.motionX*=0.8d;
                this.motionY*=0.8d;
                this.motionZ*=0.8d;
            }
            this.moveCounter.increment();
            if(this.synced && (Objects.isNull(this.boss) || this.boss.isDead)) this.setDead();
            boolean boom = false;
            for(EntityPlayer player : this.boss.getTrackingPlayers()) {
                if(this.getDistance(player)<=(boom ? 4 : 3)) {
                    this.boss.subtractPlayerHealth(player,10d);
                    boom = true;
                }
            }
            if(this.ticksExisted>40 && this.getDistance(this.boss)<=(boom ? 6 : 5)) {
                this.boss.boom = true;
                boom = true;
            }
            if(boom) {
                this.world.createExplosion(this,this.posX,this.posY,this.posZ,5f,true);
                this.setDead();
            } else if(this.ticksExisted>=400) expire();
        }
        super.onUpdate();
    }

    private void calculateMove() {
        EntityPlayer player = this.world.getClosestPlayer(this.posX,this.posY,this.posZ,100d,false);
        if(Objects.nonNull(player)) calculateMove(player.getPositionVector());
        else this.moveCounter.setValue(0);
    }

    private void calculateMove(Vec3d targetVec) {
        Vec3d posVec = targetVec.subtract(this.getPositionVector()).normalize();
        this.motionX = posVec.x*this.speed;
        this.motionY = posVec.y*this.speed;
        this.motionZ = posVec.z*this.speed;
        this.moveCounter.setValue(0);
    }

    private void moveSimple() {
        this.posX+=this.motionX;
        this.posY+=this.motionY;
        this.posZ+=this.motionZ;
        this.motionX*=0.95d;
        this.motionY*=0.95d;
        this.motionZ*=0.95d;
    }

    public void expire() {
        if(Objects.nonNull(this.boss)) {
            boolean boom = false;
            for(EntityPlayer player : this.boss.getTrackingPlayers()) {
                if(this.getDistance(player)<=4) {
                    this.boss.subtractPlayerHealth(player,10d);
                    boom = true;
                }
            }
            if(this.getDistance(this.boss)<=(boom ? 6 : 4)) this.boss.boom = true;
        }
        this.world.createExplosion(this,this.posX,this.posY,this.posZ,5f,true);
        this.setDead();
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound tag) {
        this.synced = tag.getBoolean("isSynced");
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound tag) {
        tag.setBoolean("isSynced",this.synced);
    }
}
