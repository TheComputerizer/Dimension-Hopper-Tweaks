package mods.thecomputerizer.dimhoppertweaks.client;

import mods.thecomputerizer.dimhoppertweaks.client.render.RenderFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.client.render.RenderHomingProjectile;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.registry.ItemRegistry;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.HomingProjectile;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

public final class DHTClient {
    public final static ResourceLocation FORCEFIELD = new ResourceLocation(DHTRef.MODID,"textures/models/forcefield.png");
    public final static ResourceLocation ATTACK = new ResourceLocation(DHTRef.MODID,"textures/models/attack.png");
    public static OBJModel FORCEFIELD_MODEL;
    public static float FOG_DENSITY_OVERRIDE = -1f;

    public static void registerRenderers() {
        registerEntityRenderers();
    }

    public static void registerBasicItemModels() {
        registerItemModels();
    }

    private static void registerItemModels() {
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.STARGATE_ADDRESSER,0,
                getModelRes(ItemRegistry.STARGATE_ADDRESSER));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.SKILL_TOKEN,0,
                getModelRes(ItemRegistry.SKILL_TOKEN));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN,0,
                getModelRes(ItemRegistry.STARGATE_ADDRESSER));
    }

    private static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityFinalBoss.class, RenderFinalBoss::new);
        RenderingRegistry.registerEntityRenderingHandler(HomingProjectile.class, RenderHomingProjectile::new);
        try {
            FORCEFIELD_MODEL = (OBJModel) OBJLoader.INSTANCE.loadModel(getModelRes("models/boss/forcefield.obj"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load obj model!",e);
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

    public static void queryFogRender() {
        Minecraft mc = Minecraft.getMinecraft();
        WorldClient world = mc.world;
        WorldProvider provider = getOrNull(world,wc -> wc.provider);
        int dimension = getOrDef(provider,WorldProvider::getDimension,Integer.MAX_VALUE);
        IRenderHandler cloudRender = getOrNull(provider,WorldProvider::getCloudRenderer);
        IRenderHandler skyRender = getOrNull(provider,WorldProvider::getSkyRenderer);
        IRenderHandler weatherRender = getOrNull(provider,WorldProvider::getWeatherRenderer);
        DHTRef.LOGGER.error("WORLD INFO: `PROVIDER {} | DIMENSION {} | CLOUD RENDER {} | SKY RENDER {} | "+
                "WEATHER RENDER {}`",getClassName(provider),dimension,getClassName(cloudRender),getClassName(skyRender),
                getClassName(weatherRender));
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
}
