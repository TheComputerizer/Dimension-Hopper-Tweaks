package mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class HomingProjectile extends Entity {

    private EntityPlayer target;
    private EntityFinalBoss boss;
    private int phaseFired;

    public HomingProjectile(World world) {
        super(world);
        this.updateBlocked = true;
        this.isImmuneToFire = true;
        this.setSize(1f,1f);
    }

    public void setUpdate(EntityPlayer target, EntityFinalBoss boss, int phaseFired) {
        this.target = target;
        this.boss = boss;
        this.phaseFired = phaseFired;
        this.updateBlocked = false;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    public void updateCustom() {
        if(this.boss==null || this.boss.isDead || this.phaseFired!=this.boss.phase) {
            this.target.sendMessage(new TextComponentString("SETTING DEAD"));
            this.setDead();
        }
        else {
            super.onUpdate();
            Vec3d motionVector = calculateMotionVectorTarget();
            this.setPositionAndUpdate(this.posX+motionVector.x,this.posY+motionVector.y,this.posZ+motionVector.z);
            if(this.getDistance(this.target)<=2) {
                this.boss.subtractPlayerHealth(this.target,10d);
                world.createExplosion(null, this.posX, this.posY, this.posZ, 5f, true);
                this.setDead();
            }
            else if(this.getDistance(this.boss)<=4) {
                this.boss.setPhaseOnceComplete();
                world.createExplosion(null, this.posX, this.posY, this.posZ, 5f, true);
                this.setDead();
            }
            this.target.sendMessage(new TextComponentString("Coord tracker: x: "+this.posX+" y: "+this.posY+" z: "+this.posZ));
        }
    }

    private Vec3d calculateMotionVectorTarget() {
        double distX = this.posX - this.target.posX;
        double distZ = this.posZ - this.target.posZ;
        double distY = this.posY - this.target.posY;
        double dirXZ = MathHelper.atan2(distZ, distX);
        double dirXYZ = MathHelper.atan2(distY, dirXZ);
        double speed = 1f / this.target.getDistance(this.posX, this.posY, this.posZ);
        double x = MathHelper.cos((float) dirXZ) * speed;
        double y = MathHelper.sin((float) dirXYZ) * speed;
        double z = MathHelper.sin((float) dirXZ) * speed;
        return new Vec3d(x,y,z);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
