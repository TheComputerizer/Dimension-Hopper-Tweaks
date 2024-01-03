package mods.thecomputerizer.dimhoppertweaks.mixin.mods.tconstruct;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.gadgets.Exploder;
import slimeknights.tconstruct.gadgets.entity.EntityThrowball;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(value = Exploder.class, remap = false)
public class MixinExploder {

    @Shadow @Final public Entity exploder;

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(" +
            "Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"), method = "iteration")
    private IBlockState dimhoppertweaks$redirectGetBlockState4(World world, BlockPos pos) {
        return DelayedModAccess.getWithOreStage(this.exploder,world.getBlockState(pos));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(" +
            "Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"), method = "explodeBlock")
    private IBlockState dimhoppertweaks$redirectGetBlockState5(World world, BlockPos pos) {
        return DelayedModAccess.getWithOreStage(this.exploder,world.getBlockState(pos));
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;"+
            "fireBlockHarvesting(Ljava/util/List;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;"+
            "Lnet/minecraft/block/state/IBlockState;IFZLnet/minecraft/entity/player/EntityPlayer;)F"), method = "explodeBlock")
    private EntityPlayer dimhoppertweaks$addStagingPlayer(@Nullable EntityPlayer original) {
        EntityThrowball ball = this.exploder instanceof EntityThrowball ? (EntityThrowball)this.exploder : null;
        Entity actualExploder = Objects.nonNull(ball) ? ball.getThrower() : this.exploder;
        return actualExploder instanceof EntityPlayer ? (EntityPlayer)actualExploder : null;
    }
}
