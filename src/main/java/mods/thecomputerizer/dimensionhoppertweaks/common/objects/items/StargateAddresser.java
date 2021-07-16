package mods.thecomputerizer.dimensionhoppertweaks.common.objects.items;

import gcewing.sg.SGCraft;
import gcewing.sg.interfaces.ISGBlock;
import mods.thecomputerizer.dimensionhoppertweaks.generator.Stargate;
import gcewing.sg.tileentity.SGBaseTE;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class StargateAddresser extends Item {
    public ITextComponent text;
    public Stargate gate = new Stargate();
    public int id = 0;

    public StargateAddresser() {}

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player,EnumHand hand) {
        player.getCooldownTracker().setCooldown(this, 200);
        ItemStack stack = player.getHeldItem(hand);
        ItemStack chev = new ItemStack(SGCraft.sgChevronUpgrade);
        text = new TextComponentString("Gate successfully located!");
        if (!world.isRemote) {
            if (world.provider.getDimension()==0) {
                id=1;
            }
            DimensionManager.initDimension(id);
            World genhere = DimensionManager.getWorld(id);
            BlockPos pos = player.getPosition();
            gate.build(genhere, pos);
            Block block = genhere.getBlockState(pos).getBlock();
            if (block instanceof ISGBlock) {
                SGBaseTE te = ((ISGBlock) block).getBaseTE(genhere, pos);
                if (te != null)
                    te.applyChevronUpgrade(chev, player);
            }
            stack.shrink(1);
            player.sendStatusMessage(text, true);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
