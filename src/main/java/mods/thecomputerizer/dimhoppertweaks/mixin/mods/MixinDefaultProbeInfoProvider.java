package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.providers.DefaultProbeInfoProvider;
import mods.thecomputerizer.dimhoppertweaks.util.PsiUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DefaultProbeInfoProvider.class, remap = false)
public class MixinDefaultProbeInfoProvider {

    @Inject(at = @At(value = "HEAD"), method = "showStandardBlockInfo")
    private static void dimhoppertweaks_showStandardBlockInfo(IProbeConfig config, ProbeMode mode, IProbeInfo probeInfo,
                                                              IBlockState blockState, Block block, World world, BlockPos pos,
                                                              EntityPlayer player, IProbeHitData data,
                                                              CallbackInfo ci) {
        blockState = Loader.isModLoaded("orestages") ? PsiUtil.accountForOreStages(player,blockState) : blockState;
        block = blockState.getBlock();
    }
}
