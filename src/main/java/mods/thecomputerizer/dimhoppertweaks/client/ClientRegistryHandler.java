package mods.thecomputerizer.dimhoppertweaks.client;

import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.client.render.RenderFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.client.render.RenderHomingProjectile;
import mods.thecomputerizer.dimhoppertweaks.registry.ItemRegistry;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.HomingProjectile;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Constants.MODID, value = { Side.CLIENT })
public final class ClientRegistryHandler {
    public final static ResourceLocation FORCEFIELD = new ResourceLocation(Constants.MODID,"textures/models/forcefield.png");
    public final static ResourceLocation ATTACK = new ResourceLocation(Constants.MODID,"textures/models/attack.png");
    public static OBJModel FORCEFIELD_MODEL;
    public static float FOG_DENSITY_OVERRIDE = -1f;

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
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.STARGATE_ADDRESSER, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.SKILL_TOKEN, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.SKILL_TOKEN.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_1, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_2, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_3, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_4, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_5, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_6, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_7, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_8, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_9, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_10, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_11, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_12, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_13, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_14, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_15, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_16, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_17, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_18, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_19, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_20, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_21, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_22, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_23, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_24, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_25, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_26, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_27, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_28, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_29, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_30, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.PRESTIGE_TOKEN_31, 0, new ModelResourceLocation(Objects.requireNonNull(ItemRegistry.STARGATE_ADDRESSER.getRegistryName()), "inventory"));
    }

    private static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityFinalBoss.class, RenderFinalBoss::new);
        RenderingRegistry.registerEntityRenderingHandler(HomingProjectile.class, RenderHomingProjectile::new);
        try {
            FORCEFIELD_MODEL = (OBJModel) OBJLoader.INSTANCE.loadModel(new ModelResourceLocation("dimhoppertweaks:models/boss/forcefield.obj"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load obj model!",e);
        }
    }
}
