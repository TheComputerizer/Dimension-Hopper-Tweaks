package mods.thecomputerizer.dimhoppertweaks.registry;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.registry.items.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static mods.thecomputerizer.dimhoppertweaks.registry.RegistryHandler.DIM_HOPPER_TAB;

@SuppressWarnings({"unused","SameParameterValue"})
@ParametersAreNonnullByDefault
public final class ItemRegistry {

    private static final List<Item> ALL_ITEMS = new ArrayList<>();
    public static final Item STARGATE_ADDRESSER = makeItem("stargate_addresser",StargateAddresser::new);
    public static final Item REALITY_SLASHER = makeItem("reality_slasher",RealitySlasher::new);
    public static final RecipeFunctionItem RECIPE_FUNCTION = makeItem("recipe_function",RecipeFunctionItem::new);
    public static final Item SKILL_TOKEN = makeItem("skill_token",SkillToken::new);
    public static final Item PRESTIGE_TOKEN = makeItem("prestige_token",PrestigeToken::new,
            item -> item.setMaxStackSize(10));
    public static final Item LIGHTNING_ENHANCER = makeEpicItemBlock(BlockRegistry.LIGHTNING_ENHANCER);

    private static <I extends Item> I makeItem(final String name, final Supplier<I> constructor) {
        return makeItem(name,constructor,null);
    }

    private static <I extends Item> I makeItem(final String name, final Supplier<I> constructor,
                                 final @Nullable Consumer<I> config) {
        final I item = constructor.get();
        item.setCreativeTab(DIM_HOPPER_TAB);
        item.setMaxStackSize(1);
        item.setTranslationKey(DHTRef.MODID+"."+name);
        item.setRegistryName(DHTRef.MODID, name);
        if(Objects.nonNull(config)) config.accept(item);
        ALL_ITEMS.add(item);
        return item;
    }

    private static ItemBlock makeEpicItemBlock(final Block constructor) {
        return makeEpicItemBlock(constructor,null);
    }

    private static ItemBlock makeEpicItemBlock(final Block constructor, final @Nullable Consumer<ItemBlock> config) {
        final ItemBlock item = new ItemBlock(constructor);
        item.setCreativeTab(DIM_HOPPER_TAB);
        item.setMaxStackSize(1);
        item.setRegistryName(Objects.requireNonNull(constructor.getRegistryName()));
        item.setTranslationKey(constructor.getTranslationKey());
        if(Objects.nonNull(config)) config.accept(item);
        ALL_ITEMS.add(item);
        return item;
    }

    public static Item[] getItems() {
        return ALL_ITEMS.toArray(new Item[0]);
    }
}
