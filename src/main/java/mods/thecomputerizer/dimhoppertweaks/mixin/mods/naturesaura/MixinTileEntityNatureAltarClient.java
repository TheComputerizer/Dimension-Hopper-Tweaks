package mods.thecomputerizer.dimhoppertweaks.mixin.mods.naturesaura;

import de.ellpeck.naturesaura.blocks.tiles.TileEntityNatureAltar;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityNatureAltar.class, remap = false)
public abstract class MixinTileEntityNatureAltarClient {

    @Redirect(at = @At(value = "FIELD", target = "Lde/ellpeck/naturesaura/blocks/tiles/TileEntityNatureAltar;" +
            "bobTimer:I", opcode = Opcodes.GETFIELD), method = "update")
    private int dimhoppertweaks$doubleTimeClient(TileEntityNatureAltar instance) {
        return instance.bobTimer+1;
    }
}