package mods.thecomputerizer.dimensionhoppertweaks.client.entity.render;

import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityWhiteNova;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWhiteNova extends Render<EntityWhiteNova>
{
    public RenderWhiteNova(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void doRender(EntityWhiteNova entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        bindEntityTexture(entity);
        GlStateManager.translate(x, y, z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(1.0,1.0,1.0);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.rotate(180.0F - renderManager.playerViewY,
                0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

        if (renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        bufferbuilder.pos(-0.5D, -0.25D, 0.0D)
                .tex(0.0, 1.0)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        bufferbuilder.pos(0.5D, -0.25D, 0.0D)
                .tex(1.0, 1.0)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        bufferbuilder.pos(0.5D, 0.75D, 0.0D)
                .tex(1.0, 0.0)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        bufferbuilder.pos(-0.5D, 0.75D, 0.0D)
                .tex(0.0, 0.0)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        tessellator.draw();

        if(renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityWhiteNova entity)
    {
        return new ResourceLocation("textures/entity/whitenova");
    }
}