package mods.thecomputerizer.dimhoppertweaks.mixin.mods.dimdoors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.ChunkPrimer;
import net.tslat.aoa3.common.registration.BlockRegister;
import org.dimdev.dimdoors.shared.world.limbo.ChunkGeneratorLimbo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ChunkGeneratorLimbo.class, remap = false)
public class MixinChunkGeneratorLimbo {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkPrimer;"+
            "setBlockState(IIILnet/minecraft/block/state/IBlockState;)V", ordinal = 0), method = "setBlocksInChunk")
    private void dimhoppertweaks$replaceFloor1(ChunkPrimer primer, int x, int y, int z, IBlockState state) {
        if(y<=0) state = BlockRegister.DIMENSIONAL_FABRIC.getDefaultState();
        primer.setBlockState(x,y,z,state);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkPrimer;"+
            "setBlockState(IIILnet/minecraft/block/state/IBlockState;)V", ordinal = 1), method = "setBlocksInChunk")
    private void dimhoppertweaks$replaceFloor2(ChunkPrimer primer, int x, int y, int z, IBlockState state) {
        if(y<=0) state = BlockRegister.DIMENSIONAL_FABRIC.getDefaultState();
        primer.setBlockState(x,y,z,state);
    }
}
