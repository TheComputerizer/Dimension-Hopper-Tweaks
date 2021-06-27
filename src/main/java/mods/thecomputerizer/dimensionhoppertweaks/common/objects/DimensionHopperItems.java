package mods.thecomputerizer.dimensionhoppertweaks.common.objects;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.items.StargateAddresser;
import mods.thecomputerizer.dimensionhoppertweaks.util.Util;
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
    public static final Item STARGATE_ADDRESSER = Util.sneakyNull();

    private DimensionHopperItems() {}

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(makeItem("stargate_addresser", StargateAddresser::new, item -> item.setCreativeTab(CreativeTabs.MISC)));
    }

    public static Item makeBasicItem(final String name, final CreativeTabs tab) {
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
