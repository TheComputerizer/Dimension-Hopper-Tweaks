package mods.thecomputerizer.dimensionhoppertweaks.client.gui;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class TokenExchangeGui extends GuiScreen {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(DimensionHopperTweaks.MODID,"textures/gui/background.png");

    private final List<String> skills;
    private String currentSkill;
    private int conversionRate;

    public TokenExchangeGui(List<String> skills, String currentSkill, int conversionRate) {
        this.skills = skills;
        this.currentSkill = currentSkill;
        this.conversionRate = conversionRate;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawBackgroundOverlay();
    }

    @Override
    public void initGui() {

    }

    protected void drawBackgroundOverlay() {
        int alpha = 255;
        int startX = this.width/4;
        int endX = startX*3;
        int startY = this.height/4;
        int endY = startY*3;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        this.mc.getTextureManager().bindTexture(OPTIONS_BACKGROUND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(startX, endY, 0d).tex(0d, 0d).color(64, 64, 64, alpha).endVertex();
        bufferbuilder.pos(endX, endY, 0d).tex(0d, 0d).color(64, 64, 64, alpha).endVertex();
        bufferbuilder.pos(endX, startY, 0d).tex(0d, 0d).color(64, 64, 64, alpha).endVertex();
        bufferbuilder.pos(startX, startY, 0d).tex(0d, 0d).color(64, 64, 64, alpha).endVertex();
        tessellator.draw();
    }
}
