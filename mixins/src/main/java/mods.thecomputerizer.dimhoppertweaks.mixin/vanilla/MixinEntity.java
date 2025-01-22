package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IEntity;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

import static net.minecraft.util.EnumFacing.Axis.X;
import static net.minecraft.util.EnumFacing.AxisDirection.NEGATIVE;

@Mixin(Entity.class)
public abstract class MixinEntity implements IEntity {

    @Shadow public World world;
    @Shadow protected BlockPos lastPortalPos;
    @Shadow protected Vec3d lastPortalVec;
    @Shadow protected EnumFacing teleportDirection;
    @Shadow public double posZ;
    @Shadow public double posX;
    @Shadow public double posY;
    @Shadow public boolean isDead;
    @Shadow public boolean onGround;
    @Unique private boolean dimhoppertweaks$inGaiaPortal;
    @Unique private int dimhoppertweaks$gaiaPortalCounter;
    @Unique private int dimhoppertweaks$gaiaTimeUntilPortal;
    @Unique private int dimhoppertweaks$dimFrom;
    @Unique private double dimhoppertweaks$gravityFactor = 1d;
    @Shadow public abstract int getPortalCooldown();
    @Shadow public abstract boolean isRiding();
    @Shadow public abstract int getMaxInPortalTime();
    @Shadow @Nullable public abstract Entity changeDimension(int dimension);
    @Shadow @Nullable public abstract MinecraftServer getServer();
    @Shadow public abstract boolean isInWater();
    
    @Unique
    private Entity dimhoppertweaks$cast() {
        return (Entity)(Object)this;
    }

    @Override
    public double dimhoppertweaks$getGravityFactor() {
        return this.dimhoppertweaks$gravityFactor;
    }
    
    @ModifyVariable(at = @At("HEAD"), method = "move", ordinal = 0, argsOnly = true)
    private double dimhoppertweaks$moveX(double x) {
        return isInWater() && SkillWrapper.isGoodSwimmer(dimhoppertweaks$cast()) ? x*2d : x;
    }
    
    @ModifyVariable(at = @At("HEAD"), method = "move", ordinal = 1, argsOnly = true)
    private double dimhoppertweaks$moveY(double y) {
        return isInWater() && SkillWrapper.isGoodSwimmer(dimhoppertweaks$cast()) ? y*1.5d : y;
    }
    
    @ModifyVariable(at = @At("HEAD"), method = "move", ordinal = 2, argsOnly = true)
    private double dimhoppertweaks$moveZ(double z) {
        return isInWater() && SkillWrapper.isGoodSwimmer(dimhoppertweaks$cast()) ? z*2d : z;
    }
    
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V",
            ordinal = 1), method = "onEntityUpdate")
    private void dimhoppertweaks$onPortalProfile(Profiler profiler, String name) {
        profiler.startSection(name);
        if(this.dimhoppertweaks$inGaiaPortal) {
            if(!this.isRiding()) {
                int i = this.getMaxInPortalTime();
                if(this.dimhoppertweaks$gaiaPortalCounter++>=i) {
                    this.dimhoppertweaks$gaiaPortalCounter = i;
                    this.dimhoppertweaks$gaiaTimeUntilPortal = this.getPortalCooldown();
                    int dim;
                    if(this.world.provider.getDimensionType().getId()==-2) dim = this.dimhoppertweaks$dimFrom;
                    else {
                        this.dimhoppertweaks$dimFrom = this.world.provider.getDimension();
                        dim = -2;
                    }
                    if(!this.world.isRemote && !this.isDead)
                        dimhoppertweaks$cast().changeDimension(dim,DelayedModAccess.makeGaiaTeleporter(dim));
                }
            }
            this.dimhoppertweaks$inGaiaPortal = false;
        }
        else {
            if(this.dimhoppertweaks$gaiaPortalCounter>0) this.dimhoppertweaks$gaiaPortalCounter-=4;
            if(this.dimhoppertweaks$gaiaPortalCounter<0) this.dimhoppertweaks$gaiaPortalCounter = 0;
        }
        if(this.dimhoppertweaks$gaiaTimeUntilPortal>0) this.dimhoppertweaks$gaiaTimeUntilPortal--;
    }
    
    @Inject(at = @At("RETURN"), method = "onEntityUpdate")
    private void setDimhoppertweaks$onEntityUpdate(CallbackInfo ci) {
        if(!this.world.isRemote && this.onGround) SkillWrapper.resetFanUsage(dimhoppertweaks$cast());
    }

    @Override
    public void dimhoppertweaks$setGravityFactor(double gravityFactor) {
        if(gravityFactor<=0) gravityFactor = 1d;
        this.dimhoppertweaks$gravityFactor = gravityFactor;
    }

    /**
     * Uses the same logic as the vanilla Entity#setPortal but without the Blocks#PORTAL hardcoding
     */
    @Override
    public void dimhoppertweaks$setPortalOther(BlockPortal block, BlockPos pos) {
        if(this.dimhoppertweaks$gaiaTimeUntilPortal>0)
            this.dimhoppertweaks$gaiaTimeUntilPortal = this.getPortalCooldown();
        else {
            if(!this.world.isRemote && !pos.equals(this.lastPortalPos)) {
                this.lastPortalPos = new BlockPos(pos);
                BlockPattern.PatternHelper pattern = block.createPatternHelper(this.world,this.lastPortalPos);
                EnumFacing forwards = pattern.getForwards();
                BlockPos frontTopLeft = pattern.getFrontTopLeft();
                double cornerCoord = forwards.getAxis()==X ?
                        (double)frontTopLeft.getZ() : (double)frontTopLeft.getX();
                double entityCoord = forwards.getAxis()==X ? this.posZ : this.posX;
                entityCoord = Math.abs(MathHelper.pct(entityCoord-
                        (double)(forwards.rotateY().getAxisDirection()==NEGATIVE ? 1 : 0),
                        cornerCoord,cornerCoord-(double)pattern.getWidth()));
                double d2 = MathHelper.pct(this.posY-1d,frontTopLeft.getY(),
                        frontTopLeft.getY()-pattern.getHeight());
                this.lastPortalVec = new Vec3d(entityCoord,d2,0d);
                this.teleportDirection = forwards;
            }
            this.dimhoppertweaks$inGaiaPortal = true;
        }
    }

    @Inject(at = @At("RETURN"), method = "writeToNBT")
    private void dimhoppertweaks$writeToNBT(NBTTagCompound compound, CallbackInfoReturnable<NBTTagCompound> cir) {
        compound.setDouble("gravityFactor",this.dimhoppertweaks$gravityFactor);
    }

    @Inject(at = @At("RETURN"), method = "readFromNBT")
    private void dimhoppertweaks$readFromNBT(NBTTagCompound compound, CallbackInfo ci) {
        dimhoppertweaks$setGravityFactor(compound.getDouble("gravityFactor"));
    }
}