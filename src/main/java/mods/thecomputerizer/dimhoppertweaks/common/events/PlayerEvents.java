package mods.thecomputerizer.dimhoppertweaks.common.events;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.event.UnlockUnlockableEvent.Post;
import gcewing.sg.block.SGBlock;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.util.WorldUtil;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent.Open;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thebetweenlands.common.item.herblore.ItemElixir;

import java.util.Objects;

import static mods.thecomputerizer.dimhoppertweaks.client.render.ClientEffects.MINING_SPEED;
import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.MODID;
import static mods.thecomputerizer.dimhoppertweaks.registry.TraitRegistry.NATURES_AURA;
import static net.minecraftforge.fml.common.eventhandler.EventPriority.LOWEST;

@EventBusSubscriber(modid = MODID)
public class PlayerEvents {

    @SubscribeEvent(priority = LOWEST)
    public static void breakSpeed(BreakSpeed event) {
        if(event.isCanceled()) return;
        if(event.getState().getBlock() instanceof SGBlock<?>) event.setCanceled(true);
        else if(Objects.nonNull(event.getEntityPlayer())) {
            float speedFactor = 1;
            if(event.getEntityPlayer() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP)event.getEntityPlayer();
                if (Objects.nonNull(player)) {
                    ISkillCapability cap = SkillWrapper.getSkillCapability(player);
                    if(Objects.nonNull(cap)) speedFactor = 1f+cap.getBreakSpeedMultiplier();
                }
            } else speedFactor = MINING_SPEED;
            event.setNewSpeed(event.getNewSpeed()*speedFactor);
        }
    }
    
    @SubscribeEvent(priority = LOWEST)
    public static void onContainerOpened(Open event) {
        if(!event.isCanceled()) {
            EntityPlayer player = event.getEntityPlayer();
            Container container = event.getContainer();
            if(Objects.nonNull(container) && Objects.nonNull(player))
                DelayedModAccess.inheritContainerStages(player,container);
        }
    }

    @SubscribeEvent(priority = LOWEST)
    public static void onPlayerClone(Clone event) {
        if(event.isCanceled()) return;
        if(event.getEntityPlayer() instanceof EntityPlayerMP) {
            EntityPlayerMP to = (EntityPlayerMP)event.getEntityPlayer();
            ISkillCapability capTo = SkillWrapper.getSkillCapability(to);
            if(Objects.nonNull(capTo)) {
                EntityPlayerMP from = (EntityPlayerMP)event.getOriginal();
                ISkillCapability capFrom = SkillWrapper.getSkillCapability(from);
                if(Objects.nonNull(capFrom)) capTo.of((SkillCapability)capFrom,to);
            }
        }
    }

    @SubscribeEvent(priority = LOWEST)
    public static void onLevelUp(LevelUpEvent.Post event) {
        if(event.isCanceled()) return;
        if(event.getEntityPlayer() instanceof EntityPlayerMP)
            SkillWrapper.updateTokens((EntityPlayerMP)event.getEntityPlayer());
    }

    @SubscribeEvent(priority = LOWEST)
    public static void onTraitToggled(Post event) {
        if(event.isCanceled()) return;
        EntityPlayer player = event.getEntityPlayer();
        if(event.getUnlockable()==NATURES_AURA)
            WorldUtil.setFastChunk(player.getEntityWorld(),player.chunkCoordX,player.chunkCoordZ,player);
    }

    @SubscribeEvent(priority = LOWEST)
    public static void pickupXP(PlayerPickupXpEvent event) {
        if(event.isCanceled()) return;
        if(event.getEntityPlayer() instanceof EntityPlayerMP) {
            int factor = (event.getOrb().xpValue>1 ? (int)(Math.log(event.getOrb().xpValue)/Math.log(2)) : 1)*2;
            SkillWrapper.addActionSP((EntityPlayerMP)event.getEntityPlayer(),"magic",factor);
        }
    }

    @SubscribeEvent(priority = LOWEST)
    public static void onLootingLevel(LootingLevelEvent event) {
        if(event.isCanceled()) return;
        if(event.getDamageSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.getDamageSource().getTrueSource();
            if(!player.world.isRemote) {
                if(GameStageHelper.hasStage(player,"hardcore")) event.setLootingLevel(event.getLootingLevel()+100);
                PlayerData data = PlayerDataHandler.get(player);
                if(Objects.nonNull(data)) {
                    SkillWrapper.executeOnSkills(data,h -> {
                        if(h instanceof ExtendedEventsTrait) ((ExtendedEventsTrait)h).onLootingLevel(event);
                    });
                }
            }
        }
    }

    @SubscribeEvent(priority = LOWEST)
    public static void onAdvancement(AdvancementEvent event) {
        if(event.isCanceled()) return;
        if(event.getEntityPlayer() instanceof EntityPlayerMP)
            SkillWrapper.addActionSP((EntityPlayerMP)event.getEntityPlayer(),"research",5f);
    }

    @SubscribeEvent(priority = LOWEST)
    public static void onFoodRightClick(PlayerInteractEvent event) {
        if(event.isCanceled()) return;
        Item item = event.getItemStack().getItem();
        EntityPlayer player = event.getEntityPlayer();
        if(!event.getWorld().isRemote && player.isSneaking() &&
                (event instanceof RightClickItem || event instanceof RightClickBlock ||
                        event instanceof EntityInteract || event instanceof EntityInteractSpecific)) {
            PlayerData data = PlayerDataHandler.get(player);
            if(Objects.nonNull(data)) {
                SkillWrapper.executeOnSkills(data,h -> {
                    if(h instanceof ExtendedEventsTrait) {
                        ExtendedEventsTrait trait = (ExtendedEventsTrait)h;
                        if(item instanceof ItemFood) trait.onShiftRightClickFood(player,(ItemFood)item);
                        else if(item instanceof ItemPotion || item instanceof ItemElixir)
                            trait.onShiftRightClickPotion(player,event.getItemStack());
                    }
                });
            }
        }
    }
    
    @SubscribeEvent(priority = LOWEST)
    public static void onXPPickup(PlayerPickupXpEvent event) {
        if(event.isCanceled()) return;
        EntityPlayer player = event.getEntityPlayer();
        EntityXPOrb xp = event.getOrb();
        if(player instanceof EntityPlayerMP && Objects.nonNull(xp)) {
            PlayerData data = PlayerDataHandler.get(player);
            if(Objects.nonNull(data)) {
                SkillWrapper.executeOnSkills(data,h -> {
                    if(h instanceof ExtendedEventsTrait) ((ExtendedEventsTrait)h).onXPPickup(player,xp);
                });
            }
        }
    }
}