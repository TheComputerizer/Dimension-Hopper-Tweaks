package mods.thecomputerizer.dimhoppertweaks.mixin.mods.dimdoors;

import net.minecraft.world.chunk.ChunkPrimer;
import net.tslat.aoa3.common.registration.BlockRegister;
import org.dimdev.dimdoors.shared.world.limbo.ChunkGeneratorLimbo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ChunkGeneratorLimbo.class, remap = false)
public class MixinChunkGeneratorLimbo {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/dimdev/dimdoors/shared/world/limbo/ChunkGeneratorLimbo;"+
            "setBlocksInChunk(IILnet/minecraft/world/chunk/ChunkPrimer;)V"), method = "generateChunk")
    private void dimhoppertweaks$setExtraBlocks(ChunkGeneratorLimbo chunkGen, int chunkX, int chunkZ, ChunkPrimer primer) {
        chunkGen.setBlocksInChunk(chunkX,chunkZ,primer);
        dimhoppertweaks$replaceFloor(chunkX,chunkZ,primer);
    }

    @Unique
    private void dimhoppertweaks$replaceFloor(int chunkX, int chunkZ, ChunkPrimer primer) {
        for(int x=0; x<16; x++)
            for(int z=0; z<16; z++)
                primer.setBlockState(chunkX*16+x,1,chunkZ*16+z,BlockRegister.DIMENSIONAL_FABRIC.getDefaultState());
    }
}
