package mods.thecomputerizer.dimhoppertweaks.mixin.mods.naturesaura;

import de.ellpeck.naturesaura.blocks.tiles.TileEntityWoodStand;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = TileEntityWoodStand.class, remap = false)
public abstract class MixinTileEntityWoodStand {

    @ModifyConstant(constant = @Constant(intValue = 5, ordinal = 0), method = "update", remap = true)
    private int dimhoppertweaks$doubleTime(int original) {
        return DelayedModAccess.isInFastChunk((TileEntity)(Object)this) ? original*2 : original;
    }
}