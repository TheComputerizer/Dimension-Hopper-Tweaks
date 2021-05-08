package mods.thecomputerizer.dimensionhoppertweaks.common.objects;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.function.Consumer;
import java.util.function.Supplier;

@GameRegistry.ObjectHolder(DimensionHopperTweaks.MODID)
@Mod.EventBusSubscriber(modid = DimensionHopperTweaks.MODID)
public final class DimensionHopperItems {
    //public static final Item BOSS_SPAWN_EGG = Util.sneakyNull();

    private DimensionHopperItems() {}

    @SubscribeEvent
    public static void onItemRegistration(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                //makeBasicItem("boss_spawn_egg", CreativeTabs.MATERIALS)
        );
    }

    private static Item makeBasicItem(final String name, final CreativeTabs tab) {
        return makeItem(name, Item::new, item -> item.setCreativeTab(tab));
    }

    private static Item makeItem(final String name, final Supplier<Item> constructor, final Consumer<Item> config) {
        final Item item = constructor.get();
        config.accept(item);
        item.setTranslationKey(DimensionHopperTweaks.MODID + "." + name);
        item.setRegistryName(DimensionHopperTweaks.MODID, name);
        return item;
    }
}
