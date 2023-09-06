package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import gcewing.sg.BaseModClient;
import gcewing.sg.BaseRenderingManager;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.BaseRenderingManagerAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = BaseRenderingManager.class)
public abstract class MixinBaseRenderingManager implements BaseRenderingManagerAccess {
    @Shadow protected abstract BaseModClient.ICustomRenderer getCustomRenderer(IBlockAccess world, BlockPos pos, IBlockState state);

    @Override
    public BaseModClient.ICustomRenderer dimhoppertweaks$accessGetCustomRenderer(IBlockAccess world, BlockPos pos, IBlockState state) {
        return this.getCustomRenderer(world,pos,state);
    }
}
