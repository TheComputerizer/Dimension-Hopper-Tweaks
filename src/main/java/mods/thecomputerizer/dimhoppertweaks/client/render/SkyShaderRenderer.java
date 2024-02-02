package mods.thecomputerizer.dimhoppertweaks.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IRenderHandler;

import java.util.function.Consumer;

public class SkyShaderRenderer extends IRenderHandler {

    private static final ResourceLocation END_SKY_TEXTURES = new ResourceLocation("textures/environment/end_sky.png");

    private final Consumer<Void> shaderUser;
    private final Consumer<Void> shaderReleaser;
    private int renderID = -1;
    private boolean cached = false;
    
    public SkyShaderRenderer(Consumer<Void> shaderUser, Consumer<Void> shaderReleaser) {
        this.shaderUser = shaderUser;
        this.shaderReleaser = shaderReleaser;
    }
    
    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        this.shaderUser.accept(null);
        renderSky(partialTicks,mc);
        this.shaderReleaser.accept(null);
    }
    
    private void renderSky(float partialTicks, Minecraft mc) {
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.depthMask(false);
        mc.getTextureManager().bindTexture(END_SKY_TEXTURES);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        for(int k1 = 0; k1 < 6; ++k1) {
            GlStateManager.pushMatrix();
            if(k1==1) GlStateManager.rotate(90f, 1f, 0f, 0f);
            if(k1==2) GlStateManager.rotate(-90f, 1f, 0f, 0f);
            if(k1==3) GlStateManager.rotate(180f, 1f, 0f, 0f);
            if(k1==4) GlStateManager.rotate(90f, 0f, 0f, 1f);
            if(k1==5) GlStateManager.rotate(-90f, 0f, 0f, 1f);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(-100d, -100d, -100d).tex(0d, 0d).color(40, 40, 40, 255).endVertex();
            bufferbuilder.pos(-100d, -100d, 100d).tex(0d, 16d).color(40, 40, 40, 255).endVertex();
            bufferbuilder.pos(100d, -100d, 100d).tex(16d, 16d).color(40, 40, 40, 255).endVertex();
            bufferbuilder.pos(100d, -100d, -100d).tex(16d, 0d).color(40, 40, 40, 255).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
    }
}