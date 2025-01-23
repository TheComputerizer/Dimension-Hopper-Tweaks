package mods.thecomputerizer.dimhoppertweaks.mixin.mods.naturesaura;

import de.ellpeck.naturesaura.blocks.tiles.TileEntityNatureAltar;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityNatureAltar.class, remap = false)
public abstract class MixinTileEntityNatureAltar {

    @Shadow private int timer;
    @Inject(at = @At("HEAD"), method = "update", remap = true)
    private void dimhoppertweaks$doubleTime(CallbackInfo ci) {
        TileEntityNatureAltar instance = (TileEntityNatureAltar)(Object)this;
        if(DelayedModAccess.isFastChunk(instance.getWorld(),instance.getPos()) && this.timer%2!=0) this.timer++;
    }
}