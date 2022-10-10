package mods.thecomputerizer.dimensionhoppertweaks.client;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.client.entity.render.RenderFinalBoss;
import mods.thecomputerizer.dimensionhoppertweaks.client.entity.render.RenderHomingProjectile;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.DimensionHopperItems;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityFinalBoss;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.HomingProjectile;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = DimensionHopperTweaks.MODID, value = { Side.CLIENT })
public final class ClientHandler {
    public static OBJModel forcefieldModel;

    public static void registerRenderers() {
        registerEntityRenderers();
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        registerBasicItemModel();
    }

    private static void registerBasicItemModel() {
        registerItemModel();
    }

    private static void registerItemModel() {
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.STARGATE_ADDRESSER, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.SKILL_TOKEN, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.SKILL_TOKEN.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_1, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_2, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_3, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_4, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_5, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_6, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_7, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_8, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_9, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_10, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_11, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_12, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_13, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_14, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_15, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_16, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_17, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_18, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_19, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_20, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_21, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_22, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_23, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_24, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_25, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_26, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_27, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_28, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_29, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_30, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(DimensionHopperItems.PRESTIGE_TOKEN_31, 0, new ModelResourceLocation(Objects.requireNonNull(DimensionHopperItems.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
    }

    private static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityFinalBoss.class, RenderFinalBoss::new);
        RenderingRegistry.registerEntityRenderingHandler(HomingProjectile.class, RenderHomingProjectile::new);
        try {
            forcefieldModel = (OBJModel) OBJLoader.INSTANCE.loadModel(new ModelResourceLocation("dimensionhoppertweaks:models/forcefield.obj"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load obj model!",e);
        }
    }
}
