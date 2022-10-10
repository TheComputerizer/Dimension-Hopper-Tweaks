package mods.thecomputerizer.dimensionhoppertweaks.common.objects;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.items.PrestigeToken;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.items.RealitySlasher;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.items.SkillToken;
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
    public static final Item SKILL_TOKEN = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_1 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_2 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_3 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_4 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_5 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_6 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_7 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_8 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_9 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_10 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_11 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_12 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_13 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_14 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_15 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_16 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_17 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_18 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_19 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_20 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_21 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_22 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_23 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_24 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_25 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_26 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_27 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_28 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_29 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_30 = Util.sneakyNull();
    public static final Item PRESTIGE_TOKEN_31 = Util.sneakyNull();

    private DimensionHopperItems() {}

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(makeItem("stargate_addresser", StargateAddresser::new, item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("reality_slasher", RealitySlasher::new, item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("skill_token", SkillToken::new, item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_1", () -> new PrestigeToken(1), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_2", () -> new PrestigeToken(2), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_3", () -> new PrestigeToken(3), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_4", () -> new PrestigeToken(4), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_5", () -> new PrestigeToken(5), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_6", () -> new PrestigeToken(6), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_7", () -> new PrestigeToken(7), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_8", () -> new PrestigeToken(8), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_9", () -> new PrestigeToken(9), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_10", () -> new PrestigeToken(10), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_11", () -> new PrestigeToken(11), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_12", () -> new PrestigeToken(12), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_13", () -> new PrestigeToken(13), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_14", () -> new PrestigeToken(14), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_15", () -> new PrestigeToken(15), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_16", () -> new PrestigeToken(16), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_17", () -> new PrestigeToken(17), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_18", () -> new PrestigeToken(18), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_19", () -> new PrestigeToken(19), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_20", () -> new PrestigeToken(20), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_21", () -> new PrestigeToken(21), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_22", () -> new PrestigeToken(22), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_23", () -> new PrestigeToken(23), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_24", () -> new PrestigeToken(24), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_25", () -> new PrestigeToken(25), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_26", () -> new PrestigeToken(26), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_27", () -> new PrestigeToken(27), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_28", () -> new PrestigeToken(28), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_29", () -> new PrestigeToken(29), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_30", () -> new PrestigeToken(30), item -> item.setCreativeTab(CreativeTabs.MISC)));
        event.getRegistry().register(makeItem("prestige_token_31", () -> new PrestigeToken(31), item -> item.setCreativeTab(CreativeTabs.MISC)));
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
