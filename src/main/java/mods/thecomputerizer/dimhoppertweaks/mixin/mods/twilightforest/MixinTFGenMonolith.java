package mods.thecomputerizer.dimhoppertweaks.mixin.mods.twilightforest;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.world.feature.TFGenMonolith;

@Mixin(value = TFGenMonolith.class, remap = false)
public abstract class MixinTFGenMonolith {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;"+
            "getDefaultState()Lnet/minecraft/block/state/IBlockState;", ordinal = 0), method = "generate")
    private IBlockState dimhoppertweaks$replaceLapis1(Block block) {
        return Blocks.STONE.getDefaultState();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;"+
            "getDefaultState()Lnet/minecraft/block/state/IBlockState;", ordinal = 2), method = "generate")
    private IBlockState dimhoppertweaks$replaceLapis2(Block block) {
        return Blocks.STONE.getDefaultState();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;"+
            "getDefaultState()Lnet/minecraft/block/state/IBlockState;", ordinal = 4), method = "generate")
    private IBlockState dimhoppertweaks$replaceLapis3(Block block) {
        return Blocks.STONE.getDefaultState();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;"+
            "getDefaultState()Lnet/minecraft/block/state/IBlockState;", ordinal = 6), method = "generate")
    private IBlockState dimhoppertweaks$replaceLapis4(Block block) {
        return Blocks.STONE.getDefaultState();
    }
}