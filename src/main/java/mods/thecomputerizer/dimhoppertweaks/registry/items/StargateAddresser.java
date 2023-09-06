package mods.thecomputerizer.dimhoppertweaks.registry.items;

import gcewing.sg.SGCraft;
import gcewing.sg.interfaces.ISGBlock;
import mods.thecomputerizer.dimhoppertweaks.generator.Stargate;
import gcewing.sg.tileentity.SGBaseTE;
import gcewing.sg.util.SGAddressing;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
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

import javax.annotation.Nonnull;
import java.util.HashMap;

public class StargateAddresser extends EpicItem {
    public final Stargate gate = new Stargate();
    public final HashMap<Integer, Integer> dimIDPairs = new HashMap<>();

    public StargateAddresser() {
        dimIDPairs.put(-28,-29);    //moon->mars
        dimIDPairs.put(-29,-30);    //mars->asteroids
        dimIDPairs.put(0,-31);      //overworld->venus
        dimIDPairs.put(-31,-13);    //venus->mercury
        dimIDPairs.put(-1502,-1503);//phobos->deimos
        dimIDPairs.put(-30,-1502);  //asteroids->phobos
        dimIDPairs.put(816,-15);    //lunalus->jupiter
        dimIDPairs.put(-15,-1500);  //jupiter->io
        dimIDPairs.put(-1500,-1501);//io->europa
        dimIDPairs.put(-1501,-1506);//europa->ganymede
        dimIDPairs.put(-1506,-16);  //ganymede->saturn
        dimIDPairs.put(-16,-1508);  //saturn->titan
        dimIDPairs.put(-1508,-1509);//titan->oberon
        dimIDPairs.put(-1509,-1510);//oberon->titania
        dimIDPairs.put(-1510,-17);  //titania->uranus
        dimIDPairs.put(66,-19);     //erebus->pluto
        dimIDPairs.put(-19,-21);    //pluto->eris
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull  EnumHand hand) {
        player.getCooldownTracker().setCooldown(this, 200);
        ItemStack stack = new ItemStack(Items.PAPER);
        EnumActionResult result = EnumActionResult.PASS;
        if (!world.isRemote) {
            ItemStack chev = new ItemStack(SGCraft.sgChevronUpgrade);
            ITextComponent text = new TextComponentString("Gate could not be located");
            int id = world.provider.getDimension();
            if(dimIDPairs.containsKey(id)) {
                DimensionManager.initDimension(dimIDPairs.get(id));
                World genhere = DimensionManager.getWorld(dimIDPairs.get(id));
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
                            text = new TextComponentString("Gate successfully located with address " + address + "!");
                            stack.setStackDisplayName("§5" + address);
                            result = EnumActionResult.SUCCESS;
                        } catch (SGAddressing.AddressingError e) {
                            result = EnumActionResult.FAIL;
                        }
                    }
                }
            }
            player.sendStatusMessage(text, true);
        }
        return new ActionResult<>(result, stack);
    }
}
