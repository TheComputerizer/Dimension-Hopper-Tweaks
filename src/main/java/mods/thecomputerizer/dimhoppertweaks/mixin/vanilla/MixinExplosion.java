package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(Explosion.class)
public abstract class MixinExplosion {

    @Shadow @Nullable public abstract EntityLivingBase getExplosivePlacedBy();

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState("+
            "Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"), method = "doExplosionA")
    private IBlockState dimhoppertweaks$getStagedBlockState1(World world, BlockPos pos) {
        return DelayedModAccess.getWithOreStage(this.getExplosivePlacedBy(),world.getBlockState(pos));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState("+
            "Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", ordinal = 0),
            method = "doExplosionB")
    private IBlockState dimhoppertweaks$getStagedBlockState2(World world, BlockPos pos) {
        return DelayedModAccess.getWithOreStage(this.getExplosivePlacedBy(),world.getBlockState(pos));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState("+
            "Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", ordinal = 1),
            method = "doExplosionB")
    private IBlockState dimhoppertweaks$getStagedBlockState3(World world, BlockPos pos) {
        return DelayedModAccess.getWithOreStage(this.getExplosivePlacedBy(),world.getBlockState(pos));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState("+
            "Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", ordinal = 2),
            method = "doExplosionB")
    private IBlockState dimhoppertweaks$getStagedBlockState4(World world, BlockPos pos) {
        return DelayedModAccess.getWithOreStage(this.getExplosivePlacedBy(),world.getBlockState(pos));
    }
}