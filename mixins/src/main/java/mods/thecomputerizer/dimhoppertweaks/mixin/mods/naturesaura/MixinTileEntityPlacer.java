package mods.thecomputerizer.dimhoppertweaks.mixin.mods.naturesaura;

import de.ellpeck.naturesaura.blocks.tiles.TileEntityPlacer;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = TileEntityPlacer.class, remap = false)
public abstract class MixinTileEntityPlacer {

    @ModifyConstant(constant = @Constant(longValue = 15L), method = "update", remap = true)
    private long dimhoppertweaks$doubleTime(long original) {
        return DelayedModAccess.isInFastChunk((TileEntity)(Object)this) ? original/2L : original;
    }
}