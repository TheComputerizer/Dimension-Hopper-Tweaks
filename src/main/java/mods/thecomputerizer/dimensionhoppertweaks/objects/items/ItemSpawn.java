package mods.thecomputerizer.dimensionhoppertweaks.objects.items;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.util.interfaces.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemSpawn extends Item implements IHasModel
{
    public ItemSpawn(String name)
    {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.MATERIALS);
    }

    @Override
    public void registerModels()
    {
        DimensionHopperTweaks.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
