package mods.thecomputerizer.dimhoppertweaks.client.render;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.TransformUtils;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.client.ClientRegistryHandler;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.HomingProjectile;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class RenderHomingProjectile extends Render<HomingProjectile> {

    private final static ResourceLocation ATTACK = new ResourceLocation(DHTRef.MODID,"textures/models/attack.png");

    public RenderHomingProjectile(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(HomingProjectile entity, double x, double y, double z, float entityYaw, float partialTicks) {
        OBJModel.OBJBakedModel bakedForceField = (OBJModel.OBJBakedModel) ClientRegistryHandler.FORCEFIELD_MODEL.bake(TransformUtils.DEFAULT_BLOCK, DefaultVertexFormats.BLOCK, TextureUtils.bakedTextureGetter);
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

        GlStateManager.translate(translationX, translationY, translationZ);
        this.bindEntityTexture(entity);
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.color(1F, 1F, 1F, 0.75F);
        renderOBJ(bakedForceField.getQuads(null, null, 0), new ColourRGBA(1f,1f,1f,0.75f).argb());

        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.color(1F, 1F, 1F, 1);
        GlStateManager.popMatrix();
    }

    private void renderOBJ(List<BakedQuad> listQuads, int ARGB) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        int i = 0;
        for (int j = listQuads.size(); i < j; ++i) {
            BakedQuad quad = listQuads.get(i);
            buffer.begin(7, DefaultVertexFormats.ITEM);
            buffer.addVertexData(quad.getVertexData());
            buffer.putColor4(ARGB);
            Vec3i vec3i = quad.getFace().getDirectionVec();
            buffer.putNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
            tessellator.draw();
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull HomingProjectile entity) {
        return ATTACK;
    }
}
