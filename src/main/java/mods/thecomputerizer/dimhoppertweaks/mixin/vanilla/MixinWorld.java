package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(World.class)
public abstract class MixinWorld {

    @Shadow public abstract boolean setBlockToAir(BlockPos pos);

    @Shadow public abstract void removeEntity(Entity entityIn);

    @ModifyVariable(at = @At(value = "HEAD"), method = "setBlockState(Lnet/minecraft/util/math/BlockPos;"+
            "Lnet/minecraft/block/state/IBlockState;I)Z", ordinal = 1)
    private IBlockState dimhoppertweaks$fixBlockState(IBlockState state) {
        Block block = state.getBlock();
        return state;
    }

    @Unique
    private Block dimhoppertweaks$getBlockReplacement(String id) {
        ResourceLocation res = new ResourceLocation(id);
        return ForgeRegistries.BLOCKS.containsKey(res) ? ForgeRegistries.BLOCKS.getValue(res) : Blocks.STONE;
    }
}
