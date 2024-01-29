package mods.thecomputerizer.dimhoppertweaks.mixin.mods.naturesaura;

import de.ellpeck.naturesaura.blocks.tiles.TileEntityNatureAltar;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityNatureAltar.class, remap = false)
public abstract class MixinTileEntityNatureAltarClient {

    @Shadow public int bobTimer;

    @Redirect(at = @At(value = "FIELD", target = "Lde/ellpeck/naturesaura/blocks/tiles/TileEntityNatureAltar;" +
            "bobTimer:I", opcode = Opcodes.PUTFIELD), method = "update")
    private void dimhoppertweaks$doubleTimeClient(TileEntityNatureAltar instance, int value) {
        this.bobTimer = value;
        this.bobTimer++;
    }
}