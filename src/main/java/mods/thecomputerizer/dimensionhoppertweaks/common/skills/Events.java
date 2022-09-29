package mods.thecomputerizer.dimensionhoppertweaks.common.skills;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.items.SkillToken;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = DimensionHopperTweaks.MODID)
public class Events {

    public static final ResourceLocation SKILL_CAPABILITY = new ResourceLocation(DimensionHopperTweaks.MODID, "skills");

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void attachWorldCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getObject();
            event.addCapability(SKILL_CAPABILITY, new SkillCapabilityProvider(player));
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static SkillCapability getCapability(EntityPlayer player) {
        return (SkillCapability) player.getCapability(SkillCapabilityProvider.SKILL_CAPABILITY,null);
    }

    private static void updateTokens(EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(stack.getItem() instanceof SkillToken) {
                ((SkillToken)stack.getItem()).updateSkills(stack,getCapability(player).getCurrentValues());
            }
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if(event.side==Side.SERVER && event.phase==TickEvent.Phase.END) {
            EntityPlayerMP player = (EntityPlayerMP)event.player;
            if(player.isSprinting() && getCapability(player).checkTick()) {
                getCapability(player).addAgilityXP(getCapability(player).getSkillXpMultiplier(1f),player);
                updateTokens(player);
            }
        }
    }

    @SubscribeEvent
    public static void pickUpItem(PlayerEvent.ItemPickupEvent event) {
        if(event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.player;
            getCapability(player).addGatheringXP(getCapability(player).getSkillXpMultiplier(1f), player);
            updateTokens(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void breakSpeed(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {
        if(event.getEntityPlayer() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.getEntityPlayer();
            event.setNewSpeed(event.getOriginalSpeed()*(1f+getCapability(player).getBreakSpeedMultiplier()));
        }
    }

    @SubscribeEvent
    public static void blockBreak(BlockEvent.BreakEvent event) {
        if(!event.getWorld().isRemote && event.getPlayer() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.getPlayer();
            if(player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                getCapability(player).addMiningXP(getCapability(player).getSkillXpMultiplier(1f), player);
                updateTokens(player);
            }
        }
    }

    @SubscribeEvent
    public static void blockPlace(BlockEvent.PlaceEvent event) {
        if(!event.getWorld().isRemote && event.getPlayer() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.getPlayer();
            getCapability(player).addBuildingXP(getCapability(player).getSkillXpMultiplier(1f),player);
            updateTokens(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDamage(LivingDamageEvent event) {
        if(!event.getEntityLiving().world.isRemote) {
            if (event.getEntityLiving() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
                event.setAmount(Math.max(0f,event.getAmount()-getCapability(player).getDamageReduction()));
                getCapability(player).addDefenseXP(getCapability(player).getSkillXpMultiplier(Math.max(0f,(event.getAmount() / 2f))), player);
                updateTokens(player);
            } else if (!(event.getEntityLiving() instanceof EntityPlayer) && event.getSource().getTrueSource() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.getSource().getTrueSource();
                event.setAmount(event.getAmount()+getCapability(player).getDamageMultiplier());
                getCapability(player).addAttackXP(getCapability(player).getSkillXpMultiplier(Math.max(0f,(event.getAmount() / 2f))), player);
                updateTokens(player);
            }
        }
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.getEntityLiving();
            getCapability(player).addAgilityXP(getCapability(player).getSkillXpMultiplier(2f),player);
            updateTokens(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onHoe(UseHoeEvent event) {
        if(!event.getWorld().isRemote && event.getEntityPlayer() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.getEntityPlayer();
            if(event.getResult()==Event.Result.ALLOW)  {
                getCapability(player).addFarmingXP(getCapability(player).getSkillXpMultiplier(3f),player);
                updateTokens(player);
            }
        }
    }

    @SubscribeEvent
    public static void pickupXP(PlayerPickupXpEvent event) {
        if(event.getEntityPlayer() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.getEntityPlayer();
            getCapability(player).addMagicXP(getCapability(player).getSkillXpMultiplier(1f),player);
            updateTokens(player);
        }
    }

    @SubscribeEvent
    public static void onChangedDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.player;
            getCapability(player).addVoidXP(getCapability(player).getSkillXpMultiplier(5f),player);
            updateTokens(player);
        }
    }

    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        if(event.getEntityPlayer() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.getEntityPlayer();
            getCapability(player).addResearchXP(getCapability(player).getSkillXpMultiplier(5f),player);
            updateTokens(player);
        }
    }
}
