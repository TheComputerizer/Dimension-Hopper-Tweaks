package mods.thecomputerizer.dimhoppertweaks.mixin.mods.tconstruct;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.gadgets.entity.ExplosionEFLN;

@Mixin(value = ExplosionEFLN.class, remap = false)
public class MixinExplosionEFLN {

    @Unique ExplosionEFLN dimhoppertweaks$cast() {
        return (ExplosionEFLN)(Object)this;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState("+
            "Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", ordinal = 0),
            method = "doExplosionA")
    private IBlockState dimhoppertweaks$redirectGetBlockState1(World world, BlockPos pos) {
        return DelayedModAccess.getWithOreStage(dimhoppertweaks$cast().getExplosivePlacedBy(),world.getBlockState(pos));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState("+
            "Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", ordinal = 0),
            method = "doExplosionB")
    private IBlockState dimhoppertweaks$redirectGetBlockState2(World world, BlockPos pos) {
        return DelayedModAccess.getWithOreStage(dimhoppertweaks$cast().getExplosivePlacedBy(),world.getBlockState(pos));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState("+
            "Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", ordinal = 1),
            method = "doExplosionB")
    private IBlockState dimhoppertweaks$redirectGetBlockState3(World world, BlockPos pos) {
        return DelayedModAccess.getWithOreStage(dimhoppertweaks$cast().getExplosivePlacedBy(),world.getBlockState(pos));
    }
}
