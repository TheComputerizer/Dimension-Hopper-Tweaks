package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.providers.DefaultProbeInfoProvider;
import mods.thecomputerizer.dimhoppertweaks.util.PsiUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = DefaultProbeInfoProvider.class, remap = false)
public abstract class MixinDefaultProbeInfoProvider {

    @Shadow private static String getBlockUnlocalizedName(Block block) {
        return null;
    }

    /**
     * @author The_Computerizer
     * @reason Account for ore stages
     */
    @SuppressWarnings("ParameterCanBeLocal")
    @Overwrite
    public static void showStandardBlockInfo(IProbeConfig config, ProbeMode mode, IProbeInfo info, IBlockState state,
                                             Block block, World world, BlockPos pos, EntityPlayer player, IProbeHitData data) {
        state = Loader.isModLoaded("orestages") ? PsiUtil.accountForOreStages(player,state) : state;
        block = state.getBlock();
        String modid = Tools.getModName(block);
        ItemStack stack = data.getPickBlock();
        if(block instanceof BlockSilverfish && mode != ProbeMode.DEBUG && !Tools.show(mode,config.getShowSilverfish())) {
            BlockSilverfish.EnumType type = state.getValue(BlockSilverfish.VARIANT);
            state = type.getModelBlock();
            block = state.getBlock();
            stack = new ItemStack(block,1,block.getMetaFromState(state));
        }
        if(block instanceof BlockFluidBase || block instanceof BlockLiquid) {
            Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
            if(Objects.nonNull(fluid)) {
                FluidStack fluidStack = new FluidStack(fluid,1000);
                ItemStack bucketStack = FluidUtil.getFilledBucket(fluidStack);
                IProbeInfo horizontal = info.horizontal();
                if(fluidStack.isFluidEqual(FluidUtil.getFluidContained(bucketStack))) horizontal.item(bucketStack);
                else horizontal.icon(fluid.getStill(),-1,-1,16,16,info.defaultIconStyle().width(20));
                horizontal.vertical().text(TextStyleClass.NAME + fluidStack.getLocalizedName()).text(TextStyleClass.MODNAME+modid);
                return;
            }
        }
        if(Objects.nonNull(stack) && !stack.isEmpty()) {
            if(Tools.show(mode, config.getShowModName()))
                info.horizontal().item(stack).vertical().itemLabel(stack).text(TextStyleClass.MODNAME+modid);
            else info.horizontal(info.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).item(stack).itemLabel(stack);
        } else if(Tools.show(mode,config.getShowModName()))
            info.vertical().text(TextStyleClass.NAME+getBlockUnlocalizedName(block)).text(TextStyleClass.MODNAME+modid);
        else info.vertical().text(TextStyleClass.NAME+getBlockUnlocalizedName(block));
    }
}
