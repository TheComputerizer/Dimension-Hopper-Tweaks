package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import gcewing.sg.BaseAORenderingManager;
import gcewing.sg.BaseBakedRenderTarget;
import gcewing.sg.BaseModClient;
import gcewing.sg.Trans3;
import mods.thecomputerizer.dimhoppertweaks.Constants;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.BaseRenderingManagerAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;
import java.util.Objects;

@Mixin(targets = "gcewing.sg.BaseAORenderingManager$CustomBlockRendererDispatcher")
public abstract class MixinCustomBlockRendererDispatcher {
    @Shadow protected BlockRendererDispatcher base;

    @Shadow @Final private BaseAORenderingManager this$0;

    @Shadow public abstract BlockModelRenderer getBlockModelRenderer();

    /**
     * @author The_Computerizer
     * @reason Crash when trying to mine stargate ring blocks
     */
    @Overwrite
    public void renderBlockDamage(@Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull TextureAtlasSprite icon, @Nonnull IBlockAccess world) {
        try {
            BaseModClient.ICustomRenderer rend = ((BaseRenderingManagerAccess)this$0).accessGetCustomRenderer(world, pos, state);
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
            Constants.LOGGER.error("Stargate Damage Render Catch");
            e.printStackTrace();
        }
    }
}
