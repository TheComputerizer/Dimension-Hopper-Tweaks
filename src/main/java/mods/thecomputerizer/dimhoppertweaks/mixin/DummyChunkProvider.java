package mods.thecomputerizer.dimhoppertweaks.mixin;

import io.netty.util.collection.LongObjectHashMap;
import io.netty.util.collection.LongObjectMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Adapted from UniversalTweaks mod.acgaming.universaltweaks.mods.compactmachines.memory.DummyWorld$DummyChunkProvider
 */
public class DummyChunkProvider implements IChunkProvider {
    
    private final World world;
    private final LongObjectMap<Chunk> loadedChunks = new LongObjectHashMap<>();
    
    public DummyChunkProvider(World world) {
        this.world = world;
    }
    
    @Override public @Nullable Chunk getLoadedChunk(int x, int z) {
        return this.loadedChunks.get(ChunkPos.asLong(x,z));
    }
    
    @Override public @Nonnull Chunk provideChunk(int x, int z) {
        long chunkKey = ChunkPos.asLong(x,z);
        if(this.loadedChunks.containsKey(chunkKey)) return this.loadedChunks.get(chunkKey);
        Chunk chunk = new Chunk(this.world,x,z);
        this.loadedChunks.put(chunkKey,chunk);
        return chunk;
    }
    
    @Override public boolean tick() {
        for(Chunk chunk : this.loadedChunks.values()) chunk.onTick(false);
        return !this.loadedChunks.isEmpty();
    }
    
    @Override @Nonnull public String makeString() {
        return "Dummy";
    }
    
    @Override public boolean isChunkGeneratedAt(int x, int z) {
        return true;
    }
}