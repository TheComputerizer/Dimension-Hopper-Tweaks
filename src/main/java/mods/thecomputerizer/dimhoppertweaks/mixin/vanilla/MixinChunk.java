package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Chunk.class)
public abstract class MixinChunk {

    @Shadow @Final public int x;
    @Shadow @Final public int z;

    @Inject(at = @At("RETURN"), method = "populate(Lnet/minecraft/world/gen/IChunkGenerator;)V")
    private void dimhoopertweaks$replaceTiles(IChunkGenerator generator, CallbackInfo ci) {
        Chunk instance = (Chunk)(Object)this;
        if(instance.isTerrainPopulated()) DelayedModAccess.replaceAfterStructureGeneration(instance);
    }
}