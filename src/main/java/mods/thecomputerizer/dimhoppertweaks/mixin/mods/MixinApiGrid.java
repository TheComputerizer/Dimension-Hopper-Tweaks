package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import appeng.api.networking.IGridBlock;
import appeng.api.networking.IGridNode;
import appeng.core.api.ApiGrid;
import appeng.me.GridNode;
import appeng.util.Platform;
import com.cleanroommc.multiblocked.client.util.TrackedDummyWorld;
import com.google.common.base.Preconditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ApiGrid.class, remap = false)
public class MixinApiGrid {

    /**
     * @author The_Computerizer
     * @reason Try ignoring the exception when inside a TrackedDummyWorld from mbd
     */
    @Overwrite
    public IGridNode createGridNode(IGridBlock blk) {
        Preconditions.checkNotNull(blk);
        if(Platform.isClient()) {
            if(blk.getLocation().getWorld() instanceof TrackedDummyWorld) return new GridNode(blk);
            throw new IllegalStateException("Grid features for "+blk+" are server side only.");
        } else return new GridNode(blk);
    }
}
