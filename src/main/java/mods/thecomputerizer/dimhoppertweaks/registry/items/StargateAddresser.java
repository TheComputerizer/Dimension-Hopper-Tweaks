package mods.thecomputerizer.dimhoppertweaks.registry.items;

import gcewing.sg.SGCraft;
import gcewing.sg.interfaces.ISGBlock;
import gcewing.sg.tileentity.SGBaseTE;
import gcewing.sg.util.SGAddressing;
import mods.thecomputerizer.dimhoppertweaks.registry.StructureRegistry;
import mods.thecomputerizer.theimpossiblelibrary.util.object.ItemUtil;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StargateAddresser extends EpicItem {

    public final Map<Integer,Integer> dimIDPairs;
    public final Map<Integer,String> dimGateStages;

    public StargateAddresser() {
        this.dimIDPairs = new HashMap<>();
        this.dimGateStages = new HashMap<>();
        this.dimIDPairs.put(-28,-29);    //moon->mars
        this.dimGateStages.put(-28,"stargateMoon");
        this.dimIDPairs.put(-29,-30);    //mars->asteroids
        this.dimGateStages.put(-29,"stargateMars");
        this.dimIDPairs.put(0,-31);      //overworld->venus
        this.dimGateStages.put(0,"stargateOverworld");
        this.dimIDPairs.put(-31,-13);    //venus->mercury
        this.dimGateStages.put(-31,"stargateVenus");
        this.dimIDPairs.put(-1502,-1503);//phobos->deimos
        this.dimGateStages.put(-1502,"stargatePhobos");
        this.dimIDPairs.put(-30,-1502);  //asteroids->phobos
        this.dimGateStages.put(-30,"stargateAsteroids");
        this.dimIDPairs.put(816,-15);    //lunalus->jupiter
        this.dimGateStages.put(816,"stargateLunalus");
        this.dimIDPairs.put(-15,-1500);  //jupiter->io
        this.dimGateStages.put(-15,"stargateJupiter");
        this.dimIDPairs.put(-1500,-1501);//io->europa
        this.dimGateStages.put(-1500,"stargateIo");
        this.dimIDPairs.put(-1501,-1506);//europa->ganymede
        this.dimGateStages.put(-1501,"stargateEuropa");
        this.dimIDPairs.put(-1506,-16);  //ganymede->saturn
        this.dimGateStages.put(-1506,"stargateGanymede");
        this.dimIDPairs.put(-16,-1508);  //saturn->titan
        this.dimGateStages.put(-16,"stargateSaturn");
        this.dimIDPairs.put(-1508,-1509);//titan->oberon
        this.dimGateStages.put(-1508,"stargateTitan");
        this.dimIDPairs.put(-1509,-1510);//oberon->titania
        this.dimGateStages.put(-1509,"stargateOberon");
        this.dimIDPairs.put(-1510,-17);  //titania->uranus
        this.dimGateStages.put(-1510,"stargateTitania");
        this.dimIDPairs.put(66,-19);     //erebus->pluto
        this.dimGateStages.put(66,"stargateErebus");
        this.dimIDPairs.put(-19,-21);    //pluto->eris
        this.dimGateStages.put(-19,"stargatePluto");
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        player.getCooldownTracker().setCooldown(this, 200);
        ItemStack stack = new ItemStack(Items.PAPER);
        EnumActionResult result = EnumActionResult.PASS;
        if(!world.isRemote) {
            ItemStack chev = new ItemStack(SGCraft.sgChevronUpgrade);
            ITextComponent text = new TextComponentString("Gate could not be located");
            int dimFrom = world.provider.getDimension();
            if(canPlayerUse(player,player.getHeldItem(hand),dimFrom)) {
                int dimTo = this.dimIDPairs.get(dimFrom);
                DimensionManager.initDimension(dimTo);
                World genhere = DimensionManager.getWorld(dimTo);
                BlockPos pos = player.getPosition();
                StructureRegistry.STARGATE.build(genhere,pos);
                Block block = genhere.getBlockState(pos).getBlock();
                if(block instanceof ISGBlock) {
                    SGBaseTE gateBase = ((ISGBlock)block).getBaseTE(genhere,pos);
                    if(Objects.nonNull(gateBase)) {
                        gateBase.applyChevronUpgrade(chev,player);
                        String address;
                        try {
                            address = gateBase.getHomeAddress();
                            text = new TextComponentTranslation("item.dimhoppertweaks.stargate_addresser.success",address);
                            stack.setStackDisplayName(String.format("Stargate Address: §5%s",address));
                            result = EnumActionResult.SUCCESS;
                        } catch (SGAddressing.AddressingError e) {
                            result = EnumActionResult.FAIL;
                        }
                    }
                }
            }
            player.sendStatusMessage(text,true);
        }
        return new ActionResult<>(result,stack);
    }

    private boolean canPlayerUse(EntityPlayer player, ItemStack stack, int dimID) {
        NBTTagCompound tag = ItemUtil.getOrCreateTag(stack);
        if(!tag.hasKey("activeDimension") || tag.getInteger("activeDimension")!=dimID) return false;
        String stage = this.dimGateStages.get(dimID);
        return Objects.nonNull(stage) && this.dimIDPairs.containsKey(dimID) && GameStageHelper.hasStage(player,stage);
    }
}
