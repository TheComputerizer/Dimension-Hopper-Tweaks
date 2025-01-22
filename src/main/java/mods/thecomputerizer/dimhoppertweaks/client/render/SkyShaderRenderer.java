package mods.thecomputerizer.dimhoppertweaks.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Consumer;

import static net.minecraft.client.renderer.GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA;
import static net.minecraft.client.renderer.GlStateManager.DestFactor.ZERO;
import static net.minecraft.client.renderer.GlStateManager.SourceFactor.ONE;
import static net.minecraft.client.renderer.GlStateManager.SourceFactor.SRC_ALPHA;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX_COLOR;
import static net.minecraftforge.fml.relauncher.Side.CLIENT;
import static org.lwjgl.opengl.GL11.GL_QUADS;

@SideOnly(CLIENT)
public class SkyShaderRenderer extends IRenderHandler {

    private static final ResourceLocation END_SKY_TEXTURES = new ResourceLocation("textures/environment/end_sky.png");

    private final Consumer<Float> shaderUser;
    private final Consumer<Float> shaderReleaser;
    private int renderID = -1;
    private boolean cached = false;
    
    public SkyShaderRenderer(Consumer<Float> shaderUser, Consumer<Float> shaderReleaser) {
        this.shaderUser = shaderUser;
        this.shaderReleaser = shaderReleaser;
    }
    
    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        this.shaderUser.accept(partialTicks);
        renderSky(partialTicks,mc);
        this.shaderReleaser.accept(partialTicks);
    }
    
    private void renderSky(float partialTicks, Minecraft mc) {
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(SRC_ALPHA,ONE_MINUS_SRC_ALPHA,ONE,ZERO);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.depthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        for(int k1 = 0; k1 < 6; ++k1) {
            GlStateManager.pushMatrix();
            if(k1==1) GlStateManager.rotate(90f,1f,0f,0f);
            else if(k1==2) GlStateManager.rotate(-90f,1f,0f,0f);
            else if(k1==3) GlStateManager.rotate(180f,1f,0f,0f);
            else if(k1==4) GlStateManager.rotate(90f,0f,0f,1f);
            else if(k1==5) GlStateManager.rotate(-90f,0f,0f,1f);
            buffer.begin(GL_QUADS,POSITION_TEX_COLOR);
            buffer.pos(-100d,-100d,-100d).tex(0d,0d).color(40,40,40,255).endVertex();
            buffer.pos(-100d,-100d,100d).tex(0d,16d).color(40,40,40,255).endVertex();
            buffer.pos(100d,-100d,100d).tex(16d,16d).color(40,40,40,255).endVertex();
            buffer.pos(100d,-100d,-100d).tex(16d,0d).color(40,40,40,255).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
    }
}