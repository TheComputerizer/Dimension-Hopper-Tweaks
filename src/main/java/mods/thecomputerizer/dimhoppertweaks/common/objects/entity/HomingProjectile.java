package mods.thecomputerizer.dimhoppertweaks.common.objects.entity;

import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
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

    private final MutableInt moveCounter = new MutableInt();
    private EntityFinalBoss boss;
    private int phaseFired;
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

    public void setUpdate(EntityFinalBoss boss, int phaseFired, float speed) {
        this.boss = boss;
        this.phaseFired = phaseFired;
        Vec3d posVec = this.boss.getPositionVector().add(this.getPositionVector()).normalize();
        this.motionX = posVec.x*speed;
        this.motionY = posVec.y*speed;
        this.motionZ = posVec.z*speed;
        this.speed = speed;
        this.glowing = true;
        this.synced = true;
    }

    @Override
    protected void entityInit() {

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
                    (rand.nextDouble()-0.5d)*2d,-1d*rand.nextDouble(),(rand.nextDouble()-0.5d)*2d);
        } else {
            if(this.moveCounter.getValue()>=50) calculateMove();
            if(this.moveCounter.getValue()<20) this.moveSimple();
            else {
                this.motionX = 0d;
                this.motionY = 0d;
                this.motionZ = 0d;
            }
            this.moveCounter.increment();
            if(this.synced && (Objects.isNull(this.boss) || this.boss.isDead || this.phaseFired!=this.boss.phase))
                this.setDead();
            boolean boom = false;
            for (EntityPlayer player : this.boss.getTrackingPlayers()) {
                if (this.getDistance(player)<=(boom?4:3)) {
                    this.boss.subtractPlayerHealth(player, 10d);
                    boom = true;
                }
            }
            if (this.getDistance(this.boss)<=(boom?6:5)) {
                this.boss.boom = true;
                boom = true;
            }
            if (boom) {
                this.world.createExplosion(this,this.posX,this.posY,this.posZ,5f, true);
                this.setDead();
            } else if(this.ticksExisted>=400) expire();
        }
        super.onUpdate();
    }

    private void calculateMove() {
        EntityPlayer player = world.getClosestPlayer(this.posX,this.posY,this.posZ,100d,false);
        if(Objects.nonNull(player)) {
            Vec3d posVec = player.getPositionVector().subtract(this.getPositionVector()).normalize();
            this.motionX = posVec.x*this.speed;
            this.motionY = posVec.y*this.speed;
            this.motionZ = posVec.z*this.speed;
        }
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
                if (this.getDistance(player)<=4) {
                    this.boss.subtractPlayerHealth(player, 10d);
                    boom = true;
                }
            }
            if(this.getDistance(this.boss)<=(boom?6:4)) this.boss.boom = true;
        }
        this.world.createExplosion(this, this.posX, this.posY, this.posZ, 5f, true);
        this.setDead();
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        this.synced = compound.getBoolean("projectileSynced");
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        compound.setBoolean("projectileSynced",this.synced);
    }
}
