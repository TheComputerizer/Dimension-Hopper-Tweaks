package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IChunk;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Chunk.class)
public abstract class MixinChunk implements IChunk {
    @Unique private boolean dimhoppertweaks$isFast = false;

    @Inject(at = @At("RETURN"), method = "populate(Lnet/minecraft/world/gen/IChunkGenerator;)V")
    private void dimhoopertweaks$replaceTiles(IChunkGenerator generator, CallbackInfo ci) {
        Chunk instance = (Chunk)(Object)this;
        if(instance.isTerrainPopulated()) DelayedModAccess.replaceAfterStructureGeneration(instance);
    }

    @Override
    public boolean dimhoppertweaks$isFast() {
        return this.dimhoppertweaks$isFast;
    }

    @Override
    public void dimhoppertweaks$setFast(boolean fast) {
        this.dimhoppertweaks$isFast = fast;
    }
}