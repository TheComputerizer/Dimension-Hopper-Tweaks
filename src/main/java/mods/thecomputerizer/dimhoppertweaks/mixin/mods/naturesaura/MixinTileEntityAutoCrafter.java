package mods.thecomputerizer.dimhoppertweaks.mixin.mods.naturesaura;

import de.ellpeck.naturesaura.blocks.tiles.TileEntityAutoCrafter;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = TileEntityAutoCrafter.class, remap = false)
public abstract class MixinTileEntityAutoCrafter {

    @ModifyConstant(constant = @Constant(longValue = 60L), method = "update", remap = true)
    private long dimhoppertweaks$doubleTime(long original) {
        return DelayedModAccess.isInFastChunk((TileEntity)(Object)this) ? 30L : original;
    }
}