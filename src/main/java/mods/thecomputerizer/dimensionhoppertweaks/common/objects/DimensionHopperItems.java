package mods.thecomputerizer.dimensionhoppertweaks.common.objects;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.items.RealitySlasher;
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
    public static final Item REALITY_SLASHER = Util.sneakyNull();

    private DimensionHopperItems() {}

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(makeItem("stargate_addresser", StargateAddresser::new, item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("reality_slasher", RealitySlasher::new, item -> item.setCreativeTab(CreativeTabs.MISC)));
    }

    private static Item makeItem(final String name, final Supplier<Item> constructor, final Consumer<Item> config) {
        final Item item = constructor.get();
        config.accept(item);
        item.setTranslationKey(DimensionHopperTweaks.MODID + "." + name);
        item.setRegistryName(DimensionHopperTweaks.MODID, name);
        item.setMaxStackSize(1);
        return item;
    }
}
