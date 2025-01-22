package mods.thecomputerizer.dimhoppertweaks.client;

import mods.thecomputerizer.dimhoppertweaks.client.render.RenderFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.client.render.RenderHomingProjectile;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.mixin.vanilla_access.EntityRendererAccess;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.HomingProjectile;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.registry.items.RecipeFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;


import static codechicken.lib.texture.TextureUtils.bakedTextureGetter;
import static codechicken.lib.util.TransformUtils.DEFAULT_BLOCK;
import static java.lang.Float.MAX_VALUE;
import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;
import static mods.thecomputerizer.dimhoppertweaks.registry.ItemRegistry.*;
import static net.minecraft.init.MobEffects.BLINDNESS;
import static net.minecraftforge.client.model.obj.OBJLoader.INSTANCE;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.BLOCK;
import static org.lwjgl.opengl.GL11.GL_FOG_DENSITY;
import static org.lwjgl.opengl.GL11.GL_FOG_END;
import static org.lwjgl.opengl.GL11.GL_FOG_MODE;
import static org.lwjgl.opengl.GL11.GL_FOG_START;

@SuppressWarnings("deprecation")
public final class DHTClient {
    public final static ResourceLocation FORCEFIELD = DHTRef.res("textures/models/forcefield.png");
    public final static ResourceLocation ATTACK = DHTRef.res("textures/models/attack.png");
    public static OBJModel FORCEFIELD_MODEL;
    public static OBJBakedModel BAKED_FORCEFIELD;
    public static float FOG_DENSITY_OVERRIDE = -1f;
    public static boolean REMOVE_FOG = false;

    public static OBJBakedModel getBakedForcefield() {
        if(Objects.isNull(BAKED_FORCEFIELD))
            BAKED_FORCEFIELD = (OBJBakedModel)FORCEFIELD_MODEL.bake(DEFAULT_BLOCK,BLOCK,bakedTextureGetter);
        return BAKED_FORCEFIELD;
    }

    public static void registerRenderers() {
        registerEntityRenderers();
    }

    public static void registerItemModels() {
        ModelLoader.setCustomModelResourceLocation(PRESTIGE_TOKEN,0,getModelRes(STARGATE_ADDRESSER));
        ModelLoader.setCustomModelResourceLocation(RECIPE_FUNCTION,0,getModelRes(STARGATE_ADDRESSER));
        ModelLoader.setCustomModelResourceLocation(SKILL_CREDIT,0,getModelRes(STARGATE_ADDRESSER));
        ModelLoader.setCustomModelResourceLocation(SKILL_TOKEN,0,getModelRes(SKILL_TOKEN));
        ModelLoader.setCustomModelResourceLocation(STARGATE_ADDRESSER,0,getModelRes(STARGATE_ADDRESSER));
        ModelLoader.setCustomMeshDefinition(RECIPE_FUNCTION,RecipeFunction::getModelLocation);
    }

    private static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityFinalBoss.class,RenderFinalBoss::new);
        RenderingRegistry.registerEntityRenderingHandler(HomingProjectile.class,RenderHomingProjectile::new);
        try {
            FORCEFIELD_MODEL = (OBJModel)INSTANCE.loadModel(getModelRes("models/boss/forcefield.obj"));
        } catch(Exception ex) {
            throw new RuntimeException("Failed to load obj model!",ex);
        }
    }

    private static ModelResourceLocation getModelRes(Item item) {
        return getModelRes(item.getRegistryName(),"inventory");
    }

    @SuppressWarnings("SameParameterValue")
    private static ModelResourceLocation getModelRes(String path) {
        return getModelRes(DHTRef.res(path),"");
    }

    private static ModelResourceLocation getModelRes(@Nullable ResourceLocation res, @Nullable String variant) {
        if(Objects.isNull(res)) res = DHTRef.res("null_model");
        if(StringUtils.isBlank(variant)) variant = "normal";
        return new ModelResourceLocation(res,variant);
    }

    public static void fixFog(Minecraft mc) {
        mc.addScheduledTask(() -> {
            float density = tryOrDef(() -> GL11.glGetFloat(GL_FOG_DENSITY),MAX_VALUE,"Unable to query fog density");
            if(density>=0 && density<MAX_VALUE) {
                float farplane = ((EntityRendererAccess)Minecraft.getMinecraft().entityRenderer).getFarPlaneDistance();
                GlStateManager.setFogStart(farplane*0.75f);
                GlStateManager.setFogEnd(farplane);
                if(GLContext.getCapabilities().GL_NV_fog_distance) GlStateManager.glFogi(34138,34139);
                LOGGER.info("Set fog to ({},{})",farplane*0.75f,farplane);
            }
        });
    }

    public static void queryFog(Minecraft mc) {
        mc.addScheduledTask(() -> {
            int fogMode = tryOrDef(() -> GL11.glGetInteger(GL_FOG_MODE),Integer.MAX_VALUE,"Unable to query fog mode");
            float fogStart = tryOrDef(() -> GL11.glGetFloat(GL_FOG_START),MAX_VALUE,"Unable to query fog start");
            float fogEnd = tryOrDef(() -> GL11.glGetFloat(GL_FOG_END),MAX_VALUE,"Unable to query fog end");
            float fogDensity = tryOrDef(() -> GL11.glGetFloat(GL_FOG_DENSITY),MAX_VALUE, "Unable to query fog density");
            LOGGER.error("FOG INFO: `MODE {} | START {} | END {} | DENSITY {}`",fogMode,fogStart,fogEnd,fogDensity);
        });
    }

    public static void queryGame(Minecraft mc) {
        mc.addScheduledTask(() -> {
            GameSettings settings = Minecraft.getMinecraft().gameSettings;
            int renderDist = getOrDef(settings,s -> s.renderDistanceChunks,Integer.MAX_VALUE);
            float farPlaneDist = getOrDef(Minecraft.getMinecraft().entityRenderer, r -> ((EntityRendererAccess)r).getFarPlaneDistance(), MAX_VALUE);
            PotionEffect blindness = Minecraft.getMinecraft().player.getActivePotionEffect(BLINDNESS);
            boolean isBlind = Objects.nonNull(blindness);
            int blindLevel = isBlind ? blindness.getAmplifier() : Integer.MAX_VALUE;
            LOGGER.error("GAME INFO: `RENDER DISTANCE (CHUNKS) {} | FARPLANE DISTANCE {} | "+
                            "BLINDNESS {}` | BLINDNESS LEVEL {}",renderDist,farPlaneDist,isBlind,blindLevel);
        });
    }

    public static void queryShader(Minecraft mc) {
        mc.addScheduledTask(() -> {
            Minecraft.getMinecraft().entityRenderer.switchUseShader();
            LOGGER.error("SWITCH SHADER USAGE");
        });
    }

    public static void queryWorld(Minecraft mc) {
        mc.addScheduledTask(() -> {
            WorldClient world = Minecraft.getMinecraft().world;
            WorldProvider provider = getOrNull(world,wc -> wc.provider);
            int dimension = getOrDef(provider,WorldProvider::getDimension,Integer.MAX_VALUE);
            IRenderHandler cloudRender = getOrNull(provider,WorldProvider::getCloudRenderer);
            IRenderHandler skyRender = getOrNull(provider,WorldProvider::getSkyRenderer);
            IRenderHandler weatherRender = getOrNull(provider,WorldProvider::getWeatherRenderer);
            LOGGER.error("WORLD INFO: `PROVIDER {} | DIMENSION {} | CLOUD RENDER {} | SKY RENDER {} | " +
                            "WEATHER RENDER {}`",getClassName(provider),dimension,getClassName(cloudRender),getClassName(skyRender),
                    getClassName(weatherRender));
        });
    }

    public static <T> @Nullable String getClassName(@Nullable T obj) {
        return getOrNull(obj,o -> o.getClass().getName());
    }

    public static <T,V> @Nullable V getOrNull(@Nullable T obj, Function<T,V> converter) {
        return getOrDef(obj,converter,null);
    }

    public static <T,V> V getOrDef(@Nullable T obj, Function<T,V> converter, V defVal) {
        return Objects.nonNull(obj) ? converter.apply(obj) : defVal;
    }

    public static <V> V tryOrDef(Supplier<V> dangerousSupplier, V defVal, String msg) {
        try {
            return dangerousSupplier.get();
        } catch(Exception ex) {
            LOGGER.error(msg,ex);
            return defVal;
        }
    }
}
