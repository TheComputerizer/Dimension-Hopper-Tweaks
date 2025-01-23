package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.entity.layers.LayerHeldBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(LayerHeldBlock.class)
public abstract class MixinLayerHeldBlock {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockRendererDispatcher;"+
            "renderBlockBrightness(Lnet/minecraft/block/state/IBlockState;F)V"),
            method = "doRenderLayer(Lnet/minecraft/entity/monster/EntityEnderman;FFFFFFF)V")
    private void dimhoppertweaks$renderBlockBrightnessWithException(
            BlockRendererDispatcher dispatcher, IBlockState state, float brightness) {
        try {
            dispatcher.renderBlockBrightness(state,brightness);
        } catch(Exception ex) {
            Block block = state.getBlock();
            LOGGER.error("Caught an error trying to render the held block for an enderman! "+
                            "`BLOCK - {} | CLASS - {} | TOSTRING {}`",block.getRegistryName(),block.getClass(),state,ex);
        }
    }
}