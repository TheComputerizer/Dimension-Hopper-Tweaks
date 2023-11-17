package mods.thecomputerizer.dimhoppertweaks.common.events;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import mods.thecomputerizer.dimhoppertweaks.common.capability.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

import java.util.Objects;

@SuppressWarnings("ConstantValue")
@Mod.EventBusSubscriber(modid = Constants.MODID)
public class GameEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerJoin(PlayerLoggedInEvent event) {
        if(event.isCanceled()) return;
        if(event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.player;
            checkDimStage(player,false,false,"bridgeone",7,20,684);
            if(!GameStageHelper.hasStage(player,"bedrockFinal"))
                player.inventory.clearMatchingItems(Item.getItemFromBlock(Blocks.BEDROCK),-1,0,null);
            SkillWrapper.updateTokens(player);
            ISkillCapability cap = SkillWrapper.getSkillCapability(player);
            if(Objects.nonNull(cap)) {
                BlockPos pos = cap.getTwilightRespawn();
                if(player.dimension==7 && Objects.isNull(player.getBedLocation(7)) && Objects.nonNull(pos))
                    player.setSpawnPoint(pos, true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerRespawn(PlayerRespawnEvent event) {
        if(event.isCanceled()) return;
        if(event.player instanceof EntityPlayerMP) SkillWrapper.forceTwilightRespawn(event.player);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void pickUpItem(ItemPickupEvent event) {
        if(event.isCanceled()) return;
        if(event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.player;
            ItemStack stack = event.getStack();
            if(!GameStageHelper.hasStage(player,"bedrockFinal") &&
                    stack.getItem()==Item.getItemFromBlock(Blocks.BEDROCK))
                stack.setCount(0);
            if(stack.getCount()>0) {
                ISkillCapability cap = SkillWrapper.getSkillCapability(player);
                if(Objects.nonNull(cap) && cap.checkGatheringItem(stack.getItem())) {
                    int sizeFactor = stack.getCount()>1 ? (int)(Math.log(stack.getCount())/Math.log(2)) : 1;
                    SkillWrapper.addSP(player,"gathering",sizeFactor,false);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onChangedDimensions(PlayerChangedDimensionEvent event) {
        if(event.isCanceled()) return;
        PlayerData data = PlayerDataHandler.get(event.player);
        if(Objects.nonNull(data)) {
            SkillWrapper.executeOnSkills(data, h -> {
                if(h instanceof ExtendedEventsTrait) ((ExtendedEventsTrait)h).onChangeDimensions(event);
            });
        }
        if(event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.player;
            checkDimStage(player,false,true,"twilight",7);
            checkDimStage(player,true,false,"nether",-1);
            checkDimStage(player,true,false,"finalfrontier",-19);
            SkillWrapper.addSP(player,"void",5f,false);
            ISkillCapability cap = SkillWrapper.getSkillCapability(player);
            if(Objects.nonNull(cap)) cap.resetDreamTimer();
        }
    }

    private static void checkDimStage(EntityPlayer player, boolean goodDim, boolean goodStage, String stage, int ... dimensions) {
        boolean passedDimCheck = dimensions.length==0 || !goodDim;
        for(int dim : dimensions) {
            if(player.dimension==dim) {
                if(!goodDim) return;
                else {
                    passedDimCheck = true;
                    break;
                }
            }
        }
        if(passedDimCheck && goodStage== GameStageHelper.hasStage(player,stage)) {
            if(!goodStage) GameStageHelper.addStage(player,stage);
            else GameStageHelper.removeStage(player,stage);
        }
    }
}
