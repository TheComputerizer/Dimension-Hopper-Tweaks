package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lightningcraft;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sblectric.lightningcraft.config.LCConfig;
import sblectric.lightningcraft.tiles.TileEntityEnergy;
import sblectric.lightningcraft.tiles.TileEntityEnergyProvider;

import static sblectric.lightningcraft.config.LCConfig.RFtoLEConversion;

@Mixin(value = TileEntityEnergyProvider.class, remap = false)
public abstract class MixinTileEntityEnergyProvider extends TileEntityEnergy {

    @Inject(at = @At("RETURN"), method = "<init>")
    private void dimhoppertweaks$init(CallbackInfo ci) {
        ((IEnergyStorage)this.storage).dimhoppertweaks$setExtract(
                Math.min((int)this.storage.getCapacity(),RFtoLEConversion*2));
    }

    /**
     * @author The_Computerizer
     * @reason Fix weird LE conversion caps in relation to the max storage
     */
    @Overwrite
    public void update() {
        super.update();
        if(!this.world.isRemote && this.hasLPCell() && !this.world.isBlockPowered(this.pos) &&
                this.storage.getEnergyStored()<this.storage.getMaxEnergyStored() && this.canDrawCellPower(1d)) {
            this.drawCellPower(1d);
            this.storage.modifyEnergyStored(RFtoLEConversion);
            this.markDirty();
        }
    }
}