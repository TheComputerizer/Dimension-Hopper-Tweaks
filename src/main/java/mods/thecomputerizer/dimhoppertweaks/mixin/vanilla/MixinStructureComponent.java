package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.structures.StructureTFComponent;

@Mixin(StructureComponent.class)
public abstract class MixinStructureComponent {

    @Shadow protected abstract int getXWithOffset(int x, int z);
    @Shadow protected abstract int getYWithOffset(int y);
    @Shadow protected abstract int getZWithOffset(int x, int z);

    @Unique
    private StructureComponent dimhoppertweaks$cast() {
        return (StructureComponent)(Object)this;
    }

    @Inject(at = @At(value = "HEAD"), method = "setBlockState", cancellable = true)
    private void dimhoppertweaks$setBlockState(
            World world, IBlockState state, int x, int y, int z, StructureBoundingBox box, CallbackInfo ci) {
        BlockPos pos = new BlockPos(this.getXWithOffset(x,z),this.getYWithOffset(y),this.getZWithOffset(x,z));
        if(box.isVecInside(pos)) {
            Block block = state.getBlock();
            StructureComponent instance = dimhoppertweaks$cast();
            if(block instanceof BlockAnvil || (instance instanceof StructureTFComponent &&
                    (block==Blocks.IRON_BLOCK || block instanceof BlockSoulSand))) ci.cancel();
        }
    }
}