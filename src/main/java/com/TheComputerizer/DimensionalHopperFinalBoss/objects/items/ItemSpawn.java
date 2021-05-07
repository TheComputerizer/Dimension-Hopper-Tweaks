package com.TheComputerizer.DimensionalHopperFinalBoss.objects.items;

import com.TheComputerizer.DimensionalHopperFinalBoss.DimensionalHopperFinalBoss;
import com.TheComputerizer.DimensionalHopperFinalBoss.proxy.ClientProxy;
import com.TheComputerizer.DimensionalHopperFinalBoss.util.interfaces.IHasModel;
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
        DimensionalHopperFinalBoss.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
