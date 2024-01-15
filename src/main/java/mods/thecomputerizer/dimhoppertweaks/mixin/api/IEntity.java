package mods.thecomputerizer.dimhoppertweaks.mixin.api;

import net.minecraft.block.BlockPortal;
import net.minecraft.util.math.BlockPos;

public interface IEntity {

    void dimhoppertweaks$setPortalOther(BlockPortal block, BlockPos pos);
}