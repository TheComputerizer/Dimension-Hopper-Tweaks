package mods.thecomputerizer.dimhoppertweaks.common.events;

import lumien.randomthings.item.ModItems;
import mekanism.common.security.ISecurityTile;
import mods.thecomputerizer.dimhoppertweaks.common.commands.DHDebugCommands;
import mods.thecomputerizer.dimhoppertweaks.common.skills.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.ItemTimeInABottleAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.TileEntityAccess;
import mods.thecomputerizer.dimhoppertweaks.util.WorldUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import slimeknights.tconstruct.library.tools.TinkerToolCore;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

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
        EntityPlayer player = event.getPlayer();
        if(player instanceof EntityPlayerMP && event.getState().getBlock()!=Blocks.FARMLAND)
            SkillWrapper.addSP((EntityPlayerMP)event.getPlayer(),"building",1f,false);
        if(Objects.nonNull(player)) {
            TileEntity tile = event.getWorld().getTileEntity(event.getPos());
            if(Objects.nonNull(tile) && !(tile instanceof ISecurityTile))
                ((TileEntityAccess)tile).dimhoppertweaks$setStages(getPotentialFakePlayerStages(player,event.getPos()));
        }
    }

    private static Collection<String> getPotentialFakePlayerStages(EntityPlayer player, BlockPos pos) {
        if(player instanceof FakePlayer) {
            Set<Class<?>> placerClasses = DelayedModAccess.getPlacerTileClasses();
            World world = player.getEntityWorld();
            TileEntity tile = WorldUtil.checkValidTile(world,player.getPosition(),placerClasses);
            if(Objects.nonNull(tile)) return ((TileEntityAccess)tile).dimhoppertweaks$getStages();
            tile = WorldUtil.getTileOrAdjacent(world,pos,false,placerClasses);
            if(Objects.nonNull(tile)) return ((TileEntityAccess)tile).dimhoppertweaks$getStages();
        }
        return DelayedModAccess.getGameStages(player);
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
        server.getCommandManager().executeCommand(server,DHDebugCommands.buildRawCommand("tpx",player.getName(),
                respawnPos.getX(),respawnPos.getY(),respawnPos.getZ(),respawnDim));
    }
}
