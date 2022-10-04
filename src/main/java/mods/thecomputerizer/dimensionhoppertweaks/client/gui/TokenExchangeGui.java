package mods.thecomputerizer.dimensionhoppertweaks.client.gui;

import mods.thecomputerizer.dimensionhoppertweaks.network.PacketHandler;
import mods.thecomputerizer.dimensionhoppertweaks.network.packets.PacketSyncGuiData;
import mods.thecomputerizer.dimensionhoppertweaks.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NullableProblems")
public class TokenExchangeGui extends GuiScreen {
    private final List<String> skills;
    private final List<CircularScrollableElement> scrollables;
    private String currentSkill;
    private int conversionRate;

    public TokenExchangeGui(List<String> skills, String currentSkill, int conversionRate) {
        this.skills = skills;
        this.currentSkill = currentSkill;
        this.conversionRate = conversionRate;
        this.scrollables = new ArrayList<>();
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        super.setWorldAndResolution(mc, width, height);
        this.scrollables.clear();
        this.scrollables.add(new ScrollableInteger(this,3*(this.width/4),this.height/4,50,72,
                this.conversionRate, ""+this.conversionRate, "level"));
        this.scrollables.add(new ScrollableList(this, this.width/4,this.height/4,50,72,
                getSkillTranslation(this.currentSkill), this.currentSkill, this.skills,getListTranslation(), "current_skill"));
    }

    public void setConversionRate(int level) {
        this.conversionRate = level;
    }

    public void setCurrentSkill(String skill) {
        this.currentSkill = skill;
    }

    private String getSkillTranslation(String skill) {
        return ItemUtil.getTranslationForType("skill",skill);
    }

    private List<String> getListTranslation() {
        return this.skills.stream().map(this::getSkillTranslation).collect(Collectors.toList());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(this.mc==null) this.mc = Minecraft.getMinecraft();
        this.drawDefaultBackground();
        for(CircularScrollableElement circle : this.scrollables)
            circle.render(Minecraft.getMinecraft(),mouseX,mouseY,0,0,0,64);
        this.drawCenteredString(this.fontRenderer, ItemUtil.getTranslationForType("gui","skill_drain"), this.width/2, 8, 10526880);
        this.renderSmallCircleOnCursor(Minecraft.getMinecraft(),mouseX,mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        for(CircularScrollableElement circle : this.scrollables) circle.handleScroll();
    }

    @Override
    public void onGuiClosed() {
        PacketHandler.NETWORK.sendToServer(new PacketSyncGuiData.PacketSyncGuiDataMessage(this.currentSkill,
                this.conversionRate,mc.player.getUniqueID()));
    }

    private void renderSmallCircleOnCursor(Minecraft ignored, int mouseX, int mouseY) {
        int r = 255;
        int g = 255;
        int b = 255;
        int a = 255;
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        float startAngle = (float) Math.toRadians(180);
        float endAngle = (float) Math.toRadians(540);
        float angle = endAngle - startAngle;
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i < 72; i++) {
            float angle1 = startAngle + (i / 72f) * angle;
            float angle2 = startAngle + ((i + 1) / 72f) * angle;
            float xOut = mouseX + 5 * (float) Math.cos(angle1);
            float yOut = mouseY + 5 * (float) Math.sin(angle1);
            float xOut2 = mouseX + 5 * (float) Math.cos(angle2);
            float yOut2 = mouseY + 5 * (float) Math.sin(angle2);
            buffer.pos(xOut, yOut, this.zLevel).color(r, g, b, a).endVertex();
            buffer.pos(mouseX, mouseY, this.zLevel).color(r, g, b, a).endVertex();
            buffer.pos(mouseX, mouseY, this.zLevel).color(r, g, b, a).endVertex();
            buffer.pos(xOut2, yOut2, this.zLevel).color(r, g, b, a).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}
