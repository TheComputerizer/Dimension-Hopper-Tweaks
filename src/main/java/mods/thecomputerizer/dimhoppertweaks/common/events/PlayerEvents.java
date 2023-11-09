package mods.thecomputerizer.dimhoppertweaks.common.events;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.common.skills.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.*;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class PlayerEvents {

    private static final MutableInt TICK_DELAY = new MutableInt();

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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLevelUp(LevelUpEvent.Post event) {
        if(event.isCanceled()) return;
        if(event.getEntityPlayer() instanceof EntityPlayerMP)
            SkillWrapper.updateTokens((EntityPlayerMP)event.getEntityPlayer());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerJoin(PlayerLoggedInEvent event) {
        if(event.isCanceled()) return;
        if(event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.player;
            checkDimStage(player,false,false,"bridgeone",7,20,684);
            if(!GameStageHelper.hasStage(player,"bedrockFinal"))
                player.inventory.clearMatchingItems(Item.getItemFromBlock(Blocks.BEDROCK),-1,0,null);
            SkillWrapper.updateTokens(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerRespawn(PlayerRespawnEvent event) {
        if(event.isCanceled()) return;
        if(event.player instanceof EntityPlayerMP) SkillWrapper.forceTwilightRespawn(event.player);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void pickupXP(PlayerPickupXpEvent event) {
        if(event.isCanceled()) return;
        if(event.getEntityPlayer() instanceof EntityPlayerMP) {
            int factor = (event.getOrb().xpValue>1 ? (int)(Math.log(event.getOrb().xpValue)/Math.log(2)) : 1)*2;
            SkillWrapper.addSP((EntityPlayerMP)event.getEntityPlayer(),"magic",factor,false);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLootingLevel(LootingLevelEvent event) {
        if(event.isCanceled()) return;
        if(event.getDamageSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.getDamageSource().getTrueSource();
            if(!player.world.isRemote) {
                PlayerData data = PlayerDataHandler.get(player);
                if(Objects.nonNull(data)) {
                    SkillWrapper.executeOnSkills(data,h -> {
                        if(h instanceof ExtendedEventsTrait) ((ExtendedEventsTrait)h).onLootingLevel(event);
                    });
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAdvancement(AdvancementEvent event) {
        if(event.isCanceled()) return;
        if(event.getEntityPlayer() instanceof EntityPlayerMP)
            SkillWrapper.addSP((EntityPlayerMP)event.getEntityPlayer(),"research",5f,false);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if(event.isCanceled()) return;
        if(event.phase==TickEvent.Phase.END) {
            if(event.side== Side.SERVER) {
                if(TICK_DELAY.addAndGet(1)>=20) {
                    EntityPlayerMP player = (EntityPlayerMP) event.player;
                    if(player.isSprinting()) {
                        int speedFactor = player.isPotionActive(MobEffects.SPEED) ? Objects.requireNonNull(
                                player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier()+2 : 1;
                        SkillWrapper.addSP(player,"agility",speedFactor, false);
                    }
                    ISkillCapability cap = SkillWrapper.getSkillCapability(player);
                    if(Objects.nonNull(cap)) cap.decrementGatheringItems(20);
                    TICK_DELAY.setValue(0);
                }
            }
        }
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

    private static void checkDimStage(EntityPlayer player, boolean goodDim, boolean goodStage, String stage,
                                      int ... dimensions) {
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
        if(passedDimCheck && goodStage==GameStageHelper.hasStage(player,stage)) {
            if(!goodStage) GameStageHelper.addStage(player,stage);
            else GameStageHelper.removeStage(player,stage);
        }
    }
}
