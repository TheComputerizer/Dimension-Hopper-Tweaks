package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.gadgets.Exploder;

import java.util.Objects;

@Mixin(value = Exploder.class, remap = false)
public class MixinExploder {

    @Shadow @Final public Entity exploder;

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(" +
            "Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"), method = "explodeBlock")
    private IBlockState dimhoppertweaks$redirectGetBlockState(World world, BlockPos pos) {
        IBlockState original = world.getBlockState(pos);
        Tuple<String,IBlockState> stageInfo = OreTiersAPI.getStageInfo(original);
        if(Objects.isNull(stageInfo)) return original;
        if(Objects.isNull(this.exploder) || !(this.exploder instanceof EntityPlayer)) return stageInfo.getSecond();
        EntityPlayer player = (EntityPlayer)this.exploder;
        return DelayedModAccess.hasGameStage(player,stageInfo.getFirst()) ? original : stageInfo.getSecond();
    }
}
