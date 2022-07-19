package mods.thecomputerizer.dimensionhoppertweaks.mixin;

import mekanism.common.capabilities.Capabilities;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygen;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityElectrolyzer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileEntityOxygen.class,remap = false)
public abstract class MixinTileEntityOxygen extends TileBaseElectricBlock {

    public MixinTileEntityOxygen(String tileName) {
        super(tileName);
    }

    @Inject(at = @At(value = "HEAD"), method = "hasCapability", cancellable = true)
    private void hasCapability(Capability<?> capability, EnumFacing facing, CallbackInfoReturnable<Boolean> cir) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) cir.setReturnValue(true);
        else cir.setReturnValue(super.hasCapability(capability, facing));
        cir.cancel();
    }

    @Inject(at = @At(value = "HEAD"), method = "getCapability", cancellable = true)
    private void getCapability(Capability<?> capability, EnumFacing facing, CallbackInfoReturnable<Object> cir) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            cir.setReturnValue(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FluidHandlerWrapper((TileEntityOxygen)(Object)this, facing)));
        else cir.setReturnValue(super.getCapability(capability,facing));
        cir.cancel();
    }
}
