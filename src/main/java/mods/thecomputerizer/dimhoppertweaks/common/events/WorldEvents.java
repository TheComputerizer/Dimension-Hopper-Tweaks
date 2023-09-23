package mods.thecomputerizer.dimhoppertweaks.common.events;

import lumien.randomthings.item.ModItems;
import mods.thecomputerizer.dimhoppertweaks.common.skills.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.ItemTimeInABottleAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import slimeknights.tconstruct.library.tools.TinkerToolCore;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class WorldEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void blockBreak(BlockEvent.BreakEvent event) {
        if(event.getPlayer() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
            ISkillCapability cap = SkillWrapper.getSkillCapability(player);
            if(Objects.nonNull(cap)) {
                Item heldItem = player.getHeldItemMainhand().getItem();
                if (heldItem instanceof ItemTool || heldItem instanceof TinkerToolCore) {
                    IBlockState state = event.getState();
                    int harvestLevel = state.getBlock() == Blocks.COAL_ORE ? 1 : state.getBlock().getHarvestLevel(state);
                    harvestLevel = harvestLevel <= 0 && player.world.rand.nextFloat() <= ((float)(((int)(Math.log(
                            cap.getSkillLevel("mining"))/Math.log(2)))+1))/10f ? 1 : harvestLevel;
                    if(harvestLevel > 0) {
                        int hardness = (int) state.getBlockHardness(event.getWorld(), event.getPos());
                        int hardnessPower = Math.min(hardness > 1 ? (int) (Math.log(hardness) / Math.log(2)) : 0, 10);
                        SkillWrapper.addSP(player, "mining", Math.max(1, (hardnessPower + harvestLevel) / 2), false);
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void blockPlace(BlockEvent.PlaceEvent event) {
        if(event.getPlayer() instanceof EntityPlayerMP && event.getState().getBlock()!=Blocks.FARMLAND)
            SkillWrapper.addSP((EntityPlayerMP)event.getPlayer(),"building",1f,false);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if(event.phase==TickEvent.Phase.END) {
            int dim = event.world.provider.getDimension();
            if(dim==44 || dim==45) {
                synchronized (event.world.playerEntities) {
                    EntityPlayerMP wakingUp = null;
                    for(EntityPlayer p : event.world.playerEntities) {
                        EntityPlayerMP player = (EntityPlayerMP) p;
                        int ticks = checkTime(player.getHeldItemOffhand()) || checkTime(player.getHeldItemMainhand()) ? -1 : 1;
                        if(SkillWrapper.ticKDreamer(player, ticks)) {
                            wakingUp = player;
                            break;
                        }
                    }
                    //This needs to be outside the loop to avoid a cmod error since teleporting removes the player from the list
                    if(Objects.nonNull(wakingUp)) wakeUp(wakingUp);
                }
            }
        }
    }

    private static boolean checkTime(ItemStack stack) {
        return stack.getItem()==ModItems.timeInABottle &&
                ((ItemTimeInABottleAccess)stack.getItem()).dimhoppertweaks$hasTime(stack);
    }

    @SuppressWarnings("ConstantValue")
    private static void wakeUp(EntityPlayerMP player) {
        ISkillCapability cap = SkillWrapper.getSkillCapability(player);
        if(Objects.nonNull(cap)) cap.resetDreamTimer();
        int respawnDim = player.getSpawnDimension();
        BlockPos respawnPos = player.getBedLocation(respawnDim);
        if(Objects.isNull(respawnPos)) respawnPos = player.getPosition();
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        server.getCommandManager().executeCommand(server,"/tpx "+player.getName()+" "+ respawnPos.getX()+" "+
                respawnPos.getY()+" "+respawnPos.getZ()+" "+respawnDim);
    }
}
