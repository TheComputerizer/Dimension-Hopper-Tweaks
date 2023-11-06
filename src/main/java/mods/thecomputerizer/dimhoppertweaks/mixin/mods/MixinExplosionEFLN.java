package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.gadgets.entity.ExplosionEFLN;

import java.util.Objects;

@Mixin(value = ExplosionEFLN.class, remap = false)
public class MixinExplosionEFLN {

    @Unique ExplosionEFLN dimhoppertweaks$cast() {
        return (ExplosionEFLN)(Object)this;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState("+
            "Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", ordinal = 0),
            method = "doExplosionA")
    private IBlockState dimhoppertweaks$redirectGetBlockState1(World world, BlockPos pos) {
        IBlockState original = world.getBlockState(pos);
        Tuple<String,IBlockState> stageInfo = OreTiersAPI.getStageInfo(original);
        if(Objects.isNull(stageInfo)) return original;
        EntityLivingBase entity = dimhoppertweaks$cast().getExplosivePlacedBy();
        if(Objects.isNull(entity) || !(entity instanceof EntityPlayer)) return stageInfo.getSecond();
        EntityPlayer player = (EntityPlayer)entity;
        return DelayedModAccess.hasGameStage(player,stageInfo.getFirst()) ? original : stageInfo.getSecond();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState("+
            "Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", ordinal = 0),
            method = "doExplosionB")
    private IBlockState dimhoppertweaks$redirectGetBlockState2(World world, BlockPos pos) {
        IBlockState original = world.getBlockState(pos);
        Tuple<String,IBlockState> stageInfo = OreTiersAPI.getStageInfo(original);
        if(Objects.isNull(stageInfo)) return original;
        EntityLivingBase entity = dimhoppertweaks$cast().getExplosivePlacedBy();
        if(Objects.isNull(entity) || !(entity instanceof EntityPlayer)) return stageInfo.getSecond();
        EntityPlayer player = (EntityPlayer)entity;
        return DelayedModAccess.hasGameStage(player,stageInfo.getFirst()) ? original : stageInfo.getSecond();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState("+
            "Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", ordinal = 1),
            method = "doExplosionB")
    private IBlockState dimhoppertweaks$redirectGetBlockState3(World world, BlockPos pos) {
        IBlockState original = world.getBlockState(pos);
        Tuple<String,IBlockState> stageInfo = OreTiersAPI.getStageInfo(original);
        if(Objects.isNull(stageInfo)) return original;
        EntityLivingBase entity = dimhoppertweaks$cast().getExplosivePlacedBy();
        if(Objects.isNull(entity) || !(entity instanceof EntityPlayer)) return stageInfo.getSecond();
        EntityPlayer player = (EntityPlayer)entity;
        return DelayedModAccess.hasGameStage(player,stageInfo.getFirst()) ? original : stageInfo.getSecond();
    }
}
