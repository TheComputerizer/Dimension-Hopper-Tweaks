package mods.thecomputerizer.dimhoppertweaks.mixin.api;

import net.minecraft.block.BlockPortal;
import net.minecraft.util.math.BlockPos;

public interface IEntity {

    double dimhoppertweaks$getGravityFactor();
    void dimhoppertweaks$setGravityFactor(double gravityFactor);
    void dimhoppertweaks$setPortalOther(BlockPortal block, BlockPos pos);
}