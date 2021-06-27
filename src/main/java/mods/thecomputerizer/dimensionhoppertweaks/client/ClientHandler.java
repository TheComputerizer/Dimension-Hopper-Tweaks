package mods.thecomputerizer.dimensionhoppertweaks.client;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.client.entity.render.RenderFinalBoss;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.DimensionHopperItems;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityFinalBoss;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
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
        registerBasicItemModel(DimensionHopperItems.STARGATE_ADDRESSER);
    }

    private static void registerBasicItemModel(final Item item) {
        registerItemModel(item, 0, "inventory");
    }

    private static void registerItemModel(final Item item, final int meta, final String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), id));
    }
    // </editor-fold>

    // <editor-fold region="Entities">
    private static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityFinalBoss.class, RenderFinalBoss::new);
    }
    // </editor-fold>
}
