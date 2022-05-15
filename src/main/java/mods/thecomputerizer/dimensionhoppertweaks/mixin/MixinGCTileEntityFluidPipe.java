package mods.thecomputerizer.dimensionhoppertweaks.mixin;

import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidPipe;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidTransmitter;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = TileEntityFluidPipe.class, remap = false)
public abstract class MixinGCTileEntityFluidPipe extends TileEntityFluidTransmitter {

    public MixinGCTileEntityFluidPipe(String tileName, int pullAmount) {
        super(tileName, pullAmount);
    }

    @Inject(at = @At(value = "HEAD"), method = "hasCapability", cancellable = true)
    private void hasCapability(Capability<?> capability, EnumFacing facing, CallbackInfoReturnable<Boolean> cir) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) cir.setReturnValue(true);
        else cir.setReturnValue(super.hasCapability(capability, facing));
        cir.cancel();
    }

    @Inject(at = @At(value = "HEAD"), method = "getCapability", cancellable = true)
    private void getCapability(Capability<?> capability, EnumFacing facing, CallbackInfoReturnable<Object> cir) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            cir.setReturnValue(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FluidHandlerWrapper((TileEntityFluidPipe)(Object)this, facing)));
        } else cir.setReturnValue(super.getCapability(capability, facing));
        cir.cancel();
    }

    public FluidStack getBuffer() {
        return ((TileEntityFluidPipe)(Object)this).getBuffer();
    }

    public int getCapacity() {
        return ((TileEntityFluidPipe)(Object)this).getCapacity();
    }

    public boolean canTransmit() {
        return ((TileEntityFluidPipe)(Object)this).canTransmit();
    }

    public int fill(EnumFacing enumFacing, FluidStack fluidStack, boolean b) {
        return ((TileEntityFluidPipe)(Object)this).fill(enumFacing,fluidStack,b);
    }

    public FluidStack drain(EnumFacing enumFacing, FluidStack fluidStack, boolean b) {
        return ((TileEntityFluidPipe)(Object)this).drain(enumFacing, fluidStack, b);
    }

    public FluidStack drain(EnumFacing enumFacing, int i, boolean b) {
        return ((TileEntityFluidPipe)(Object)this).drain(enumFacing, i, b);
    }

    public boolean canFill(EnumFacing enumFacing, Fluid fluid) {
        return ((TileEntityFluidPipe)(Object)this).canFill(enumFacing, fluid);
    }

    public boolean canDrain(EnumFacing enumFacing, Fluid fluid) {
        return ((TileEntityFluidPipe)(Object)this).canDrain(enumFacing, fluid);
    }

    public FluidTankInfo[] getTankInfo(EnumFacing enumFacing) {
        return ((TileEntityFluidPipe)(Object)this).getTankInfo(enumFacing);
    }

    public int[] getSlotsForFace(EnumFacing side) {
        return ((TileEntityFluidPipe)(Object)this).getSlotsForFace(side);
    }
}
