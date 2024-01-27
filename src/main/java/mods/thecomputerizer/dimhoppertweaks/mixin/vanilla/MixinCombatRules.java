package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.util.CombatRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CombatRules.class)
public abstract class MixinCombatRules {

    @Unique private static final double dimhoppertweaks$maxArmor = 200d;
    @Unique private static final double dimhoppertweaks$maxArmorPercent = 90d;
    @Unique private static final double dimhoppertweaks$armorPercentFactor = 15d;
    @Unique private static final double dimhoppertweaks$maxArmorLinear = dimhoppertweaks$maxArmor/(Math.pow(2d,dimhoppertweaks$maxArmorPercent/dimhoppertweaks$armorPercentFactor-1d));
    @Unique private static final double dimhoppertweaks$maxArmorReduction = (100d/dimhoppertweaks$maxArmorPercent)*50d;
    @Unique private static final double dimhoppertweaks$maxToughness = 50d;
    @Unique private static final double dimhoppertweaks$maxToughnessPercent = 50d;
    @Unique private static final double dimhoppertweaks$toughnessPercentFactor = 10d;
    @Unique private static final double dimhoppertweaks$maxToughnessLinear = dimhoppertweaks$maxToughness/(Math.pow(2d,dimhoppertweaks$maxToughnessPercent/dimhoppertweaks$toughnessPercentFactor-2d));
    @Unique private static final double dimhoppertweaks$maxToughnessReduction = (100d/dimhoppertweaks$maxToughnessPercent)*25d;

    /**
     * @author The_Computerizer
     * @reason Custom armor curve shenanigans
     */
    @Overwrite
    public static float getDamageAfterAbsorb(float damage, float armor, float toughness) {
        double reduction = 0d;
        double armorPercent = 0d;
        if(armor>0f) {
            double maxPercent = dimhoppertweaks$maxArmorPercent/100d; //decimal percentage
            armorPercent = Math.min(maxPercent,dimhoppertweaks$calculateArmorPercent(armor)); //decimal percentage
            double maxReduction = Math.min(dimhoppertweaks$maxArmorReduction*maxPercent,dimhoppertweaks$maxArmorReduction*armorPercent);
            reduction = Math.min(damage*armorPercent,maxReduction);
        }
        if(toughness>0f) {
            double maxPercent = dimhoppertweaks$maxToughnessPercent/100d; //decimal percentage
            double toughnessPercent = Math.min(maxPercent,dimhoppertweaks$calculateToughnessPercent(toughness)); //decimal percentage
            double maxReduction = Math.min(dimhoppertweaks$maxToughnessReduction*maxPercent,dimhoppertweaks$maxToughnessReduction*toughnessPercent);
            toughnessPercent = (1d-armorPercent)*toughnessPercent;
            reduction+=(Math.min(damage*toughnessPercent,maxReduction));
        }
        return (float)Math.max(0d,damage-reduction);
    }

    /**
     * Returns a decimal percentage
     */
    @Unique
    private static double dimhoppertweaks$calculateArmorPercent(double armor) {
        armor = Math.min(armor,dimhoppertweaks$maxArmor);
        if(armor<=dimhoppertweaks$maxArmorLinear)
            return (dimhoppertweaks$armorPercentFactor*(armor/dimhoppertweaks$maxArmorLinear))/100d;
        double factor = Math.log(dimhoppertweaks$maxArmor/armor)/Math.log(2d);
        return (dimhoppertweaks$maxArmorPercent-(dimhoppertweaks$armorPercentFactor*factor))/100d;
    }

    /**
     * Returns a decimal percentage
     */
    @Unique
    private static double dimhoppertweaks$calculateToughnessPercent(double toughness) {
        toughness = Math.min(toughness,dimhoppertweaks$maxToughness);
        if(toughness<=dimhoppertweaks$maxToughnessLinear)
            return ((dimhoppertweaks$toughnessPercentFactor*2d)*(toughness/dimhoppertweaks$maxToughnessLinear))/100d;
        double factor = Math.log(dimhoppertweaks$maxToughness/toughness)/Math.log(2d);
        return (dimhoppertweaks$maxToughnessPercent-(dimhoppertweaks$toughnessPercentFactor*factor))/100d;
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