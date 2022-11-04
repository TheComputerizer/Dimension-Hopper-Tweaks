package mods.thecomputerizer.dimhoppertweaks.client.entity.render;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.TransformUtils;
import mods.thecomputerizer.dimhoppertweaks.DimHopperTweaks;
import mods.thecomputerizer.dimhoppertweaks.client.ClientHandler;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.EntityFinalBoss;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@SuppressWarnings("ConstantConditions")
@Mod.EventBusSubscriber(modid = DimHopperTweaks.MODID, value = Side.CLIENT)
public class RenderEvents {

    private final static ResourceLocation FORCEFIELD = new ResourceLocation(DimHopperTweaks.MODID,"textures/models/forcefield.png");
    private final static ResourceLocation ATTACK = new ResourceLocation(DimHopperTweaks.MODID,"textures/models/attack.png");

    private static final Random rand = new Random();
    public static HashMap<String, Boolean> bossShields = new HashMap<>();
    public static HashMap<BlockPos, Integer> attacks = new HashMap<>();
    public static HashMap<BlockPos, Integer> attackSize = new HashMap<>();

    @SubscribeEvent
    public static void renderEntity(RenderLivingEvent.Post event) {
        if(event.getEntity() instanceof EntityFinalBoss) {
            EntityFinalBoss boss = (EntityFinalBoss) event.getEntity();
            bossShields.putIfAbsent(boss.getUniqueID().toString(), false);
            if (bossShields.get(boss.getUniqueID().toString())) {
                OBJModel.OBJBakedModel bakedForceField = (OBJModel.OBJBakedModel) ClientHandler.forcefieldModel.bake(TransformUtils.DEFAULT_BLOCK, DefaultVertexFormats.BLOCK, TextureUtils.bakedTextureGetter);
                GlStateManager.disableCull();
                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                GlStateManager.pushMatrix();

                EntityPlayer viewingPlayer = Minecraft.getMinecraft().player;

                double translationXLT = boss.prevPosX - viewingPlayer.prevPosX;
                double translationYLT = boss.prevPosY - viewingPlayer.prevPosY;
                double translationZLT = boss.prevPosZ - viewingPlayer.prevPosZ;

                double translationX = translationXLT + (((boss.posX - viewingPlayer.posX) - translationXLT) * event.getPartialRenderTick());
                double translationY = translationYLT + (((boss.posY - viewingPlayer.posY) - translationYLT) * event.getPartialRenderTick());
                double translationZ = translationZLT + (((boss.posZ - viewingPlayer.posZ) - translationZLT) * event.getPartialRenderTick());

                GlStateManager.translate(translationX, translationY + 1.1, translationZ);
                event.getRenderer().bindTexture(FORCEFIELD);
                GlStateManager.scale(8.0, 8.0, 8.0);
                GlStateManager.color(0F, 0.5F, 0.9F, 0.1F);
                renderOBJ(bakedForceField.getQuads(null, null, 0), new ColourRGBA(0F, 0.5F, 0.9F, 0.1F).argb());

                GlStateManager.enableCull();
                GlStateManager.enableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.enableLighting();
                GlStateManager.color(1F, 1F, 1F, 1);
                GlStateManager.popMatrix();
            }
        }
    }

    @SubscribeEvent
    public static void renderAttacks(RenderWorldLastEvent event) {
        for(BlockPos pos : attacks.keySet()) {
            EntityPlayer viewingPlayer = Minecraft.getMinecraft().player;
            if(attacks.get(pos)>27) {
                OBJModel.OBJBakedModel bakedForceField = (OBJModel.OBJBakedModel) ClientHandler.forcefieldModel.bake(TransformUtils.DEFAULT_BLOCK, DefaultVertexFormats.BLOCK, TextureUtils.bakedTextureGetter);
                GlStateManager.disableCull();
                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                GlStateManager.pushMatrix();

                double translationXLT = pos.getX() - viewingPlayer.prevPosX;
                double translationYLT = pos.getY() - viewingPlayer.prevPosY;
                double translationZLT = pos.getZ() - viewingPlayer.prevPosZ;

                double translationX = translationXLT + (((pos.getX() - viewingPlayer.posX) - translationXLT) * event.getPartialTicks());
                double translationY = translationYLT + (((pos.getY() - viewingPlayer.posY) - translationYLT) * event.getPartialTicks());
                double translationZ = translationZLT + (((pos.getZ() - viewingPlayer.posZ) - translationZLT) * event.getPartialTicks());

                GlStateManager.translate(translationX, translationY + 1.1, translationZ);
                Minecraft.getMinecraft().getTextureManager().bindTexture(ATTACK);
                GlStateManager.scale(attackSize.get(pos), attackSize.get(pos), attackSize.get(pos));
                GlStateManager.color(0F, 0.5F, 0.9F, 0.1F);
                renderOBJ(bakedForceField.getQuads(null, null, 0), new ColourRGBA(0F, 0.5F, 0.9F, 0.1F).argb());

                GlStateManager.enableCull();
                GlStateManager.enableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.enableLighting();
                GlStateManager.color(1F, 1F, 1F, 1);
                GlStateManager.popMatrix();
            } else {
                for (int i = 0; i < getParticleLoops(attacks.get(pos)); ++i) {
                    Vec3d spawned = new Vec3d(pos.getX() + (rand.nextDouble() - 0.5) * (double) attackSize.get(pos),
                            pos.getY() + (rand.nextDouble() - 0.5) * (double) attackSize.get(pos),
                            pos.getZ() + (rand.nextDouble() - 0.5) * (double) attackSize.get(pos));
                    Vec3d toCenter = spawned.subtract(new Vec3d(pos.getX(),pos.getY(),pos.getZ())).normalize();
                    float velocity = getVelocityFactor(attacks.get(pos));
                    viewingPlayer.world.spawnParticle(EnumParticleTypes.PORTAL, spawned.x, spawned.y, spawned.z,
                            toCenter.x*velocity, toCenter.y*velocity, toCenter.z*velocity);
                }
            }
        }
    }

    private static int getParticleLoops(int timeStamp) {
        if(timeStamp+10<=0) return 2;
        return timeStamp+10;
    }

    private static float getVelocityFactor(int timeStamp) {
        if(timeStamp+200<=0) return 0.02f;
        return (timeStamp+200f)*0.02f;
    }

    @SubscribeEvent
    public static void tickTimers(TickEvent.ClientTickEvent event) {
        if(event.phase==TickEvent.Phase.END) {
            List<BlockPos> toRemove = new ArrayList<>();
            for(BlockPos pos : attacks.keySet()) {
                attacks.put(pos,attacks.get(pos)+1);
                if(attacks.get(pos)>=30) toRemove.add(pos);
            }
            for(BlockPos pos : toRemove) attacks.remove(pos);
        }
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
}
