package mods.thecomputerizer.dimensionhoppertweaks.client.gui;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.network.PacketHandler;
import mods.thecomputerizer.dimensionhoppertweaks.network.packets.PacketSyncGuiData;
import mods.thecomputerizer.dimensionhoppertweaks.util.ItemUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TokenExchangeGui extends GuiScreen {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(DimensionHopperTweaks.MODID,"textures/gui/background.png");

    private final List<String> skills;
    private DropDownMenu skillMenu;
    private ScrollableInteger levelScroll;
    private String currentSkill;
    private int conversionRate;

    public TokenExchangeGui(List<String> skills, String currentSkill, int conversionRate) {
        this.skills = skills;
        this.currentSkill = currentSkill;
        this.conversionRate = conversionRate;
    }

    private String getSkillTranslation(String skill) {
        return ItemUtil.getTranslationForType("skill",skill);
    }

    private List<String> getListTranslation() {
        return this.skills.stream().map(this::getSkillTranslation).collect(Collectors.toList());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawBackgroundOverlay();
    }

    @Override
    public void initGui() {
        this.addDropDownSkillMenu();
        this.addScrollableInteger();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.conversionRate = this.levelScroll.handleScroll(this.conversionRate);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if(this.skillMenu.checkForMenuUnderMouse(mouseX, mouseY))
            this.currentSkill = this.skillMenu.updateCurrentElement(this.currentSkill);
        else super.mouseReleased(mouseX, mouseY, state);
    }

    private void addDropDownSkillMenu() {
        this.skillMenu = new DropDownMenu(0,(this.width/2)-10,(this.height/2)-5,20,10,
                getSkillTranslation(this.currentSkill),this.skills,getListTranslation());
        this.buttonList.add(this.skillMenu);
    }

    private void addScrollableInteger() {
        this.levelScroll = new ScrollableInteger(1,(this.width/2)+10,(this.height/2)-5,10,10,
                ""+this.conversionRate,this.conversionRate);
        this.buttonList.add(this.levelScroll);
    }

    @Override
    public void onGuiClosed() {
        PacketHandler.NETWORK.sendToServer(new PacketSyncGuiData.PacketSyncGuiDataMessage(this.currentSkill,
                this.conversionRate,mc.player.getUniqueID()));
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
