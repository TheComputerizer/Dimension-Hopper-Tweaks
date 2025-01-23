package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkProviderServer.class)
public abstract class MixinChunkProviderServer {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;"+
            "populate(Lnet/minecraft/world/chunk/IChunkProvider;Lnet/minecraft/world/gen/IChunkGenerator;)V"),
            method = "loadChunk(IILjava/lang/Runnable;)Lnet/minecraft/world/chunk/Chunk;")
    private void dimhoppertweaks$runPostGenerationReplacements(Chunk chunk, IChunkProvider provider, IChunkGenerator generator) {
        chunk.populate(provider,generator);
        DelayedModAccess.replaceAfterStructureGeneration(chunk);
    }
}