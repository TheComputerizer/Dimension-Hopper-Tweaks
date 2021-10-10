package mods.thecomputerizer.dimensionhoppertweaks.client;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.TransformUtils;
import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityFinalBoss;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

@Mod.EventBusSubscriber(modid = DimensionHopperTweaks.MODID, value = { Side.CLIENT })
public class ClientEvents {
    public static IBakedModel forcefieldModel = null;

    @SubscribeEvent
    public static void renderLivingEvent(RenderLivingEvent.Post event) {
        if (event.getEntity() instanceof EntityFinalBoss) {
            EntityLivingBase e = event.getEntity();
            if (EntityFinalBoss.allowForcefield) {
                if (forcefieldModel == null) {
                    try {
                        forcefieldModel = OBJLoader.INSTANCE.loadModel(new ModelResourceLocation("dimensionhoppertweaks:models/forcefield.obj")).bake(TransformUtils.DEFAULT_BLOCK, DefaultVertexFormats.BLOCK, TextureUtils.bakedTextureGetter);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                }

                GlStateManager.pushMatrix();
                GlStateManager.depthMask(false);
                GlStateManager.disableCull();
                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.disableLighting();

                EntityPlayer viewingPlayer = Minecraft.getMinecraft().player;

                double translationXLT = e.prevPosX - viewingPlayer.prevPosX;
                double translationYLT = e.prevPosY - viewingPlayer.prevPosY;
                double translationZLT = e.prevPosZ - viewingPlayer.prevPosZ;

                double translationX = translationXLT + (((e.posX - viewingPlayer.posX) - translationXLT) * event.getPartialRenderTick());
                double translationY = translationYLT + (((e.posY - viewingPlayer.posY) - translationYLT) * event.getPartialRenderTick());
                double translationZ = translationZLT + (((e.posZ - viewingPlayer.posZ) - translationZLT) * event.getPartialRenderTick());

                GlStateManager.translate(translationX, translationY + 1.1, translationZ);

                GlStateManager.bindTexture(Minecraft.getMinecraft().getTextureMapBlocks().getGlTextureId());

                GlStateManager.scale(8.0, 8.0, 8.0);

                renderOBJ(forcefieldModel.getQuads(null, null, 0), new ColourRGBA(0F, 0.5F, 0.9F, 0.1F).argb());

                GlStateManager.enableCull();
                GlStateManager.enableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.enableLighting();
                GlStateManager.depthMask(true);
                GlStateManager.popMatrix();
            }
            if (EntityFinalBoss.renderSmallAttackArea) {
                if (forcefieldModel == null) {
                    try {
                        forcefieldModel = OBJLoader.INSTANCE.loadModel(new ModelResourceLocation("dimensionhoppertweaks:models/forcefield.obj")).bake(TransformUtils.DEFAULT_BLOCK, DefaultVertexFormats.BLOCK, TextureUtils.bakedTextureGetter);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                }

                GlStateManager.pushMatrix();
                GlStateManager.depthMask(false);
                GlStateManager.disableCull();
                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.disableLighting();

                EntityPlayer viewingPlayer = Minecraft.getMinecraft().player;

                double translationXLT = EntityFinalBoss.delayedAreaAttackSmallRenderx - viewingPlayer.prevPosX;
                double translationYLT = EntityFinalBoss.delayedAreaAttackSmallRendery - viewingPlayer.prevPosY;
                double translationZLT = EntityFinalBoss.delayedAreaAttackSmallRenderz - viewingPlayer.prevPosZ;

                double translationX = translationXLT + (((EntityFinalBoss.delayedAreaAttackSmallRenderx - viewingPlayer.posX) - translationXLT) * event.getPartialRenderTick());
                double translationY = translationYLT + (((EntityFinalBoss.delayedAreaAttackSmallRendery - viewingPlayer.posY) - translationYLT) * event.getPartialRenderTick());
                double translationZ = translationZLT + (((EntityFinalBoss.delayedAreaAttackSmallRenderz  - viewingPlayer.posZ) - translationZLT) * event.getPartialRenderTick());

                GlStateManager.translate(translationX, translationY + 1.1, translationZ);

                GlStateManager.bindTexture(Minecraft.getMinecraft().getTextureMapBlocks().getGlTextureId());

                GlStateManager.scale(8.0, 8.0, 8.0);

                renderOBJ(forcefieldModel.getQuads(null, null, 0), new ColourRGBA(0F, 0.5F, 0.9F, 0.1F).argb());

                GlStateManager.enableCull();
                GlStateManager.enableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.enableLighting();
                GlStateManager.depthMask(true);
                GlStateManager.popMatrix();
            }
        }
    }

    public static void renderOBJ(List<BakedQuad> listQuads, int ARGB) {
        Tessellator t = Tessellator.getInstance();
        BufferBuilder v = t.getBuffer();
        int i = 0;
        for (int j = listQuads.size(); i < j; ++i) {
            BakedQuad b = listQuads.get(i);
            v.begin(7, DefaultVertexFormats.ITEM);
            v.addVertexData(b.getVertexData());

            v.putColor4(ARGB);

            Vec3i vec3i = b.getFace().getDirectionVec();
            v.putNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
            t.draw();
        }
    }
}
