package mods.thecomputerizer.dimensionhoppertweaks.common.objects.items;

import mods.thecomputerizer.dimensionhoppertweaks.generator.Handler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class StargateAddresser extends Item {
    private final Random rand = new Random();

    public StargateAddresser() {}

    @Override
    public @Nonnull ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        //int dim = world.provider.getDimension();
        this.rand.setSeed(world.getSeed());
        Handler.generateStructure("stargate", world, rand, player.getPosition());
        return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}
