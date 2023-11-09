package mods.thecomputerizer.dimhoppertweaks.client;

import mods.thecomputerizer.dimhoppertweaks.client.render.ClientEffects;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.registry.tiles.LightningEnhancerEntity;
import mods.thecomputerizer.dimhoppertweaks.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Constants.MODID, value = Side.CLIENT)
public class ClientEvents {

    public static Set<Item> autoFeedItems = new HashSet<>();
    private static boolean shaderLoaded = false;
    private static boolean screenShakePositive = true;

    @SubscribeEvent
    public static void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        //if(ClientHandler.FOG_DENSITY_OVERRIDE>=0)
            //event.setDensity(ClientHandler.FOG_DENSITY_OVERRIDE);
    }

    @SubscribeEvent
    public static void onFogColors(EntityViewRenderEvent.FogColors event) {
        //event.setRed(0.5f);
        //event.setGreen(0.5f);
        //event.setBlue(1f);
    }

    @SubscribeEvent
    public static void screenShakeUpdate(TickEvent.PlayerTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if(event.phase==TickEvent.Phase.END && event.player==mc.player) {
            Tuple<LightningEnhancerEntity,Double> entityTuple = getNearbyEnhancer(mc.player);
            float distanceFactor = Objects.nonNull(entityTuple) ?
                    (float)MathHelper.clamp(1d-(entityTuple.getSecond()/32),0d,1d) : 0f;
            distanceFactor = 1f-distanceFactor;
            ClientEffects.COLOR_CORRECTION = distanceFactor;
            ClientEffects.SCREEN_SHAKE = distanceFactor;
            if(!shaderLoaded) {
                mc.entityRenderer.loadShader(ClientEffects.GRAYSCALE_SHADER);
                shaderLoaded = true;
            }
            if(ClientEffects.isScreenShaking()) {
                event.player.rotationPitch += ClientEffects.getScreenShake(screenShakePositive);
                screenShakePositive = !screenShakePositive;
            }
        }
    }

    private static Tuple<LightningEnhancerEntity,Double> getNearbyEnhancer(EntityPlayerSP player) {
        for(TileEntity entity : player.world.loadedTileEntityList) {
            if(entity instanceof LightningEnhancerEntity) {
                double distance = entity.getPos().getDistance((int)player.posX,(int)player.posY,(int)player.posZ);
                if(distance<=32) return new Tuple<>((LightningEnhancerEntity)entity,distance);
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void onRenderBossBars(RenderGameOverlayEvent.BossInfo event) {
        if(event.getY()>12 && event.getY() >= event.getResolution().getScaledHeight()/5) event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPreTextRender(RenderGameOverlayEvent.Pre event) {
        if(event.getType()==RenderGameOverlayEvent.ElementType.TEXT) {
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.color(1f,1f,1f,1f);
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.disableColorMaterial();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        }
    }



    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGetTooltip(ItemTooltipEvent event) {
        if(autoFeedItems.contains(event.getItemStack().getItem()))
            event.getToolTip().add(TextUtil.getTranslated("trait.dimhoppertweaks.hungry_farmer.auto_feed_enabled"));
    }
}
