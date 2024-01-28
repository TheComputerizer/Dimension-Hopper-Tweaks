package mods.thecomputerizer.dimhoppertweaks.mixin.mods.openblocks;

import info.openmods.calc.ExprType;
import info.openmods.calc.SingleExprEvaluator;
import mods.thecomputerizer.dimhoppertweaks.config.DHTConfigHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import openblocks.enchantments.LastStandEnchantmentsHandler;
import openmods.utils.EnchantmentUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Mixin(value = LastStandEnchantmentsHandler.class, remap = false)
public abstract class MixinLastStandEnchantmentsHandler {

    @Shadow
    public static int countLastStandEnchantmentLevels(EntityLivingBase living) {
        return 0;
    }

    @Shadow @Final private SingleExprEvaluator<Double,ExprType> reductionCalculator;

    @Unique private final Map<EntityPlayer,MutableInt> dimhoppertweaks$playerTicker = Collections.synchronizedMap(new HashMap<>());

    /**
     * @author The_Computerizer
     * @reason Nerf last stand
     */
    @Overwrite
    @SubscribeEvent
    public void onHurt(LivingHurtEvent e) {
        if(e.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)e.getEntityLiving();
            int enchantmentLevels = countLastStandEnchantmentLevels(player);
            if(enchantmentLevels>0) {
                synchronized(this.dimhoppertweaks$playerTicker) {
                    this.dimhoppertweaks$playerTicker.putIfAbsent(player, new MutableInt());
                    float playerHealth = player.getHealth();
                    float healthAvailable = playerHealth-e.getAmount();
                    if(this.dimhoppertweaks$playerTicker.get(player).intValue()<=0 && healthAvailable<1f) {
                        int time = DHTConfigHelper.getLastStandCooldown(enchantmentLevels);
                        int xpAvailable = EnchantmentUtils.getPlayerXP(player);
                        float xpRequired = this.reductionCalculator.evaluate(env -> {
                            env.setGlobalSymbol("ench",(double)enchantmentLevels);
                            env.setGlobalSymbol("xp",(double)xpAvailable);
                            env.setGlobalSymbol("hp",(double)playerHealth);
                            env.setGlobalSymbol("dmg",(double)e.getAmount());
                        },() -> {
                            float xp = 1f-healthAvailable;
                            xp*=200f;
                            xp/=(float)enchantmentLevels;
                            xp = Math.max(1f,xp);
                            return (double)xp;
                        }).floatValue();
                        if((float)xpAvailable>=xpRequired) {
                            player.setHealth(1f);
                            EnchantmentUtils.addPlayerXP(player,-((int)xpRequired));
                            e.setAmount(0);
                            this.dimhoppertweaks$playerTicker.get(player).setValue(time);
                            e.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    @Unique
    @SubscribeEvent
    public void dimhoppertweaks$onServerTick(TickEvent.ServerTickEvent e) {
        if(e.phase==TickEvent.Phase.END) {
            synchronized(this.dimhoppertweaks$playerTicker) {
                for(MutableInt timer : this.dimhoppertweaks$playerTicker.values())
                    if(timer.intValue()>0) timer.decrement();
            }
        }
    }
}