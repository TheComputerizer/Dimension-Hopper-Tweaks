package mods.thecomputerizer.dimensionhoppertweaks.common.objects.items;

import gcewing.sg.SGCraft;
import gcewing.sg.interfaces.ISGBlock;
import mods.thecomputerizer.dimensionhoppertweaks.generator.Stargate;
import gcewing.sg.tileentity.SGBaseTE;
import gcewing.sg.util.SGAddressing;
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
import net.minecraft.init.Items;

public class StargateAddresser extends EpicItem {
    public ITextComponent text;
    public Stargate gate = new Stargate();
    public int id = 0;
    public int[] from = {-28,-29,0,-31,-1502,-30,816,-15,-1500,-1501,-1506,-16,-1508,-1509,-1510,66,-19};
    public int[] to = {-29,-30,-31,-13,-1503,-1502,-15,-1500,-1501,-1506,-16,-1508,-1509,-1510,-17,-19,-21};
    public boolean found = false;

    public StargateAddresser() {}

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player,EnumHand hand) {
        player.getCooldownTracker().setCooldown(this, 200);
        ItemStack stack = new ItemStack(Items.PAPER);
        ItemStack chev = new ItemStack(SGCraft.sgChevronUpgrade);
        text = new TextComponentString("Gate successfully located with address: (empty address)!");
        if (!world.isRemote) {
            for (int i=0;i<from.length;i++) {
                if (world.provider.getDimension() == from[i]) {
                    id = to[i];
                    found = true;
                }
            }
            if (!found) {
                text = new TextComponentString("Gate could not be located");
                player.sendStatusMessage(text, true);
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
            DimensionManager.initDimension(id);
            World genhere = DimensionManager.getWorld(id);
            BlockPos pos = player.getPosition();
            gate.build(genhere, pos);
            Block block = genhere.getBlockState(pos).getBlock();
            if (block instanceof ISGBlock) {
                SGBaseTE te = ((ISGBlock) block).getBaseTE(genhere, pos);
                if (te != null) {
                    te.applyChevronUpgrade(chev, player);
                    String address;
                    try {
                        address = te.getHomeAddress();
                    } catch (SGAddressing.AddressingError e) {
                        player.sendStatusMessage(text, true);
                        return null;
                    }
                    text = new TextComponentString("Gate successfully located with address: " + address + "!");
                    stack.setStackDisplayName("§5" + address);
                }
            }
            player.sendStatusMessage(text, true);
            found=false;
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
