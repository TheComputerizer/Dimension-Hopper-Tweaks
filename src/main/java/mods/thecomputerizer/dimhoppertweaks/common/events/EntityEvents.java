package mods.thecomputerizer.dimhoppertweaks.common.events;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import com.google.common.collect.Iterables;
import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.common.skills.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.EntityPixieAccess;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.util.WorldUtil;
import morph.avaritia.util.DamageSourceInfinitySword;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.scalinghealth.event.BlightHandler;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumSword;

import java.util.Objects;

@SuppressWarnings("SpellCheckingInspection")
@Mod.EventBusSubscriber(modid = Constants.MODID)
public class EntityEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingAttack(LivingAttackEvent event) {
        if(event.isCanceled()) return;
        if(event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.getEntityLiving();
            if(!player.isEntityInvulnerable(event.getSource()) && player.canBlockDamageSource(event.getSource())) {
                ISkillCapability cap = SkillWrapper.getSkillCapability(player);
                if(Objects.nonNull(cap)) cap.setShieldedDamage(event.getAmount());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        if(event.isCanceled()) return;
        if(event.getSource()!=DamageSource.OUT_OF_WORLD) {
            if(event.getEntityLiving() instanceof EntityFinalBoss) {
                if(!(event.getSource() instanceof DamageSourceInfinitySword))
                    event.setCanceled(true);
            } else if(event.getEntityLiving() instanceof EntityTameable) {
                EntityTameable tameable = (EntityTameable)event.getEntityLiving();
                if(!tameable.world.isRemote && tameable.isTamed()) {
                    EntityLivingBase owner = tameable.getOwner();
                    if(owner instanceof EntityPlayer) {
                        PlayerData data = PlayerDataHandler.get((EntityPlayer)owner);
                        if(Objects.nonNull(data)) {
                            SkillWrapper.executeOnSkills(data, h -> {
                                if(h instanceof ExtendedEventsTrait) ((ExtendedEventsTrait)h).onTamedHurt(event);
                            });
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if(event.isCanceled()) return;
        if(event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.getEntityLiving();
            int jumpFactor = player.isPotionActive(MobEffects.JUMP_BOOST) ?
                    Objects.requireNonNull(player.getActivePotionEffect(MobEffects.JUMP_BOOST)).getAmplifier()+2 : 1;
            SkillWrapper.addSP((EntityPlayerMP)event.getEntityLiving(),"agility",jumpFactor,false);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDamage(LivingDamageEvent event) {
        if(event.isCanceled()) return;
        if(!event.getEntityLiving().world.isRemote && Objects.nonNull(event.getSource()) && event.getSource()!=DamageSource.OUT_OF_WORLD) {
            if(event.getEntityLiving() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
                if(Objects.nonNull(player)) {
                    ISkillCapability cap = SkillWrapper.getSkillCapability(player);
                    if(Objects.nonNull(cap)) event.setAmount(Math.max(0f,event.getAmount()-cap.getDamageReduction()));
                }
            } else if(event.getSource().getTrueSource() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.getSource().getTrueSource();
                if(Objects.nonNull(player)) {
                    ISkillCapability cap = SkillWrapper.getSkillCapability(player);
                    float amount = event.getAmount();
                    if(Objects.nonNull(cap)) {
                        amount+=cap.getDamageMultiplier();
                        event.setAmount(amount);
                        SkillWrapper.addSP(player,"attack",Math.max(0.5f,(amount/2f)),false);
                    }
                    if(amount>=50f && player.getHeldItemMainhand().getItem() instanceof ItemElementiumSword) {
                        EntityPixie pixie = new EntityPixie(player.getServerWorld());
                        ((EntityPixieAccess) pixie).setBypassesTarget(true);
                        pixie.setPosition(player.posX,player.posY+2d,player.posZ);
                        pixie.onInitialSpawn(player.getServerWorld().getDifficultyForLocation(new BlockPos(pixie)),null);
                        player.getServerWorld().spawnEntity(pixie);
                    }
                }
            } else if(event.getSource().getTrueSource() instanceof EntityTameable) {
                EntityTameable tameable = (EntityTameable)event.getSource().getTrueSource();
                if(!tameable.world.isRemote && tameable.isTamed()) {
                    EntityLivingBase owner = tameable.getOwner();
                    if(owner instanceof EntityPlayer) {
                        PlayerData data = PlayerDataHandler.get((EntityPlayer)owner);
                        if(Objects.nonNull(data)) {
                            SkillWrapper.executeOnSkills(data, h -> {
                                if(h instanceof ExtendedEventsTrait) ((ExtendedEventsTrait)h).onTamedDamageOther(event);
                            });
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingKnockBack(LivingKnockBackEvent event) {
        if(event.isCanceled()) return;
        EntityLivingBase entity = event.getEntityLiving();
        if(!entity.world.isRemote && entity instanceof EntityPlayer) {
            PlayerData data = PlayerDataHandler.get((EntityPlayer) entity);
            if(Objects.nonNull(data)) {
                SkillWrapper.executeOnSkills(data, h -> {
                    if(h instanceof ExtendedEventsTrait) ((ExtendedEventsTrait) h).onLivingKnockback(event);
                });
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDeath(LivingDeathEvent event) {
        if(event.isCanceled()) return;
        if(event.getSource().getTrueSource() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.getSource().getTrueSource();
            if(Objects.nonNull(player) && Objects.nonNull(SkillWrapper.getSkillCapability(player))) {
                NonNullList<ItemStack> armorList = NonNullList.from(ItemStack.EMPTY,
                        Iterables.toArray(event.getEntityLiving().getArmorInventoryList(),ItemStack.class));
                float armor = (25f-ISpecialArmor.ArmorProperties.applyArmor(
                        event.getEntityLiving(),armorList,event.getSource(),25f))*2f;
                SkillWrapper.addSP(player,"defense",armor,false);
            }
        } else {
            EntityLivingBase entity = event.getEntityLiving();
            if(entity.world.isRemote && BlightHandler.isBlight(entity))
                for(int i=0; i<MathHelper.clamp(entity.getMaxHealth()/500f,5f,64f); i++)
                    WorldUtil.spawnBlightParticle(entity.world,entity.posX,entity.getEntityBoundingBox().minY,
                            entity.posZ,entity.width,entity.height);
        }
    }
}
