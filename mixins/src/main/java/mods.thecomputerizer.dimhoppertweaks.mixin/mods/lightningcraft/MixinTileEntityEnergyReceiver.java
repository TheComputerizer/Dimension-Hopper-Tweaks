package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lightningcraft;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sblectric.lightningcraft.tiles.TileEntityEnergy;
import sblectric.lightningcraft.tiles.TileEntityEnergyReceiver;
import sblectric.lightningcraft.util.Effect;

import static sblectric.lightningcraft.config.LCConfig.RFtoLEConversion;

@Mixin(value = TileEntityEnergyReceiver.class, remap = false)
public abstract class MixinTileEntityEnergyReceiver extends TileEntityEnergy {

    @Inject(at = @At("RETURN"), method = "<init>")
    private void dimhoppertweaks$init(CallbackInfo ci) {
        ((IEnergyStorage)this.storage).dimhoppertweaks$setReceive(
                Math.min((int)this.storage.getCapacity(),RFtoLEConversion));
    }

    /**
     * @author The_Computerizer
     * @reason Fix weird LE conversion caps in relation to the max storage
     */
    @Overwrite
    public void update() {
        super.update();
        if(!this.world.isRemote && this.hasLPCell() && !this.isRemotelyPowered() &&
            !this.world.isBlockPowered(this.pos) &&
            this.storage.getEnergyStored()>=RFtoLEConversion*10 &&
                this.cellPower<this.maxPower-100d*this.tileCell.efficiency) {
            Effect.lightningGen(this.world,this.tileCell.getPos().up());
            this.storage.modifyEnergyStored(-RFtoLEConversion*10);
            this.markDirty();
        }
    }
}