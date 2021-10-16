package mods.thecomputerizer.dimensionhoppertweaks.client;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.client.entity.render.RenderFinalBoss;
import mods.thecomputerizer.dimensionhoppertweaks.client.entity.render.RenderWhiteNova;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.DimensionHopperItems;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityFinalBoss;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityWhiteNova;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = DimensionHopperTweaks.MODID, value = { Side.CLIENT })
public final class ClientHandler {

    public static void registerRenderers() {
        // Items are handled automatically due to the above annotation
        registerEntityRenderers();
    }

    // <editor-fold region="Items">
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
    // </editor-fold>

    // <editor-fold region="Entities">
    private static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityFinalBoss.class, RenderFinalBoss::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWhiteNova.class, RenderWhiteNova::new);
    }
    // </editor-fold>
}
