package mods.thecomputerizer.dimensionhoppertweaks.client.entity.render;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.TransformUtils;
import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityFinalBoss;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class RenderFinalBoss extends RenderBiped<EntityFinalBoss>
{
    public static final ResourceLocation TEXTURES = new ResourceLocation(DimensionHopperTweaks.MODID, "textures/entity/final_boss.png");
    public OBJModel forcefieldModel;

    public RenderFinalBoss(RenderManager manager) {
        super(manager, new ModelPlayer(0.0F, false), 0.0F);
        try {
            DimensionHopperTweaks.LOGGER.info("binding model to final boss render");
            this.forcefieldModel = (OBJModel) OBJLoader.INSTANCE.loadModel(new ModelResourceLocation("dimensionhoppertweaks:models/forcefield.obj"));
            DimensionHopperTweaks.LOGGER.info("bound model to final boss render");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityFinalBoss entity)
    {
        return TEXTURES;
    }

    @Override
    protected void applyRotations(EntityFinalBoss entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }

    @Override
    public void doRender(EntityFinalBoss entity, double x, double y, double z, float entityYaw, float partialTicks) {
        OBJModel.OBJBakedModel bakedForceField = (OBJModel.OBJBakedModel)forcefieldModel.bake(TransformUtils.DEFAULT_BLOCK, DefaultVertexFormats.BLOCK, TextureUtils.bakedTextureGetter);
        if(entity.allowForcefield) {
            DimensionHopperTweaks.LOGGER.info("rendering forcefield "+bakedForceField);
            GlStateManager.pushMatrix();
            GlStateManager.depthMask(false);
            GlStateManager.disableCull();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();

            EntityPlayer viewingPlayer = Minecraft.getMinecraft().player;

            double translationXLT = entity.prevPosX - viewingPlayer.prevPosX;
            double translationYLT = entity.prevPosY - viewingPlayer.prevPosY;
            double translationZLT = entity.prevPosZ - viewingPlayer.prevPosZ;

            double translationX = translationXLT + (((entity.posX - viewingPlayer.posX) - translationXLT) * partialTicks);
            double translationY = translationYLT + (((entity.posY - viewingPlayer.posY) - translationYLT) * partialTicks);
            double translationZ = translationZLT + (((entity.posZ - viewingPlayer.posZ) - translationZLT) * partialTicks);

            GlStateManager.translate(translationX, translationY + 1.1, translationZ);

            GlStateManager.bindTexture(Minecraft.getMinecraft().getTextureMapBlocks().getGlTextureId());

            GlStateManager.scale(8.0, 8.0, 8.0);

            renderOBJ(bakedForceField.getQuads(null, null, 0), new ColourRGBA(0F, 0.5F, 0.9F, 0.9F).argb());

            GlStateManager.enableCull();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.popMatrix();
        }
        if (entity.renderSmallAttackArea) {
            GlStateManager.pushMatrix();
            GlStateManager.depthMask(false);
            GlStateManager.disableCull();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();

            EntityPlayer viewingPlayer = Minecraft.getMinecraft().player;

            double translationXLT = entity.delayedAreaAttackSmallRenderx;
            double translationYLT = entity.delayedAreaAttackSmallRendery;
            double translationZLT = entity.delayedAreaAttackSmallRenderz;

            double translationX = translationXLT + (((entity.delayedAreaAttackSmallRenderx - viewingPlayer.posX) - translationXLT) * partialTicks);
            double translationY = translationYLT + (((entity.delayedAreaAttackSmallRendery - viewingPlayer.posY) - translationYLT) * partialTicks);
            double translationZ = translationZLT + (((entity.delayedAreaAttackSmallRenderz  - viewingPlayer.posZ) - translationZLT) * partialTicks);

            GlStateManager.translate(translationX, translationY + 1.1, translationZ);

            GlStateManager.bindTexture(Minecraft.getMinecraft().getTextureMapBlocks().getGlTextureId());

            GlStateManager.scale(8.0, 8.0, 8.0);

            renderOBJ(bakedForceField.getQuads(null, null, 0), new ColourRGBA(0F, 0.5F, 0.9F, 0.1F).argb());

            GlStateManager.enableCull();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.popMatrix();
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    private void renderOBJ(List<BakedQuad> listQuads, int ARGB) {
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
