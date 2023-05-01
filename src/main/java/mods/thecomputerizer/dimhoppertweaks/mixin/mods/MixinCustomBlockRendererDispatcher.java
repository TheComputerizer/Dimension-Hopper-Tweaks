package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import gcewing.sg.BaseAORenderingManager;
import gcewing.sg.BaseBakedRenderTarget;
import gcewing.sg.BaseModClient;
import gcewing.sg.Trans3;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.BaseRenderingManagerAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;
import java.util.Objects;

@Mixin(targets = "gcewing.sg.BaseAORenderingManager$CustomBlockRendererDispatcher")
public abstract class MixinCustomBlockRendererDispatcher extends BlockRendererDispatcher {
    @Shadow protected BlockRendererDispatcher base;

    @Shadow @Final private BaseAORenderingManager this$0;

    public MixinCustomBlockRendererDispatcher(BlockModelShapes shapes, BlockColors colors) {
        super(shapes, colors);
    }

    @Override
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
        this.base.renderBlockDamage(state, pos, icon, world);
    }
}
