package mods.thecomputerizer.dimhoppertweaks.client;

import mods.thecomputerizer.dimhoppertweaks.client.render.ClientEffects;
import mods.thecomputerizer.dimhoppertweaks.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

import javax.annotation.Nullable;
import java.util.*;

import static mods.thecomputerizer.dimhoppertweaks.client.render.ClientEffects.COLOR_CORRECTION;
import static mods.thecomputerizer.dimhoppertweaks.client.render.ClientEffects.COLOR_CORRECTION_OVERRIDE;
import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.MODID;
import static net.minecraft.client.renderer.OpenGlHelper.defaultTexUnit;
import static net.minecraft.client.renderer.OpenGlHelper.lightmapTexUnit;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.TEXT;
import static net.minecraftforge.fml.common.eventhandler.EventPriority.LOWEST;
import static net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END;
import static net.minecraftforge.fml.relauncher.Side.CLIENT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

@Mod.EventBusSubscriber(modid = MODID, value = CLIENT)
public class ClientEvents {

    public static Set<Item> autoFeedItems = new HashSet<>();
    public static List<Tuple<Potion,Integer>> autoPotionItems = new ArrayList<>();
    private static boolean screenShakePositive = true;

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        DHTClient.registerItemModels();
    }

    @SubscribeEvent(priority = LOWEST)
    public static void screenShakeUpdate(PlayerTickEvent event) {
        if(event.isCanceled()) return;
        Minecraft mc = Minecraft.getMinecraft();
        if(event.phase==END && event.player==mc.player) {
            if(ClientEffects.isScreenShaking()) {
                event.player.rotationPitch+=ClientEffects.getScreenShake(screenShakePositive);
                screenShakePositive = !screenShakePositive;
            }
        }
    }
    
    @SubscribeEvent(priority = LOWEST)
    public static void onClientLeave(ClientDisconnectionFromServerEvent event) {
        COLOR_CORRECTION_OVERRIDE = 0f;
        COLOR_CORRECTION = 1f;
    }

    @SubscribeEvent(priority = LOWEST)
    public static void onRenderBossBars(RenderGameOverlayEvent.BossInfo event) {
        if(event.isCanceled()) return;
        if(event.getY()>12 && event.getY()>=event.getResolution().getScaledHeight()/5) event.setCanceled(true);
    }

    @SubscribeEvent(priority = LOWEST)
    public static void onPreTextRender(RenderGameOverlayEvent.Pre event) {
        if(event.isCanceled()) return;
        if(event.getType()==TEXT) {
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.color(1f,1f,1f,1f);
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.disableColorMaterial();
            GlStateManager.setActiveTexture(lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(defaultTexUnit);
            GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
        }
    }

    @SubscribeEvent(priority = LOWEST)
    public static void onGetTooltip(ItemTooltipEvent event) {
        if(event.isCanceled()) return;
        if(autoFeedItems.contains(event.getItemStack().getItem()))
            event.getToolTip().add(TextUtil.getTranslated("trait.dimhoppertweaks.hungry_farmer.auto_feed_enabled",
                    event.getItemStack().getItem().getRegistryName()));
        Tuple<Potion,Integer> validPotion = isValidPotion(event.getItemStack());
        if(Objects.nonNull(validPotion))
            event.getToolTip().add(TextUtil.getTranslated("trait.dimhoppertweaks.potion_master.auto_drink_enabled",
                    validPotion.getFirst().getRegistryName(),validPotion.getSecond()+1));
    }

    private static @Nullable Tuple<Potion,Integer> isValidPotion(ItemStack stack) {
        for(PotionEffect effect : PotionUtils.getEffectsFromStack(stack))
            for(Tuple<Potion,Integer> validPotion : autoPotionItems)
                if(effect.getPotion()==validPotion.getFirst() && effect.getAmplifier()==validPotion.getSecond())
                    return validPotion;
        return null;
    }
}