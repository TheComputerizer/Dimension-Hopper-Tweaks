package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lightningcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import sblectric.lightningcraft.tiles.TileEntityLightningCell;
import sblectric.lightningcraft.tiles.TileEntityLightningUser;

@Mixin(value = TileEntityLightningUser.class, remap = false)
public abstract class MixinTileEntityLightningUser {

    @Shadow public abstract double getEfficiency();

    @Shadow public double cellPower;

    @Shadow public double maxPower;

    @Shadow public TileEntityLightningCell tileCell;

    /**
     * @author The_Computerizer
     * @reason Fix LE power not being able to get to the maximum value
     */
    @Overwrite
    protected boolean canAddCellPower(double amount) {
        double adjusted = amount*this.getEfficiency();
        if(adjusted<0) return false;
        return this.cellPower<this.maxPower && this.tileCell.storedPower<this.tileCell.maxPower;
    }

    /**
     * @author The_Computerizer
     * @reason Fix LE power not being able to get to the maximum value
     */
    @Overwrite
    protected boolean addCellPower(double amount) {
        if(this.canAddCellPower(amount)) {
            double adjusted = amount*this.getEfficiency();
            this.cellPower = Math.min(this.maxPower,this.cellPower+adjusted);
            this.tileCell.storedPower = Math.min(this.tileCell.maxPower,this.tileCell.storedPower+adjusted);
            return true;
        } else return false;
    }
}
