package mods.thecomputerizer.dimhoppertweaks.mixin.mods.openblocks;

import info.openmods.calc.ExprType;
import info.openmods.calc.SingleExprEvaluator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import openblocks.enchantments.LastStandEnchantmentsHandler;
import openmods.utils.EnchantmentUtils;
import org.spongepowered.asm.mixin.*;

@Mixin(value = LastStandEnchantmentsHandler.class, remap = false)
public abstract class MixinLastStandEnchantmentsHandler {

    @Shadow
    public static int countLastStandEnchantmentLevels(EntityLivingBase living) {
        return 0;
    }

    @Shadow @Final private SingleExprEvaluator<Double,ExprType> reductionCalculator;

    private static final float BASE_XP_COST = 200f;
    private static final float XP_SCALING_FACTOR = 1.2f;

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
            if (enchantmentLevels > 0) {
                float playerHealth = player.getHealth();
                float healthAvailable = playerHealth - e.getAmount();
                if (healthAvailable < 1f) {
                    int xpAvailable = EnchantmentUtils.getPlayerXP(player);
                    float xpRequired = this.reductionCalculator.evaluate(env -> {
                        env.setGlobalSymbol("ench", (double) enchantmentLevels);
                        env.setGlobalSymbol("xp", (double) xpAvailable);
                        env.setGlobalSymbol("hp", (double) playerHealth);
                        env.setGlobalSymbol("dmg", (double) e.getAmount());
                    }, () -> {
                        float damage = 1f - healthAvailable;
                        float baseCost = BASE_XP_COST / enchantmentLevels;
                        float xp = baseCost * (float) Math.pow(XP_SCALING_FACTOR, damage);
                        xp = Math.max(1f, xp);
                        return (double) xp;
                    }).floatValue();
                    if ((float) xpAvailable >= xpRequired) {
                        player.setHealth(1f);
                        EnchantmentUtils.addPlayerXP(player, -((int) xpRequired));
                        e.setAmount(0);
                        e.setCanceled(true);
                    }
                }
            }
        }
    }
}