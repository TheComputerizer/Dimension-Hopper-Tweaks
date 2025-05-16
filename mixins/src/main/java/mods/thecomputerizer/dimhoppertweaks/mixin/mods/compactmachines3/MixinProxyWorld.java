package mods.thecomputerizer.dimhoppertweaks.mixin.mods.compactmachines3;

import mods.thecomputerizer.dimhoppertweaks.mixin.DummyChunkProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.dave.compactmachines3.world.ProxyWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.Integer.MAX_VALUE;
import static net.minecraft.world.GameType.SURVIVAL;
import static net.minecraft.world.WorldType.DEFAULT;

/**
 * Adapt the client-side memory leak fix from Universal Tweaks without setting alfheim$lightingEngine to null
 */
@Mixin(value=ProxyWorld.class,remap=false)
public abstract class MixinProxyWorld extends World {
    
    @Unique private static final WorldSettings DEFAULT_SETTINGS = new WorldSettings(
            1L,SURVIVAL,true,false,DEFAULT);
    
    @Shadow @Final @Mutable private World realWorld;
    
    protected MixinProxyWorld(ISaveHandler saveHandler, WorldInfo info, WorldProvider provider, Profiler profiler,
            boolean client) {
        super(saveHandler,info,provider,profiler,client);
    }
    
    @Redirect(method="<init>*",at=@At(value="INVOKE",target="Lnet/minecraft/client/multiplayer/WorldClient;"+
            "getWorldInfo()Lnet/minecraft/world/storage/WorldInfo;",remap=true))
    private static WorldInfo dimhoppertweaks$dummyWorldInfo(WorldClient world) {
        return new WorldInfo(DEFAULT_SETTINGS,"Dummy");
    }
    
    @Redirect(method="<init>*",at=@At(value="FIELD",target="Lnet/minecraft/client/multiplayer/WorldClient;"+
            "provider:Lnet/minecraft/world/WorldProvider;",remap=true))
    private static WorldProvider dimhoppertweaks$dummyWorldProvider(WorldClient world) {
        return new WorldProviderSurface();
    }
    
    @Redirect(method="<init>*",at=@At(value="FIELD",target="Lnet/minecraft/client/multiplayer/WorldClient;"+
            "profiler:Lnet/minecraft/profiler/Profiler;",remap=true))
    private static Profiler dimhoppertweaks$dummyProfiler(WorldClient world) {
        return new Profiler();
    }
    
    @Inject(method="<init>*",at=@At(value="TAIL"))
    private void dimhoppertweaks$setupDummyWorld(CallbackInfo ci) {
        //Guarantee the dimension ID was not reset by the provider
        this.provider.setDimension(MAX_VALUE-1024);
        int dimension = this.provider.getDimension();
        this.provider.setWorld(this);
        this.provider.setDimension(dimension);
        this.chunkProvider = createChunkProvider();
        calculateInitialSkylight();
        calculateInitialWeather();
        getWorldBorder().setSize(30000000);
        this.realWorld = null;
    }
    
    /**
     * @author jchung01
     * @reason Don't use realWorld
     */
    @Overwrite
    protected @Nonnull IChunkProvider createChunkProvider() {
        return new DummyChunkProvider(this);
    }
    
    @Override protected void initCapabilities() {}
    
    @Override public void notifyNeighborsRespectDebug(@Nonnull BlockPos pos, @Nonnull Block blockType,
            boolean updateObservers) {}
    
    @Override public void notifyNeighborsOfStateChange(@Nonnull BlockPos pos, @Nonnull Block blockType,
            boolean updateObservers) {}
    
    @Override public void notifyNeighborsOfStateExcept(@Nonnull BlockPos pos, @Nonnull Block blockType,
            @Nonnull EnumFacing skipSide) {}
    
    @Override public void markAndNotifyBlock(@Nonnull BlockPos pos, @Nullable Chunk chunk,
            @Nonnull IBlockState oldState, @Nonnull IBlockState newState, int flags) {}
    
    @Override public void notifyBlockUpdate(@Nonnull BlockPos pos, @Nonnull IBlockState oldState,
            @Nonnull IBlockState newState, int flags) {}
    
    @Override public void markBlockRangeForRenderUpdate(@Nonnull BlockPos min, @Nonnull BlockPos max) {}
    
    @Override public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {}
    
    @Override public void updateObservingBlocksAt(@Nonnull BlockPos pos, @Nonnull Block type) {}
    
    @Override public boolean checkLightFor(@Nonnull EnumSkyBlock lightType, @Nonnull BlockPos pos) {
        return true;
    }
    
    @Override public @Nonnull World init() {
        return this;
    }
    
    @Override public int getLightFromNeighborsFor(@Nonnull EnumSkyBlock type, @Nonnull BlockPos pos) {
        return 15;
    }
    
    @SuppressWarnings({"MissingUnique", "unused"})
    public int alfheim$getLight(BlockPos pos, boolean checkNeighbors) {
        return 15;
    }
}