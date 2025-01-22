package mods.thecomputerizer.dimhoppertweaks.mixin.mods.naturesaura;

import de.ellpeck.naturesaura.blocks.tiles.TileEntityGratedChute;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = TileEntityGratedChute.class, remap = false)
public abstract class MixinTileEntityGratedChute {

    @ModifyConstant(constant = @Constant(intValue = 6), method = "update", remap = true)
    private int dimhoppertweaks$doubleTime(int original) {
        return DelayedModAccess.isInFastChunk((TileEntity)(Object)this) ? original/2 : original;
    }
}