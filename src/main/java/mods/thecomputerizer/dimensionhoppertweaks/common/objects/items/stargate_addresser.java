package mods.thecomputerizer.dimensionhoppertweaks.common.objects.items;

import mods.thecomputerizer.dimensionhoppertweaks.generator.handler;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.DimensionHopperItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class stargate_addresser extends Item {
    private final Random rand = new Random();

    public stargate_addresser() {}

    public static Item register() {
        return DimensionHopperItems.makeBasicItem("stargate_addresser", CreativeTabs.MISC);
    }

    @Override
    public @Nonnull ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        //int dim = world.provider.getDimension();
        this.rand.setSeed(world.getSeed());
        handler.generateStructure("stargate", world, rand, player.getPosition());
        return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}
