package mods.thecomputerizer.dimensionhoppertweaks.client.gui;

import mods.thecomputerizer.dimensionhoppertweaks.network.PacketHandler;
import mods.thecomputerizer.dimensionhoppertweaks.network.packets.PacketSyncGuiData;
import mods.thecomputerizer.dimensionhoppertweaks.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

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
}
