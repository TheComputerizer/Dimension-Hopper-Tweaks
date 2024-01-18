package mods.thecomputerizer.dimhoppertweaks.mixin.forge;

import com.google.common.collect.Lists;
import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import stevekung.mods.moreplanets.utils.blocks.BlockFarmlandMP;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({"deprecation", "DataFlowIssue"})
@ParametersAreNonnullByDefault
@Mixin(value = ForgeHooks.class, remap = false)
public abstract class MixinForgeHooks {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;" +
            "error(Ljava/lang/String;Ljava/lang/Throwable;)V", ordinal = 0), method = "lambda$loadAdvancements$0")
    private static void dimhoppertweaks$stopSpammingAdvancementErrors1(Logger logger, String s, Throwable throwable) {
        logger.debug("Ingoring errored advacncement from {}",
                s.replaceAll("Ingoring errored advacncement from ",""));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;" +
            "error(Ljava/lang/String;Ljava/lang/Throwable;)V", ordinal = 1),
            method = "lambda$loadAdvancements$0")
    private static void dimhoppertweaks$stopSpammingAdvancementErrors2(Logger logger, String s, Throwable throwable) {
        logger.debug("Ingoring unreadable advacncement {}",
                s.replaceAll("Couldn't read advancement ",""));
    }

    /**
     * @author The_Computerizer
     * @reason Hook hoe usage in a more general way than an ItemHoe mixin or UseHoeEvent could handle
     */
    @Overwrite
    public static EnumActionResult onPlaceItemIntoWorld(
            ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
            float hitZ, EnumHand hand) {
        // handle all placement events here
        Block previousBlock = world.getBlockState(pos).getBlock();
        int meta = stack.getItemDamage();
        int size = stack.getCount();
        NBTTagCompound nbt = null;
        if(Objects.nonNull(stack.getTagCompound())) nbt = stack.getTagCompound().copy();
        if(!(stack.getItem() instanceof ItemBucket)) // if not bucket
            world.captureBlockSnapshots = true;
        EnumActionResult ret = stack.getItem().onItemUse(player,world,pos,hand,side,hitX,hitY,hitZ);
        world.captureBlockSnapshots = false;
        if(ret==EnumActionResult.SUCCESS) {
            // save new item data
            int newMeta = stack.getItemDamage();
            int newSize = stack.getCount();
            NBTTagCompound newNBT = null;
            if(Objects.nonNull(stack.getTagCompound())) newNBT = stack.getTagCompound().copy();
            PlaceEvent placeEvent = null;
            @SuppressWarnings("unchecked")
            List<BlockSnapshot> snapshots = (List<BlockSnapshot>)world.capturedBlockSnapshots.clone();
            world.capturedBlockSnapshots.clear();
            // make sure to set pre-placement item data for event
            stack.setItemDamage(meta);
            stack.setCount(size);
            if(Objects.nonNull(nbt)) stack.setTagCompound(nbt);
            if(snapshots.size()>1)
                placeEvent = ForgeEventFactory.onPlayerMultiBlockPlace(player,snapshots,side,hand);
            else if(snapshots.size()==1)
                placeEvent = ForgeEventFactory.onPlayerBlockPlace(player,snapshots.get(0),side,hand);
            if(Objects.nonNull(placeEvent) && placeEvent.isCanceled()) {
                ret = EnumActionResult.FAIL; // cancel placement
                // revert back all captured blocks
                for(BlockSnapshot blocksnapshot : Lists.reverse(snapshots)) {
                    world.restoringBlockSnapshots = true;
                    blocksnapshot.restore(true, false);
                    world.restoringBlockSnapshots = false;
                }
            }
            else {
                // Change the stack to its new content
                stack.setItemDamage(newMeta);
                stack.setCount(newSize);
                if(Objects.nonNull(nbt)) stack.setTagCompound(newNBT);
                for(BlockSnapshot snap : snapshots) {
                    int updateFlag = snap.getFlag();
                    IBlockState oldBlock = snap.getReplacedBlock();
                    IBlockState newBlock = world.getBlockState(snap.getPos());
                    if(!newBlock.getBlock().hasTileEntity(newBlock)) // Containers get placed automatically
                        newBlock.getBlock().onBlockAdded(world,snap.getPos(),newBlock);
                    world.markAndNotifyBlock(snap.getPos(),null,oldBlock,newBlock,updateFlag);
                }
                player.addStat(StatList.getObjectUseStats(stack.getItem()));
            }
        }
        world.capturedBlockSnapshots.clear();
        Block updatedBlock = world.getBlockState(pos).getBlock();
        if(!world.isRemote && !(player instanceof FakePlayer) && updatedBlock!=previousBlock) {
            float amount = 0f;
            if(updatedBlock instanceof BlockFarmland || updatedBlock instanceof BlockFarmlandMP) amount = 3f;
            if(updatedBlock instanceof IGrowable || updatedBlock instanceof IPlantable) amount = 1f;
            if(amount>0f) SkillWrapper.addSP((EntityPlayerMP)player,"farming",amount,false);
        }
        return ret;
    }
}