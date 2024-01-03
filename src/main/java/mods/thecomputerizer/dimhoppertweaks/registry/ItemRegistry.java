package mods.thecomputerizer.dimhoppertweaks.registry;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.registry.items.PrestigeToken;
import mods.thecomputerizer.dimhoppertweaks.registry.items.RealitySlasher;
import mods.thecomputerizer.dimhoppertweaks.registry.items.SkillToken;
import mods.thecomputerizer.dimhoppertweaks.registry.items.StargateAddresser;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static mods.thecomputerizer.dimhoppertweaks.registry.RegistryHandler.DIM_HOPPER_TAB;

@SuppressWarnings("unused")
public final class ItemRegistry {

    private static final List<Item> ALL_ITEMS = new ArrayList<>();
    public static final Item STARGATE_ADDRESSER = makeItem("stargate_addresser", StargateAddresser::new,
            item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item REALITY_SLASHER = makeItem("reality_slasher", RealitySlasher::new,
            item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item SKILL_TOKEN = makeItem("skill_token", SkillToken::new,
            item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_1 = makeItem("prestige_token_1", () -> new PrestigeToken(1),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_2 = makeItem("prestige_token_2", () -> new PrestigeToken(2),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_3 = makeItem("prestige_token_3", () -> new PrestigeToken(3),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_4 = makeItem("prestige_token_4", () -> new PrestigeToken(4),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_5 = makeItem("prestige_token_5", () -> new PrestigeToken(5),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_6 = makeItem("prestige_token_6", () -> new PrestigeToken(6),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_7 = makeItem("prestige_token_7", () -> new PrestigeToken(7),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_8 = makeItem("prestige_token_8", () -> new PrestigeToken(8),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_9 = makeItem("prestige_token_9", () -> new PrestigeToken(9),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_10 = makeItem("prestige_token_10", () -> new PrestigeToken(10),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_11 = makeItem("prestige_token_11", () -> new PrestigeToken(11),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_12 = makeItem("prestige_token_12", () -> new PrestigeToken(12),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_13 = makeItem("prestige_token_13", () -> new PrestigeToken(13),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_14 = makeItem("prestige_token_14", () -> new PrestigeToken(14),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_15 = makeItem("prestige_token_15", () -> new PrestigeToken(15),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_16 = makeItem("prestige_token_16", () -> new PrestigeToken(16),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_17 = makeItem("prestige_token_17", () -> new PrestigeToken(17),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_18 = makeItem("prestige_token_18", () -> new PrestigeToken(18),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_19 = makeItem("prestige_token_19", () -> new PrestigeToken(19),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_20 = makeItem("prestige_token_20", () -> new PrestigeToken(20),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_21 = makeItem("prestige_token_21", () -> new PrestigeToken(21),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_22 = makeItem("prestige_token_22", () -> new PrestigeToken(22),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_23 = makeItem("prestige_token_23", () -> new PrestigeToken(23),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_24 = makeItem("prestige_token_24", () -> new PrestigeToken(24),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_25 = makeItem("prestige_token_25", () -> new PrestigeToken(25),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_26 = makeItem("prestige_token_26", () -> new PrestigeToken(26),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_27 = makeItem("prestige_token_27", () -> new PrestigeToken(27),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_28 = makeItem("prestige_token_28", () -> new PrestigeToken(28),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_29 = makeItem("prestige_token_29", () -> new PrestigeToken(29),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_30 = makeItem("prestige_token_30", () -> new PrestigeToken(30),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item PRESTIGE_TOKEN_31 = makeItem("prestige_token_31", () -> new PrestigeToken(31),
                    item -> item.setCreativeTab(DIM_HOPPER_TAB));
    public static final Item LIGHTNING_ENHANCER = makeEpicItemBlock(BlockRegistry.LIGHTNING_ENHANCER,
            item -> item.setCreativeTab(DIM_HOPPER_TAB).setMaxStackSize(1));


    private static Item makeItem(final String name, final Supplier<Item> constructor, final Consumer<Item> config) {
        final Item item = constructor.get();
        config.accept(item);
        item.setTranslationKey(DHTRef.MODID+"."+name);
        item.setRegistryName(DHTRef.MODID, name);
        item.setMaxStackSize(1);
        ALL_ITEMS.add(item);
        return item;
    }

    @SuppressWarnings("SameParameterValue")
    private static ItemBlock makeEpicItemBlock(final @Nonnull Block constructor, final Consumer<ItemBlock> config) {
        final ItemBlock item = new ItemBlock(constructor);
        config.accept(item);
        item.setRegistryName(Objects.requireNonNull(constructor.getRegistryName()));
        item.setTranslationKey(constructor.getTranslationKey());
        ALL_ITEMS.add(item);
        return item;
    }

    public static Item[] getItems() {
        return ALL_ITEMS.toArray(new Item[0]);
    }
}
