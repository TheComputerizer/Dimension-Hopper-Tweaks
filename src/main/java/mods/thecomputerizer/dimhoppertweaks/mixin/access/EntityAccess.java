package mods.thecomputerizer.dimhoppertweaks.mixin.access;

import net.minecraft.block.BlockPortal;
import net.minecraft.util.math.BlockPos;

public interface EntityAccess {

    void dimhoppertweaks$setPortalOther(BlockPortal block, BlockPos pos);
}
