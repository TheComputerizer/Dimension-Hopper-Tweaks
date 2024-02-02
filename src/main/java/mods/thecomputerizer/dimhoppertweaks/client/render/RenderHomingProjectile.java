package mods.thecomputerizer.dimhoppertweaks.client.render;

import codechicken.lib.colour.ColourRGBA;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.client.DHTClient;
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
import org.lwjgl.opengl.GL11;

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
        renderOBJ(DHTClient.getBakedForcefield().getQuads(null, null, 0), new ColourRGBA(1f,1f,1f,0.75f).argb());
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
        for(BakedQuad quad : listQuads) {
            buffer.begin(GL11.GL_QUADS,DefaultVertexFormats.ITEM);
            buffer.addVertexData(quad.getVertexData());
            buffer.putColor4(ARGB);
            Vec3i vec3i = quad.getFace().getDirectionVec();
            buffer.putNormal((float)vec3i.getX(),(float)vec3i.getY(),(float)vec3i.getZ());
            tessellator.draw();
        }
    }

    @Override
    protected @Nullable ResourceLocation getEntityTexture(@Nonnull HomingProjectile entity) {
        return ATTACK;
    }
}
