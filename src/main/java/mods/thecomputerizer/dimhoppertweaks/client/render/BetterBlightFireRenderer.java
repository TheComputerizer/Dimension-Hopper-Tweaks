package mods.thecomputerizer.dimhoppertweaks.client.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.lib.event.ClientTicks;
import net.silentchaos512.scalinghealth.ScalingHealth;
import net.silentchaos512.scalinghealth.lib.module.ModuleAprilTricks;
import org.lwjgl.util.Color;

import java.util.Objects;

public class BetterBlightFireRenderer {

    public static final ResourceLocation TEXTURE = new ResourceLocation(
            ScalingHealth.MOD_ID_LOWER,"textures/entity/blightfire.png");
    public static final ResourceLocation TEXTURE_GRAY = new ResourceLocation(
            ScalingHealth.MOD_ID_LOWER,"textures/entity/blightfire_gray.png");

    @SuppressWarnings("SuspiciousNameCombination")
    public static void render(RenderManager manager, EntityLivingBase parent, double x, double y, double z) {
        if(Objects.nonNull(parent)) {
            boolean tomfoolery = ModuleAprilTricks.instance.isEnabled() && ModuleAprilTricks.instance.isRightDay();
            AxisAlignedBB box = parent.getEntityBoundingBox();
            float width = averageBoxWidth(box);
            double height = Math.abs(box.maxY-box.minY);
            float adjustedHeight = (float)MathHelper.clamp(height,width*0.75d,width*1.25d);
            Color color = new Color(255,255,255);
            if (tomfoolery) {
                float entityID = parent.getEntityId();
                float hueOffset = 40f+entityID%80f;
                float hue = (ClientTicks.ticksInGame+entityID)%hueOffset/hueOffset;
                color.fromHSB(hue, 1f,1f);
            }
            int frame = ClientTicks.ticksInGame % 64;
            boolean isOffset = frame>31;
            if(isOffset) frame-=32;
            double minU = isOffset ? 0.5f : 0;
            double minV = (float)frame/32f;
            double maxV = (float)(frame+1)/32f;
            GlStateManager.disableLighting();
            GlStateManager.pushMatrix();
            GlStateManager.translate(x,y+height*1.25d,z);
            GlStateManager.scale(width,width,width);
            GlStateManager.rotate(-manager.playerViewY,0,1f,0);
            GlStateManager.translate(0,0,(float)((int)(adjustedHeight/width))*0.02f);
            setColor(color.getRed(),color.getGreen(),color.getBlue());
            manager.renderEngine.bindTexture(tomfoolery ? TEXTURE_GRAY : TEXTURE);
            draw(-width,width,-adjustedHeight,adjustedHeight,minU,minU+0.5f,minV,maxV);
            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
        }
    }

    private static void draw(double xMin, double xMax, double yMin, double yMax, double minU, double maxU, double minV, double maxV) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(xMax,yMin,0d).tex(maxU,maxV).endVertex();
        buffer.pos(xMin,yMin,0d).tex(minU,maxV).endVertex();
        buffer.pos(xMin,yMax,0d).tex(minU,minV).endVertex();
        buffer.pos(xMax,yMax,0d).tex(maxU,minV).endVertex();
        tessellator.draw();
    }

    private static void setColor(float r, float g, float b) {
        GlStateManager.color(r/255f,g/255f, b/255f,1f);
    }

    private static float averageBoxWidth(AxisAlignedBB box) {
        return (float)((Math.abs(box.maxX-box.minX)+Math.abs(box.maxZ-box.minZ))/2d);
    }
}
