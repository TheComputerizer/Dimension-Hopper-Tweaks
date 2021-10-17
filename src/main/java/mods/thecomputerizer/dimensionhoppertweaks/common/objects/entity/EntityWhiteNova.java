package mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWhiteNova extends Entity {
    EntityLivingBase EntityFrom;
    EntityLivingBase EntityTo;

    @SuppressWarnings("unused")
    public EntityWhiteNova(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
    }

    public EntityWhiteNova(World worldIn, EntityLivingBase shooter, EntityLivingBase target) {
        super(worldIn);
        this.setCustomNameTag("White Nova");
        this.EntityFrom = shooter;
        this.EntityTo = target;
        this.noClip = true;
        this.setNoGravity(true);
        this.setSize(1.0F, 1.0F);
    }

    public boolean isBurning() {
        return false;
    }

    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return 3600000.8F;
    }


    public void onUpdate() {
        if (this.world.isRemote || (this.EntityFrom == null || !this.EntityFrom.isDead)) {
            super.onUpdate();
            if (this.getDistance(this.EntityTo) < 1) {
                if (this.EntityTo instanceof EntityFinalBoss) {
                    EntityFinalBoss.allowForcefield = false;
                    this.world.playSound(this.EntityTo.posX, this.EntityTo.posY, this.EntityTo.posZ, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1.0F, 1.0F, false);
                }
                this.onImpact();
            }
            this.posX += ((this.EntityTo.posX - this.posX) / 10);
            this.posY += ((this.EntityTo.posY - this.posY) / 10);
            this.posZ += ((this.EntityTo.posZ - this.posZ) / 10);
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            this.world.spawnParticle(EnumParticleTypes.END_ROD, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    protected void onImpact() {
        if (!this.world.isRemote) {
            this.EntityTo.setPositionAndUpdate(this.posX, this.posY, this.posZ);
            if (this.EntityFrom != null) {
                if (this.EntityTo.attackEntityFrom(DamageSource.causeMobDamage(this.EntityFrom), 100000.0F)) {
                    if (this.EntityTo.isEntityAlive()) {
                        this.applyEnchantments(this.EntityFrom, this.EntityTo);
                    }
                }
            } else {
                this.EntityTo.attackEntityFrom(DamageSource.MAGIC, 100000.0F);
            }

            this.world.newExplosion(this, this.posX, this.posY, this.posZ, 10.0F, false, true);
            this.setDead();
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    protected void entityInit() {
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getTrueSource() instanceof EntityPlayer) {
            EntityLivingBase temp = this.EntityFrom;
            this.EntityFrom = this.EntityTo;
            this.EntityTo = temp;
            return true;
        }
        return false;
    }

    public float getBrightness() {
        return 1.0F;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }
}
