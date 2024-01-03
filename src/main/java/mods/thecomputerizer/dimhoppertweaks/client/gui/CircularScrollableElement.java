package mods.thecomputerizer.dimhoppertweaks.client.gui;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

public abstract class CircularScrollableElement extends Gui {

    private final TokenExchangeGui parentScreen;
    protected final int centerX;
    protected final int centerY;
    protected final int radius;
    protected final int resolution;
    protected String displayString;
    protected boolean hover;
    protected String hoverText;

    public CircularScrollableElement(TokenExchangeGui parent, int centerX, int centerY, int radius, int resolution,
                                     String displayString, String hoverKey) {
        this.parentScreen = parent;
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.resolution = resolution;
        this.displayString = displayString;
        this.hover = false;
        this.hoverText = hoverKey;
    }

    protected TokenExchangeGui getParentScreen() {
        return this.parentScreen;
    }

    public abstract void handleScroll();

    public void setCenterString(String displayString) {
        this.displayString = displayString;
    }

    private boolean isWithinRadius(int mouseX, int mouseY) {
        if(!(Math.abs(this.centerX-mouseX)<=this.radius)) return false;
        return Math.abs(this.centerY - mouseY) <= this.radius;
    }

    protected boolean getHover() {
        return this.hover;
    }

    public void render(Minecraft mc, int mouseX, int mouseY, int r, int g, int b, int a) {
        this.hover = isWithinRadius(mouseX, mouseY);
        if(this.hover) {
            a = 255;
            g = 255;
            b = 255;
        }
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
        for(int i=0;i<this.resolution;i++) {
            float angle1 = startAngle+(i/(float)this.resolution)*angle;
            float angle2 = startAngle+((i+1)/(float)this.resolution)*angle;
            float xOut = this.centerX+this.radius*(float)Math.cos(angle1);
            float yOut = this.centerY+this.radius*(float)Math.sin(angle1);
            float xOut2 = this.centerX+this.radius*(float)Math.cos(angle2);
            float yOut2 = this.centerY+this.radius*(float)Math.sin(angle2);
            buffer.pos(xOut, yOut, this.zLevel).color(r, g, b, a).endVertex();
            buffer.pos(this.centerX, this.centerY, this.zLevel).color(r, g, b, a).endVertex();
            buffer.pos(this.centerX, this.centerY, this.zLevel).color(r, g, b, a).endVertex();
            buffer.pos(xOut2, yOut2, this.zLevel).color(r, g, b, a).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
        int color = this.hover ? 16777120 : 14737632;
        this.drawCenteredString(mc.fontRenderer,this.displayString,this.centerX,this.centerY,color);
        if(this.hover && Objects.nonNull(this.hoverText))
            parentScreen.drawHoveringText(TextUtil.getTranslated("gui."+ DHTRef.MODID+"."+this.hoverText),mouseX,mouseY);
    }
}
