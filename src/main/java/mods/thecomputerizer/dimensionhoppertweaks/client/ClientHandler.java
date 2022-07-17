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
