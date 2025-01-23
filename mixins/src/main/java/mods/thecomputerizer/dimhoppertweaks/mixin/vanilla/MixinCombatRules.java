package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.config.DHTConfigHelper;
import net.minecraft.util.CombatRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CombatRules.class)
public abstract class MixinCombatRules {

    /**
     * @author The_Computerizer
     * @reason Custom armor curve shenanigans
     */
    @Overwrite
    public static float getDamageAfterAbsorb(float damage, float armor, float toughness) {
        double reduction = 0d;
        double armorPercent = 0d;
        boolean calculated = false;
        if(armor>0f) {
            double maxPercent = DHTConfigHelper.getMaxArmorPercentage(); //decimal percentage
            armorPercent = Math.min(maxPercent,DHTConfigHelper.calculateArmorPercentage(armor)); //decimal percentage
            double maxReduction = Math.min(DHTConfigHelper.getMaxArmorReduction()*maxPercent,DHTConfigHelper.getMaxArmorReduction()*armorPercent);
            reduction = Math.min(damage*armorPercent,maxReduction);
            calculated = true;
            DHTConfigHelper.devInfo("COMBAT RULES: `BASE ARMOR {} | ARMOR PERCENTAGE {} | "+
                    "MAX ARMOR DAMAGE REDUCTION {} | ACTUAL ARMOR DAMAGE REDUCTION {}`",armor,armorPercent,maxReduction,
                    reduction);
        }
        if(toughness>0f) {
            double maxPercent = DHTConfigHelper.getMaxToughnessPercentage(); //decimal percentage
            double toughnessPercent = Math.min(maxPercent,DHTConfigHelper.calculateToughnessPercentage(toughness)); //decimal percentage
            double maxReduction = Math.min(DHTConfigHelper.getMaxToughnessReduction()*maxPercent,DHTConfigHelper.getMaxToughnessReduction()*toughnessPercent);
            toughnessPercent = (1d-armorPercent)*toughnessPercent;
            double toughnessReduction = Math.min(damage*toughnessPercent,maxReduction);
            reduction+=toughnessReduction;
            calculated = true;
            DHTConfigHelper.devInfo("COMBAT RULES: `BASE TOUGHNESS {} | TOUGHNESS PERCENTAGE {} | "+
                            "MAX TOUGHNESS DAMAGE REDUCTION {} | ACTUAL TOUGHNESS DAMAGE REDUCTION {}`",toughness,
                    toughnessPercent,maxReduction,toughnessReduction);
        }
        float newDamage = (float)Math.max(0d,damage-reduction);
        if(calculated)
            DHTConfigHelper.devInfo("COMBAT RULES: `BASE DAMAGE {} | TOTAL REDUCTION {} | ACTUAL DAMAGE {}`",
                    damage,reduction,newDamage);
        return newDamage;
    }

    /**
     * @author The_Computerizer
     * @reason Custom armor curve shenanigans
     */
    @Overwrite
    public static float getDamageAfterMagicAbsorb(float damage, float enchant) {
        return damage*(10f/(10f+Math.max(enchant,0f)));
    }
}