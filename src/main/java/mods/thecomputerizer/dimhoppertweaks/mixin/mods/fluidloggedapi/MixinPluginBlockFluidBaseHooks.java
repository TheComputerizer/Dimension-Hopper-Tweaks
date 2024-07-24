package mods.thecomputerizer.dimhoppertweaks.mixin.mods.fluidloggedapi;

import git.jbredwards.fluidlogged_api.api.asm.impl.IChunkProvider;
import git.jbredwards.fluidlogged_api.mod.asm.plugins.forge.PluginBlockFluidBase.Hooks;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;
import java.util.function.BiFunction;

@Mixin(value = Hooks.class, remap = false)
@SuppressWarnings("OverwriteModifiers")
public class MixinPluginBlockFluidBaseHooks {
    
    /**
     * @author The_Computerizer
     * @reason Fix null crash
     */
    @Overwrite
    @SuppressWarnings("unchecked")
    public static <T> T getFromCache(Chunk[][] chunks, IBlockAccess world, BlockPos pos, int originX, int originZ,
            BiFunction<Chunk,BlockPos,T> func) {
        int x = (pos.getX()>>4)-originX+1;
        int z = (pos.getZ()>>4)-originZ+1;
        Chunk chunk = chunks[x][z];
        if(Objects.isNull(chunk) && Objects.nonNull(world)) {
            chunks[x][z] = IChunkProvider.getChunk(world,pos);
            chunk = chunks[x][z];
        }
        return Objects.nonNull(chunk) ? func.apply(chunk,pos) : (T)DelayedModAccess.water();
    }
}