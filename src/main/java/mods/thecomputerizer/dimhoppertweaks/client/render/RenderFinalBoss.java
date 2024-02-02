package mods.thecomputerizer.dimhoppertweaks.client.render;

import codechicken.lib.colour.ColourRGBA;
import mods.thecomputerizer.dimhoppertweaks.client.DHTClient;
import mods.thecomputerizer.dimhoppertweaks.client.model.ModelFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

@SuppressWarnings("DataFlowIssue")
public class RenderFinalBoss extends GeoEntityRenderer<EntityFinalBoss> {

    public RenderFinalBoss(RenderManager renderManager) {
        super(renderManager, new ModelFinalBoss());
    }

    @Override
    public void doRender(@Nonnull EntityFinalBoss entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        OBJBakedModel bakedForceField = DHTClient.getBakedForcefield();
        if(entity.hasShield()) {
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            EntityPlayer viewingPlayer = Minecraft.getMinecraft().player;
            double translationXLT = entity.posX-viewingPlayer.prevPosX;
            double translationYLT = entity.posY-viewingPlayer.prevPosY;
            double translationZLT = entity.posZ-viewingPlayer.prevPosZ;
            double translationX = translationXLT+(((entity.posX-viewingPlayer.posX)-translationXLT)*partialTicks);
            double translationY = translationYLT+(((entity.posY-viewingPlayer.posY)-translationYLT)*partialTicks);
            double translationZ = translationZLT+(((entity.posZ-viewingPlayer.posZ)-translationZLT)*partialTicks);
            GlStateManager.translate(translationX,translationY+1.1d,translationZ);
            Minecraft.getMinecraft().getTextureManager().bindTexture(DHTClient.FORCEFIELD);
            GlStateManager.scale(8f,8f,8f);
            GlStateManager.color(0f,0.5f,0.9f,0.1f);
            renderOBJ(bakedForceField.getQuads(null,null,0),new ColourRGBA(0f,0.5f,0.9f,0.1f).argb());
            GlStateManager.enableCull();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.color(1f,1f,1f,1);
            GlStateManager.popMatrix();
        }
        if(entity.isChargingProjectile()) {
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE,GlStateManager.DestFactor.ONE);
            EntityPlayer viewingPlayer = Minecraft.getMinecraft().player;
            Vec3d eyePos = entity.getPositionEyes(partialTicks);
            Vec3d lookVec = entity.getLook(partialTicks);
            Vec3d chargeVec = eyePos.add(lookVec.x*8.5,lookVec.y*8.5,lookVec.z*8.5);
            double translationXLT = chargeVec.x-viewingPlayer.prevPosX;
            double translationYLT = chargeVec.y-viewingPlayer.prevPosY;
            double translationZLT = chargeVec.z-viewingPlayer.prevPosZ;
            double translationX = translationXLT+(((chargeVec.x-viewingPlayer.posX)-translationXLT)*partialTicks);
            double translationY = translationYLT+(((chargeVec.y-viewingPlayer.posY)-translationYLT)*partialTicks);
            double translationZ = translationZLT+(((chargeVec.z-viewingPlayer.posZ)-translationZLT)*partialTicks);
            GlStateManager.translate(translationX, translationY, translationZ);
            Minecraft.getMinecraft().getTextureManager().bindTexture(DHTClient.FORCEFIELD);
            float alpha = entity.getProjectileChargePercent();
            GlStateManager.scale(0.5f*alpha, 0.5f*alpha, 0.5f*alpha);
            GlStateManager.color(1f,1f,1f, alpha);
            renderOBJ(bakedForceField.getQuads(null,null,0),new ColourRGBA(1f,1f,1f, alpha).argb());
            GlStateManager.enableCull();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.color(1f,1f,1f,1f);
            GlStateManager.popMatrix();
            Random rand = entity.world.rand;
            for (int i=0; i<Math.max(1,(int)(2f*entity.getProjectileChargePercent())); i++) {
                Vec3d spawned = new Vec3d(chargeVec.x+(rand.nextDouble()-0.5d),
                        chargeVec.y+(rand.nextDouble()-0.5d),
                        chargeVec.z+(rand.nextDouble()-0.5d));
                Vec3d toCenter = spawned.subtract(chargeVec).normalize();
                float velocity = entity.getProjectileChargePercent()*2f;
                viewingPlayer.world.spawnParticle(EnumParticleTypes.PORTAL,spawned.x,spawned.y,spawned.z,
                        toCenter.x*velocity,toCenter.y*velocity,toCenter.z*velocity);
            }
        }
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
}
