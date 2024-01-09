package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import androsa.gaiadimension.registry.GDBlocks;
import androsa.gaiadimension.world.TeleporterGaia;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.EntityAccess;
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
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(Entity.class)
public abstract class MixinEntity implements EntityAccess {

    @Shadow public abstract int getPortalCooldown();

    @Shadow public World world;

    @Shadow protected BlockPos lastPortalPos;

    @Shadow protected Vec3d lastPortalVec;

    @Shadow protected EnumFacing teleportDirection;


    @Shadow public double posZ;

    @Shadow public double posX;

    @Shadow public double posY;

    @Shadow public abstract boolean isRiding();

    @Shadow public abstract int getMaxInPortalTime();

    @Shadow @Nullable public abstract Entity changeDimension(int dimensionIn);

    @Shadow public boolean isDead;

    @Shadow @Nullable public abstract MinecraftServer getServer();

    @Unique private boolean dimhoppertweaks$inGaiaPortal;
    @Unique private int dimhoppertweaks$gaiaPortalCounter;
    @Unique private int dimhoppertweaks$gaiaTimeUntilPortal;

    @Unique private int dimhoppertweaks$dimFrom;

    /**
     * Uses the same logic as the vanilla Entity#setPortal but without the Blocks#PORTAL hardcoding
     */
    @Override
    public void dimhoppertweaks$setPortalOther(BlockPortal block, BlockPos pos) {
        if(this.dimhoppertweaks$gaiaTimeUntilPortal>0) this.dimhoppertweaks$gaiaTimeUntilPortal = this.getPortalCooldown();
        else {
            if(!this.world.isRemote && !pos.equals(this.lastPortalPos)) {
                this.lastPortalPos = new BlockPos(pos);
                BlockPattern.PatternHelper blockpattern$patternhelper = block.createPatternHelper(this.world,this.lastPortalPos);
                double d0 = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ?
                        (double)blockpattern$patternhelper.getFrontTopLeft().getZ() :
                        (double)blockpattern$patternhelper.getFrontTopLeft().getX();
                double d1 = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? this.posZ : this.posX;
                d1 = Math.abs(MathHelper.pct(d1 -
                        (double)(blockpattern$patternhelper.getForwards().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE ? 1 : 0),
                        d0, d0 - (double)blockpattern$patternhelper.getWidth()));
                double d2 = MathHelper.pct(this.posY-1d, blockpattern$patternhelper.getFrontTopLeft().getY(),
                        blockpattern$patternhelper.getFrontTopLeft().getY()-blockpattern$patternhelper.getHeight());
                this.lastPortalVec = new Vec3d(d1,d2,0d);
                this.teleportDirection = blockpattern$patternhelper.getForwards();
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
                if(this.dimhoppertweaks$gaiaPortalCounter++ >= i) {
                    this.dimhoppertweaks$gaiaPortalCounter = i;
                    this.dimhoppertweaks$gaiaTimeUntilPortal = this.getPortalCooldown();
                    int dim;
                    if(this.world.provider.getDimensionType().getId()==-2)
                        dim = this.dimhoppertweaks$dimFrom;
                    else {
                        this.dimhoppertweaks$dimFrom = this.world.provider.getDimension();
                        dim = -2;
                    }
                    if(!this.world.isRemote && !this.isDead)
                        ((Entity)(Object)this).changeDimension(dim,new TeleporterGaia(FMLCommonHandler.instance()
                                .getMinecraftServerInstance().getWorld(dim), GDBlocks.gaia_portal,
                                GDBlocks.keystone_block.getDefaultState()));
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
}
