package mods.thecomputerizer.dimensionhoppertweaks.init;

import mods.thecomputerizer.dimensionhoppertweaks.objects.items.ItemSpawn;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemInit {
    public static final List<Item> ITEMS = new ArrayList<Item>();

    //public static final Item ITEM_NAME = new ITEMITEMNAME();
    //follow this format for adding new items
    public static final Item BOSS_SPAWNEGG = new ItemSpawn("spawn_boss");
}
