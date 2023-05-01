package mods.thecomputerizer.dimhoppertweaks.mixin.access;

import gcewing.sg.BaseModClient;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface BaseRenderingManagerAccess {

    BaseModClient.ICustomRenderer accessGetCustomRenderer(IBlockAccess world, BlockPos pos, IBlockState state);
}
