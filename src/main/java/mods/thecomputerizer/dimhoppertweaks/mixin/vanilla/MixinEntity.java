package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IEntity;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

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
    @Shadow public abstract int getPortalCooldown();
    @Shadow public abstract boolean isRiding();
    @Shadow public abstract int getMaxInPortalTime();
    @Shadow @Nullable public abstract Entity changeDimension(int dimension);
    @Shadow @Nullable public abstract MinecraftServer getServer();

    @Unique
    private Entity dimhoppertweaks$cast() {
        return (Entity)(Object)this;
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
                double cornerCoord = forwards.getAxis() == EnumFacing.Axis.X ?
                        (double)frontTopLeft.getZ() : (double)frontTopLeft.getX();
                double entityCoord = forwards.getAxis()==EnumFacing.Axis.X ? this.posZ : this.posX;
                entityCoord = Math.abs(MathHelper.pct(entityCoord-
                        (double)(forwards.rotateY().getAxisDirection()==EnumFacing.AxisDirection.NEGATIVE ? 1 : 0),
                        cornerCoord,cornerCoord-(double)pattern.getWidth()));
                double d2 = MathHelper.pct(this.posY-1d,frontTopLeft.getY(),
                        frontTopLeft.getY()-pattern.getHeight());
                this.lastPortalVec = new Vec3d(entityCoord,d2,0d);
                this.teleportDirection = forwards;
            }
            this.dimhoppertweaks$inGaiaPortal = true;
        }
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
}