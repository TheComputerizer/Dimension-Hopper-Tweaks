package mods.thecomputerizer.dimhoppertweaks.common.objects.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HomingProjectile extends EntityLiving {

    private EntityPlayer target;
    private EntityFinalBoss boss;
    private int phaseFired;
    private boolean synced;
    private float homingSpeed;

    public HomingProjectile(World world) {
        super(world);
        this.setHealth(this.getMaxHealth());
        this.isImmuneToFire = true;
        this.setSize(1f,1f);
        this.setEntityInvulnerable(true);
        this.moveHelper = new EntityFlyHelper(this);
        this.synced = false;
    }

    public void setUpdate(EntityPlayer target, EntityFinalBoss boss, int phaseFired, float homingSpeed) {
        this.target = target;
        this.boss = boss;
        this.phaseFired = phaseFired;
        this.world.updateEntity(this);
        this.synced = true;
        this.homingSpeed = homingSpeed;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
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
        if(this.target!=null) {
            Vec3d vec = this.target.getPositionVector().subtract(this.getPositionVector()).normalize();
            this.setVelocity(vec.x*this.homingSpeed,vec.y*this.homingSpeed,vec.z*this.homingSpeed);
        }
        if (this.world.isRemote) {
            for (int i = 0; i < 10; ++i) {
                this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX,
                        this.posY,
                        this.posZ,
                        (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(),
                        (rand.nextDouble() - 0.5D) * 2.0D);
            }
        }
        else if(this.synced) {
            if(this.boss==null || this.boss.isDead || this.phaseFired!=this.boss.phase) this.setDead();
            else {
                if(this.getDistance(this.target)<=2) {
                    this.boss.subtractPlayerHealth(this.target,10d);
                    this.world.createExplosion(null, this.posX, this.posY, this.posZ, 5f, true);
                    this.setDead();
                }
                else if(this.getDistance(this.boss)<=4) {
                    if(this.phaseFired==1) this.boss.setPhaseOneComplete();
                    else if(this.phaseFired==3) this.boss.setPhaseThreeComplete();
                    this.world.createExplosion(null, this.posX, this.posY, this.posZ, 5f, true);
                    this.setDead();
                }
            }
        }
        if (this.ticksExisted > 400) expire();
        super.onUpdate();
    }

    public void expire() {
        if(this.target!=null && this.getDistance(this.target)<=4) this.boss.subtractPlayerHealth(this.target,10d);
        else if(this.boss!=null && this.getDistance(this.boss)<=4) this.boss.setPhaseOneComplete();
        this.world.createExplosion(null, this.posX, this.posY, this.posZ, 5f, true);
        this.setDead();
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }
}
