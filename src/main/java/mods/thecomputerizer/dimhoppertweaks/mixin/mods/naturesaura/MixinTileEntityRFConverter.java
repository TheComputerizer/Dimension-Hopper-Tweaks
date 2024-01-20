package mods.thecomputerizer.dimhoppertweaks.mixin.mods.naturesaura;

import de.ellpeck.naturesaura.blocks.tiles.TileEntityRFConverter;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = TileEntityRFConverter.class, remap = false)
public abstract class MixinTileEntityRFConverter {

    @ModifyConstant(constant = @Constant(longValue = 20L), method = "update", remap = true)
    private long dimhoppertweaks$doubleTime(long original) {
        return DelayedModAccess.isInFastChunk((TileEntity)(Object)this) ? original/2L : original;
    }
}