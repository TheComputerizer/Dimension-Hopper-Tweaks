package mods.thecomputerizer.dimhoppertweaks.client.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.lib.event.ClientTicks;
import net.silentchaos512.scalinghealth.ScalingHealth;
import net.silentchaos512.scalinghealth.lib.module.ModuleAprilTricks;
import org.lwjgl.util.Color;

import javax.annotation.Nonnull;
import java.util.Objects;

public class BetterBlightFireRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ScalingHealth.MOD_ID_LOWER,"textures/entity/blightfire.png");
    private static final ResourceLocation TEXTURE_GRAY = new ResourceLocation(ScalingHealth.MOD_ID_LOWER,"textures/entity/blightfire_gray.png");

    private static @Nonnull ResourceLocation getFireTexture() {
        return ModuleAprilTricks.instance.isRightDay() && ModuleAprilTricks.instance.isEnabled() ? TEXTURE_GRAY : TEXTURE;
    }

    public static void render(RenderManager manager, EntityLivingBase parent, double x, double y, double z) {
        if(Objects.nonNull(parent)) {
            GlStateManager.disableLighting();
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y-(double)parent.height+0.5d, z);
            float f = parent.width*0.5f;
            GlStateManager.scale(f,f,f);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder vertexbuffer = tessellator.getBuffer();
            float f1 = 0.5f;
            float f2 = 0;
            float f3 = parent.height/f;
            float f4 = (float)(parent.posY-parent.getEntityBoundingBox().maxY);
            GlStateManager.rotate(-manager.playerViewY,0,1f,0);
            GlStateManager.translate(0,0,(float)((int)f3)*0.02f);
            float f5;
            if (ModuleAprilTricks.instance.isRightDay() && ModuleAprilTricks.instance.isEnabled()) {
                f5 = 40f+(float)parent.getEntityId()%80f;
                Color color = new Color();
                float hue = (float)(ClientTicks.ticksInGame+parent.getEntityId())%f5/f5;
                color.fromHSB(hue, 1f,1f);
                GlStateManager.color((float)color.getRed()/255f,(float)color.getGreen()/255f,
                        (float)color.getBlue()/255f,1f);
            } else GlStateManager.color(1f,1f,1f,1f);
            f5 = 0;
            int i = 0;
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            manager.renderEngine.bindTexture(getFireTexture());
            while(f3 > 0) {
                boolean flag = i % 2 == 0;
                int frame = ClientTicks.ticksInGame % 32;
                float minU = flag ? 0.5f : 0;
                float minV = (float)frame/32f;
                float maxU = flag ? 1f : 0.5f;
                float maxV = (float)(frame+1)/32f;
                if (flag) {
                    float f10 = maxU;
                    maxU = minU;
                    minU = f10;
                }
                vertexbuffer.pos(f1-f2,-f4,f5).tex(maxU,maxV).endVertex();
                vertexbuffer.pos(-f1-f2,-f4,f5).tex(minU,maxV).endVertex();
                vertexbuffer.pos(-f1-f2,1.4f-f4,f5).tex(minU,minV).endVertex();
                vertexbuffer.pos(f1-f2,1.4f-f4,f5).tex(maxU,minV).endVertex();
                f3 -= 0.45f;
                f4 -= 0.45f;
                f1 *= 0.9f;
                f5 += 0.03f;
                ++i;
            }
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
        }
    }
}
