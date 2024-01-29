package mods.thecomputerizer.dimhoppertweaks.mixin.mods.naturesaura;

import de.ellpeck.naturesaura.blocks.tiles.TileEntityNatureAltar;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityNatureAltar.class, remap = false)
public abstract class MixinTileEntityNatureAltar {

    @Shadow private int timer;

    @Redirect(at = @At(value = "FIELD", target = "Lde/ellpeck/naturesaura/blocks/tiles/TileEntityNatureAltar;timer:I",
            opcode = Opcodes.PUTFIELD, ordinal = 0), method = "update")
    private void dimhoppertweaks$doubleTime(TileEntityNatureAltar instance, int value) {
        this.timer = value;
        this.timer++;
    }
}