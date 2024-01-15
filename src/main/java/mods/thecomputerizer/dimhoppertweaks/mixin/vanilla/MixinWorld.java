package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(World.class)
public abstract class MixinWorld {

    @ModifyVariable(at = @At(value = "HEAD"), method = "setBlockState(Lnet/minecraft/util/math/BlockPos;"+
            "Lnet/minecraft/block/state/IBlockState;I)Z", ordinal = 1)
    private IBlockState dimhoppertweaks$fixBlockState(IBlockState state) {
        return state;
    }
}