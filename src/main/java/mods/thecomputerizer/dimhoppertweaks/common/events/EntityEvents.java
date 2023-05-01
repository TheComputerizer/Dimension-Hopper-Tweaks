package mods.thecomputerizer.dimhoppertweaks.common.events;

import codersafterdark.reskillable.api.event.LevelUpEvent;
import com.google.common.collect.Iterables;
import mods.thecomputerizer.dimhoppertweaks.Constants;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.EntityPixieAccess;
import morph.avaritia.util.DamageSourceInfinitySword;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumSword;

import java.util.Objects;

@SuppressWarnings("SpellCheckingInspection")
@Mod.EventBusSubscriber(modid = Constants.MODID)
public class EntityEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingAttack(LivingAttackEvent event) {
        if(event.getEntityLiving() instanceof EntityFinalBoss) {
            if(!(event.getSource() instanceof DamageSourceInfinitySword))
                event.setCanceled(true);
        }
        if(event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.getEntityLiving();
            if(!player.isEntityInvulnerable(event.getSource()) && player.canBlockDamageSource(event.getSource()))
                SkillWrapper.getSkillCapability(player).setShieldedDamage(event.getAmount());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        if(event.getSource()!=DamageSource.OUT_OF_WORLD) {
            if(event.getEntityLiving() instanceof EntityFinalBoss) {
                if(!(event.getSource() instanceof DamageSourceInfinitySword))
                    event.setCanceled(true);
            }
            else if(event.getEntityLiving() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP)event.getEntityLiving();
                float armor = ISpecialArmor.ArmorProperties.applyArmor(player, player.inventory.armorInventory, event.getSource(), event.getAmount());
                if (armor > 0) {
                    float amount = Math.min(Math.max(0f, ((event.getAmount() - armor) / 4f)),5f);
                    SkillWrapper.addSP(player,"defense",amount,false);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.getEntityLiving();
            int jumpFactor = player.isPotionActive(MobEffects.JUMP_BOOST) ?
                    Objects.requireNonNull(player.getActivePotionEffect(MobEffects.JUMP_BOOST)).getAmplifier()+3 : 2;
            SkillWrapper.addSP((EntityPlayerMP) event.getEntityLiving(), "agility", jumpFactor, false);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDamage(LivingDamageEvent event) {
        if(!event.getEntityLiving().world.isRemote && event.getSource() != DamageSource.OUT_OF_WORLD) {
            if (event.getEntityLiving() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
                event.setAmount(Math.max(0f,event.getAmount()-SkillWrapper.getSkillCapability(player).getDamageReduction()));
            } else if (event.getSource().getTrueSource() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.getSource().getTrueSource();
                float amount = event.getAmount()+SkillWrapper.getSkillCapability(player).getDamageMultiplier();
                event.setAmount(amount);
                SkillWrapper.addSP(player,"attack",Math.max(0.5f,(event.getAmount() / 2f)),false);
                if(amount>=50f && player.getHeldItemMainhand().getItem() instanceof ItemElementiumSword) {
                    EntityPixie pixie = new EntityPixie(player.getServerWorld());
                    ((EntityPixieAccess)pixie).setBypassesTarget(true);
                    pixie.setPosition(player.posX, player.posY + 2.0, player.posZ);
                    pixie.onInitialSpawn(player.getServerWorld().getDifficultyForLocation(new BlockPos(pixie)), null);
                    player.getServerWorld().spawnEntity(pixie);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDamage(LivingDeathEvent event) {
        if(event.getSource().getTrueSource() instanceof EntityPlayerMP) {
            NonNullList<ItemStack> armorList = NonNullList.from(ItemStack.EMPTY,
                    Iterables.toArray(event.getEntityLiving().getArmorInventoryList(),ItemStack.class));
            float armor = 10f-ISpecialArmor.ArmorProperties.applyArmor(
                    event.getEntityLiving(),armorList, event.getSource(),10f);
            SkillWrapper.addSP((EntityPlayerMP)event.getSource().getTrueSource(),"defense",armor,false);
        }
    }

    @Mod.EventBusSubscriber(modid = Constants.MODID)
    public static class PlayerEvents {

        private static int TICK_DELAY = 0;

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onChangedDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {
            if(event.player instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP)event.player;
                if(event.toDim!=7 && GameStageHelper.hasStage(player,"twilight"))
                    GameStageHelper.removeStage(player,"twilight");
                if(event.toDim==-1 && !GameStageHelper.hasStage(player,"nether"))
                    GameStageHelper.addStage(player,"nether");
                if(event.toDim!=-19 && GameStageHelper.hasStage(player,"finalfrontier"))
                    GameStageHelper.addStage(player,"finalfrontier");
                SkillWrapper.addSP(player,"void",5f,false);
            }
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onLevelUp(LevelUpEvent.Post event) {
            if(event.getEntityPlayer() instanceof EntityPlayerMP) SkillWrapper.updateTokens((EntityPlayerMP)event.getEntityPlayer());
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            if(event.player instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP)event.player;
                int dim = player.dimension;
                if(dim!=7 && dim!=20 && dim!=684 && !GameStageHelper.hasStage(player,"bridgeone"))
                    GameStageHelper.addStage(player,"bridgeone");
                if(!GameStageHelper.hasStage(player,"bedrockFinal"))
                    player.inventory.clearMatchingItems(Item.getItemFromBlock(Blocks.BEDROCK),-1,0,null);
                SkillWrapper.updateTokens(player);
            }
        }

        @SuppressWarnings("ConstantValue")
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            if(event.player instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.player;
                BlockPos pos = SkillWrapper.getSkillCapability(player).getTwilightRespawn();
                if(player.dimension==7 && Objects.isNull(player.getBedLocation(7)) && Objects.nonNull(pos))
                    player.setSpawnPoint(pos,true);
            }
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void pickupXP(PlayerPickupXpEvent event) {
            if(event.getEntityPlayer() instanceof EntityPlayerMP) {
                int factor = event.getOrb().xpValue>1 ? (int) (Math.log(event.getOrb().xpValue) / Math.log(2)) : 1;
                SkillWrapper.addSP((EntityPlayerMP) event.getEntityPlayer(), "magic", factor, false);
            }
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onAdvancement(AdvancementEvent event) {
            if(event.getEntityPlayer() instanceof EntityPlayerMP)
                SkillWrapper.addSP((EntityPlayerMP)event.getEntityPlayer(),"research",5f,false);
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void playerTick(TickEvent.PlayerTickEvent event) {
            if(event.phase== TickEvent.Phase.END) {
                if (event.side == Side.SERVER) {
                    TICK_DELAY++;
                    if (TICK_DELAY >= 20) {
                        EntityPlayerMP player = (EntityPlayerMP) event.player;
                        if (player.isSprinting()) {
                            int speedFactor = player.isPotionActive(MobEffects.SPEED) ? Objects.requireNonNull(
                                    player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 2 : 1;
                            SkillWrapper.addSP(player, "agility", speedFactor, false);
                        }
                        SkillWrapper.getSkillCapability(player).decrementGatheringItems(20);
                        TICK_DELAY = 0;
                    }
                }
            }
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void pickUpItem(PlayerEvent.ItemPickupEvent event) {
            if(event.player instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP)event.player;
                ItemStack stack = event.getStack();
                if(!GameStageHelper.hasStage(player,"bedrockFinal") &&
                        stack.getItem()==Item.getItemFromBlock(Blocks.BEDROCK))
                    stack.setCount(0);
                if(stack.getCount()>0) {
                    if(SkillWrapper.getSkillCapability(player).checkGatheringItem(stack.getItem())) {
                        int sizeFactor = stack.getCount() > 1 ? (int) (Math.log(stack.getCount()) / Math.log(2)) : 1;
                        SkillWrapper.addSP(player, "gathering", sizeFactor, false);
                    }
                }
            }
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void breakSpeed(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {
            if(event.getEntityPlayer() instanceof EntityPlayerMP)
                event.setNewSpeed(event.getOriginalSpeed()*(1f+ SkillWrapper.getSkillCapability(event.getEntityPlayer())
                        .getBreakSpeedMultiplier()));
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onHoe(UseHoeEvent event) {
            if(event.getEntityPlayer() instanceof EntityPlayerMP)
                SkillWrapper.addSP((EntityPlayerMP)event.getEntityPlayer(),"farming",3f,false);
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
            if(event.getEntityPlayer() instanceof EntityPlayerMP) {
                EntityPlayerMP to = (EntityPlayerMP) event.getEntityPlayer();
                EntityPlayerMP from = (EntityPlayerMP) event.getOriginal();
                SkillWrapper.getSkillCapability(to).of((SkillCapability) SkillWrapper.getSkillCapability(from),to);
            }
        }
    }
}
