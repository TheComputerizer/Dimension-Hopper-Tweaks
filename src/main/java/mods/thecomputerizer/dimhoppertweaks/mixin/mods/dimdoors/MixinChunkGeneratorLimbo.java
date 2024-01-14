package mods.thecomputerizer.dimhoppertweaks.mixin.mods.dimdoors;

import net.minecraft.world.chunk.ChunkPrimer;
import net.tslat.aoa3.common.registration.BlockRegister;
import org.dimdev.dimdoors.shared.world.limbo.ChunkGeneratorLimbo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ChunkGeneratorLimbo.class, remap = false)
public class MixinChunkGeneratorLimbo {

    @Inject(at = @At("RETURN"), method = "setBlocksInChunk")
    public void dimhoppertweaks$setBlocksInChunk(int chunkX, int chunkZ, ChunkPrimer primer, CallbackInfo ci) {
        for(int x=0; x<16; x++) {
            for(int z=0; z<16; z++) {
                int actualX = (chunkZ*16+z) & 15;
                int actualZ = (chunkX*16+x) & 15;
                primer.setBlockState(actualX,0,actualZ,BlockRegister.DIMENSIONAL_FABRIC.getDefaultState());
                primer.setBlockState(actualX,1,actualZ,BlockRegister.DIMENSIONAL_FABRIC.getDefaultState());
            }
        }
    }
}
