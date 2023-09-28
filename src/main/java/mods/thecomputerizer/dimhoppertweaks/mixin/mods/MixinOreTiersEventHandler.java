package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.TileEntityAccess;
import net.darkhax.bookshelf.util.BlockUtils;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.orestages.OreStages;
import net.darkhax.orestages.OreTiersEventHandler;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Mixin(value = OreTiersEventHandler.class, remap = false)
public class MixinOreTiersEventHandler {

    @Unique private boolean dimhoppertweaks$verifyHooks(World world, @Nullable EntityPlayer player, BlockPos pos, String stage) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileEntityAccess) {
            TileEntityAccess access = (TileEntityAccess)tile;
            return !access.dimhoppertweaks$hasStage(stage);
        }
        return Objects.nonNull(player) && !GameStageHelper.hasStage(player,stage);
    }

    @Unique private boolean dimhoppertweaks$verifyStage(World world, EntityPlayer player, BlockPos pos, String stage) {
        return Objects.isNull(player) || player instanceof FakePlayer ?
                dimhoppertweaks$verifyHooks(world,player,pos,stage) : !GameStageHelper.hasStage(player,stage);
    }

    /**
     * @author The_Computerizer
     * @reason Hook for fixing automatic miners
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @Overwrite
    public void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        EntityPlayer player = event.getEntityPlayer();
        Tuple<String,IBlockState> stageInfo = OreTiersAPI.getStageInfo(state);
        if(Objects.nonNull(stageInfo) && dimhoppertweaks$verifyStage(world,player,pos,stageInfo.getFirst()))
            event.setUseBlock(Event.Result.DENY);
    }

    /**
     * @author The_Computerizer
     * @reason Hook for fixing automatic miners
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    @Overwrite
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        World world = event.getWorld();
        IBlockState state = event.getState();
        EntityPlayer player = event.getPlayer();
        BlockPos pos = event.getPos();
        Tuple<String,IBlockState> stageInfo = OreTiersAPI.getStageInfo(state);
        if(Objects.nonNull(stageInfo)) {
            String stage = stageInfo.getFirst();
            IBlockState otherState = stageInfo.getSecond();
            if(dimhoppertweaks$verifyStage(world,player,pos,stage)) {
                event.setExpToDrop(0);
                if(!ForgeHooks.canHarvestBlock(state.getBlock(),player,world,pos) &&
                        BlockUtils.canHarvestSafely(otherState,player))
                    BlockUtils.dropBlockSafely(world,player,pos,otherState);
            }
        }
    }

    /**
     * @author The_Computerizer
     * @reason Hook for fixing automatic miners
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @Overwrite
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        try {
            EntityPlayer player = event.getEntityPlayer();
            World world = player.getEntityWorld();
            IBlockState state = event.getState();
            BlockPos pos = event.getPos();
            Tuple<String,IBlockState> stageInfo = OreTiersAPI.getStageInfo(state);
            if(Objects.nonNull(stageInfo) && dimhoppertweaks$verifyStage(world,player,pos,stageInfo.getFirst()))
                event.setNewSpeed(BlockUtils.getBreakSpeedToMatch(state,stageInfo.getSecond(),world,player,pos));
        } catch (Exception var3) {
            OreStages.LOG.trace("Error calculating mining speed!",var3);
        }
    }

    /**
     * @author The_Computerizer
     * @reason Hook for fixing automatic miners
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    @Overwrite
    public void onBlockDrops(BlockEvent.HarvestDropsEvent event) {
        World world = event.getWorld();
        IBlockState state = event.getState();
        EntityPlayer player = event.getHarvester();
        BlockPos pos = event.getPos();
        Tuple<String,IBlockState> stageInfo = OreTiersAPI.getStageInfo(state);
        if(Objects.nonNull(stageInfo)) {
            if(Objects.isNull(player) && OreTiersAPI.NON_DEFAULTING.contains(state)) return;
            String stage = stageInfo.getFirst();
            if(dimhoppertweaks$verifyStage(world,player,pos,stage)) {
                IBlockState otherState = stageInfo.getSecond();
                List<ItemStack> drops = event.getDrops();
                int fortune = event.getFortuneLevel();
                drops.clear();
                NonNullList<ItemStack> newDrops = NonNullList.create();
                otherState.getBlock().getDrops(newDrops,world,pos,otherState,fortune);
                drops.addAll(newDrops);
                event.setDropChance(ForgeEventFactory.fireBlockHarvesting(drops,world,pos,otherState,fortune,
                        event.getDropChance(),event.isSilkTouching(),player));
            }
        }

    }
}
