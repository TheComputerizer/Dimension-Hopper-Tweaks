package mods.thecomputerizer.dimhoppertweaks.client.entity.render;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.TransformUtils;
import mods.thecomputerizer.dimhoppertweaks.DimHopperTweaks;
import mods.thecomputerizer.dimhoppertweaks.client.ClientHandler;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.HomingProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.obj.OBJModel;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class RenderHomingProjectile extends Render<HomingProjectile> {

    private final static ResourceLocation ATTACK = new ResourceLocation(DimHopperTweaks.MODID,"textures/models/attack.png");

    public RenderHomingProjectile(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(HomingProjectile entity, double x, double y, double z, float entityYaw, float partialTicks) {
        OBJModel.OBJBakedModel bakedForceField = (OBJModel.OBJBakedModel) ClientHandler.forcefieldModel.bake(TransformUtils.DEFAULT_BLOCK, DefaultVertexFormats.BLOCK, TextureUtils.bakedTextureGetter);
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.pushMatrix();

        EntityPlayer viewingPlayer = Minecraft.getMinecraft().player;

        double translationXLT = entity.prevPosX - viewingPlayer.prevPosX;
        double translationYLT = entity.prevPosY - viewingPlayer.prevPosY;
        double translationZLT = entity.prevPosZ - viewingPlayer.prevPosZ;

        double translationX = translationXLT + (((entity.posX - viewingPlayer.posX) - translationXLT) * partialTicks);
        double translationY = translationYLT + (((entity.posY - viewingPlayer.posY) - translationYLT) * partialTicks);
        double translationZ = translationZLT + (((entity.posZ - viewingPlayer.posZ) - translationZLT) * partialTicks);

        GlStateManager.translate(translationX, translationY + 1.1, translationZ);
        this.bindEntityTexture(entity);
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.color(1F, 1F, 1F, 0.25F);
        renderOBJ(bakedForceField.getQuads(null, null, 0), new ColourRGBA(0.9F, 0.5F, 0.1F, 0.1F).argb());

        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.color(1F, 1F, 1F, 1);
        GlStateManager.popMatrix();
    }

    private static void renderOBJ(List<BakedQuad> listQuads, int ARGB) {
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

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(HomingProjectile entity) {
        return ATTACK;
    }
}
