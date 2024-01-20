package mods.thecomputerizer.dimhoppertweaks.mixin.mods.naturesaura;

import de.ellpeck.naturesaura.blocks.tiles.TileEntityNatureAltar;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IChunk;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityNatureAltar.class, remap = false)
public abstract class MixinTileEntityNatureAltar {

    @Shadow private int timer;

    @Shadow public int bobTimer;

    @Inject(at = @At("HEAD"), method = "update", remap = true)
    private void dimhoppertweaks$doubleTime(CallbackInfo ci) {
        TileEntityNatureAltar instance = (TileEntityNatureAltar)(Object)this;
        World world = instance.getWorld();
        Chunk chunk = world.getChunk(instance.getPos());
        if(((IChunk)chunk).dimhoppertweaks$isFast()) {
            if(this.timer%2!=0) {
                this.timer++;
                if(world.isRemote) dimhoppertweaks$doubleTimeClient();
            }
        }
    }

    @Unique
    private void dimhoppertweaks$doubleTimeClient() {
        this.bobTimer++;
    }
}