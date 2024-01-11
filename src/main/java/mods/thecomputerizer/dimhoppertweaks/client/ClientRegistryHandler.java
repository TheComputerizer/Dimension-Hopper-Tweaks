package mods.thecomputerizer.dimhoppertweaks.client;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.client.render.RenderFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.client.render.RenderHomingProjectile;
import mods.thecomputerizer.dimhoppertweaks.registry.ItemRegistry;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.HomingProjectile;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Objects;

@SuppressWarnings("SameParameterValue")
@Mod.EventBusSubscriber(modid = DHTRef.MODID, value = Side.CLIENT)
public final class ClientRegistryHandler {
    public final static ResourceLocation FORCEFIELD = new ResourceLocation(DHTRef.MODID,"textures/models/forcefield.png");
    public final static ResourceLocation ATTACK = new ResourceLocation(DHTRef.MODID,"textures/models/attack.png");
    public static OBJModel FORCEFIELD_MODEL;
    public static float FOG_DENSITY_OVERRIDE = -1f;

    public static void registerRenderers() {
        registerEntityRenderers();
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        registerBasicItemModels();
    }

    private static void registerBasicItemModels() {
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

    private static ModelResourceLocation getModelRes(String path) {
        return getModelRes(DHTRef.res(path),"");
    }

    private static ModelResourceLocation getModelRes(@Nullable ResourceLocation res, @Nullable String variant) {
        if(Objects.isNull(res)) res = DHTRef.res("null_model");
        if(StringUtils.isBlank(variant)) variant = "normal";
        return new ModelResourceLocation(res,variant);
    }
}
