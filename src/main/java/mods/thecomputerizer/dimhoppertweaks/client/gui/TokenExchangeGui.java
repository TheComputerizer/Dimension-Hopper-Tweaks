package mods.thecomputerizer.dimhoppertweaks.client.gui;

import lombok.Setter;
import mods.thecomputerizer.dimhoppertweaks.network.DHTNetwork;
import mods.thecomputerizer.dimhoppertweaks.network.PacketSyncGuiData;
import mods.thecomputerizer.dimhoppertweaks.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.MODID;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR;
import static org.lwjgl.opengl.GL11.GL_QUADS;

@SuppressWarnings("NullableProblems")
public class TokenExchangeGui extends GuiScreen {
    
    private final List<String> skills;
    private final List<CircularScrollableElement> scrollables;
    @Setter private String currentSkill;
    @Setter private int conversionRate;

    public TokenExchangeGui(List<String> skills, String currentSkill, int conversionRate) {
        this.skills = skills;
        this.currentSkill = currentSkill;
        this.conversionRate = conversionRate;
        this.scrollables = Collections.synchronizedList(new ArrayList<>());
    }

    @Override public void setWorldAndResolution(Minecraft mc, int width, int height) {
        super.setWorldAndResolution(mc, width, height);
        synchronized (this.scrollables) {
            this.scrollables.clear();
            this.scrollables.add(new ScrollableInteger(this,3*(this.width/4),this.height/4,50,72,
                    this.conversionRate, String.valueOf(this.conversionRate),"level"));
            this.scrollables.add(new ScrollableList(this,this.width/4,this.height/4,50,72,
                    getSkillTranslation(this.currentSkill),this.currentSkill,this.skills,getListTranslation(),"current_skill"));
        }
    }
    
    private String getSkillTranslation(String skill) {
        return TextUtil.getTranslated("skill."+MODID+"."+skill);
    }

    private List<String> getListTranslation() {
        return this.skills.stream().map(this::getSkillTranslation).collect(Collectors.toList());
    }

    @Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(Objects.isNull(this.mc)) this.mc = Minecraft.getMinecraft();
        if(Objects.isNull(this.fontRenderer)) this.fontRenderer = this.mc.fontRenderer;
        try {
            this.drawDefaultBackground();
            synchronized(this.scrollables) {
                for (CircularScrollableElement circle : this.scrollables)
                    circle.render(Minecraft.getMinecraft(),mouseX,mouseY,0,0,0,64);
            }
            this.drawCenteredString(this.fontRenderer,TextUtil.getTranslated("gui."+MODID+"."+"skill_drain"),
                    this.width/2,8,10526880);
            this.renderSmallCircleOnCursor(Minecraft.getMinecraft(),mouseX,mouseY);
            super.drawScreen(mouseX, mouseY, partialTicks);
        } catch(NullPointerException ignored) {
            this.currentSkill = Objects.nonNull(this.currentSkill) && this.currentSkill.length()>1 ? this.currentSkill : "mining";
            Minecraft.getMinecraft().displayGuiScreen(null);
            Minecraft.getMinecraft().setIngameFocus();
        }
    }

    @Override public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        synchronized(this.scrollables) {
            for(CircularScrollableElement circle : this.scrollables) circle.handleScroll();
        }
    }

    @Override public void onGuiClosed() {
        DHTNetwork.sendToServer(new PacketSyncGuiData(this.currentSkill,this.conversionRate));
    }

    private void renderSmallCircleOnCursor(Minecraft ignored, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.color(1f,1f,1f,1f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        float startAngle = (float) Math.toRadians(180);
        float endAngle = (float) Math.toRadians(540);
        float angle = endAngle-startAngle;
        buffer.begin(GL_QUADS,POSITION_COLOR);
        for(int i=0;i<72;i++) {
            float angle1 = startAngle+(i/72f)*angle;
            float angle2 = startAngle+((i+1)/72f)*angle;
            float xOut = mouseX+5*(float)Math.cos(angle1);
            float yOut = mouseY+5*(float)Math.sin(angle1);
            float xOut2 = mouseX+5*(float)Math.cos(angle2);
            float yOut2 = mouseY+5*(float)Math.sin(angle2);
            buffer.pos(xOut,yOut, this.zLevel).color(255,255,255,255).endVertex();
            buffer.pos(mouseX,mouseY,this.zLevel).color(255,255,255,255).endVertex();
            buffer.pos(mouseX,mouseY,this.zLevel).color(255,255,255,255).endVertex();
            buffer.pos(xOut2,yOut2,this.zLevel).color(255,255,255,255).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    @Override public boolean doesGuiPauseGame() {
        return false;
    }
}