package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import gcewing.sg.BaseAORenderingManager;
import gcewing.sg.BaseBakedRenderTarget;
import gcewing.sg.BaseModClient;
import gcewing.sg.Trans3;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "gcewing.sg.BaseAORenderingManager$CustomBlockRendererDispatcher")
public abstract class MixinCustomBlockRendererDispatcher {
    @Shadow protected BlockRendererDispatcher base;

    @Shadow @Final BaseAORenderingManager this$0;

    @Shadow public abstract BlockModelRenderer getBlockModelRenderer();

    /**
     * @return
     * @author The_Computerizer
     * @reason Crash when trying to mine stargate ring blocks
     */
    /*
    @Overwrite
    public void renderBlockDamage(@Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull TextureAtlasSprite icon, @Nonnull IBlockAccess world) {
        try {
            BaseModClient.ICustomRenderer rend = ((BaseRenderingManagerAccess)this$0).dimhoppertweaks$accessGetCustomRenderer(world, pos, state);
            if (Objects.nonNull(rend)) {
                BaseBakedRenderTarget target = new BaseBakedRenderTarget(pos, icon);
                for (BlockRenderLayer layer : BlockRenderLayer.values())
                    if (state.getBlock().canRenderInLayer(state, layer))
                        rend.renderBlock(world, pos, state, target, layer, Trans3.blockCenter);
                this.getBlockModelRenderer().renderModel(world, target.getBakedModel(), state, pos, Tessellator.getInstance().getBuffer(), false);
                return;
            }
        } catch (Exception ignored) {}
        try {
            this.base.renderBlockDamage(state, pos, icon, world);
        } catch (Exception ignored) {}
        try {
            this.base.renderBlockDamage(Blocks.AIR.getDefaultState(), pos, icon, world);
        } catch (Exception e) {
            Constants.LOGGER.error("Stargate Damage Render Catch",e);
        }
    }
     */

    @Redirect(at = @At(value = "INVOKE", target = "Lgcewing/sg/BaseModClient$ICustomRenderer;" +
            "renderBlock(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;" +
            "Lnet/minecraft/block/state/IBlockState;Lgcewing/sg/BaseModClient$IRenderTarget;" +
            "Lnet/minecraft/util/BlockRenderLayer;Lgcewing/sg/Trans3;)V"), method = "renderBlockDamage")
    private void dimhoppertweaks$onRenderBlock(BaseModClient.ICustomRenderer renderer, IBlockAccess block, BlockPos pos,
                                               IBlockState state, BaseModClient.IRenderTarget target,
                                               BlockRenderLayer layer, Trans3 trans) {
        Constants.LOGGER.error("TESTING SGCRAFT BLOCK RENDER OF CLASS {}",renderer.getClass().getName());
        renderer.renderBlock(block,pos,state,target,layer,trans);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lgcewing/sg/BaseBakedRenderTarget;getBakedModel()" +
            "Lnet/minecraft/client/renderer/block/model/IBakedModel;"), method = "renderBlockDamage")
    private IBakedModel dimhoppertweaks$onGetBakedModel(BaseBakedRenderTarget target) {
        Constants.LOGGER.error("GETTING BAKED MODEL FOR TARGET OF CLASS {}",target.getClass().getName());
        return target.getBakedModel();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockModelRenderer;renderModel(" +
            "Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;" +
            "Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;" +
            "Lnet/minecraft/client/renderer/BufferBuilder;Z)Z"), method = "renderBlockDamage")
    private boolean dimhoppertweaks$onRenderBlockDamage(BlockModelRenderer renderer, IBlockAccess block, IBakedModel model,
                                                        IBlockState state, BlockPos pos, BufferBuilder buffer, boolean checkSides) {
        Constants.LOGGER.error("TESTING SGCRAFT MODEL RENDER OF CLASS {}",renderer.getClass().getName());
        Constants.LOGGER.error("BAKED MODEL IS OF CLASS {}",model.getClass().getName());
        renderer.renderModel(block,model,state,pos,buffer,checkSides);
        return false;
    }
}
