package mods.thecomputerizer.dimhoppertweaks.registry;

import mods.thecomputerizer.dimhoppertweaks.common.commands.RandomTP;
import mods.thecomputerizer.dimhoppertweaks.common.commands.SummonBoss;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.registry.tiles.LightningEnhancerEntity;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import static mods.thecomputerizer.dimhoppertweaks.registry.ItemRegistry.STARGATE_ADDRESSER;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public final class RegistryHandler {
    public static final CreativeTabs DIM_HOPPER_TAB = new CreativeTabs(Constants.MODID) {
        @SideOnly(Side.CLIENT)
        @Nonnull
        public ItemStack createIcon() {
            return new ItemStack(STARGATE_ADDRESSER);
        }
    };

    @SideOnly(Side.CLIENT)
    public static void onPostInit(FMLPostInitializationEvent event) {
        ParticleRegistry.postInit();
    }

    public static void onServerStarting(FMLServerStartingEvent event) {
        Constants.LOGGER.info("Registering commands");
        event.registerServerCommand(new RandomTP());
        event.registerServerCommand(new SummonBoss());
    }
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(BlockRegistry.getBlocks());
        GameRegistry.registerTileEntity(LightningEnhancerEntity.class, Constants.res("tile.lightning_enhancer"));
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> entities) {
        EntityRegistry.register();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ItemRegistry.getItems());
    }

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(SoundRegistry.getSounds());
    }
}
