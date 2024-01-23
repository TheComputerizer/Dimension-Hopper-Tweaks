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
    @Unique private static final double dimhoppertweaks$maxArmorLinear = dimhoppertweaks$maxArmor/(Math.pow(2d,dimhoppertweaks$maxArmorPercent/dimhoppertweaks$armorPercentFactor)-1d);
    @Unique private static final double dimhoppertweaks$maxToughness = 50d;
    @Unique private static final double dimhoppertweaks$maxToughnessPercent = 50d;
    @Unique private static final double dimhoppertweaks$toughnessPercentFactor = 10d;
    @Unique private static final double dimhoppertweaks$maxToughnessLinear = dimhoppertweaks$maxToughness/(Math.pow(2d,dimhoppertweaks$maxToughnessPercent/dimhoppertweaks$toughnessPercentFactor)-2d);

    /**
     * @author The_Computerizer
     * @reason Custom armor curve shenanigans
     */
    @Overwrite
    public static float getDamageAfterAbsorb(float damage, float armor, float toughness) {
        double reduction = 0d;
        double armorPercent = 0d;
        if(armor>0f) {
            armorPercent = Math.min(dimhoppertweaks$maxArmorPercent,dimhoppertweaks$calculateArmorPercent(armor))/100d;
            double maxReduction = Math.min(50d,((dimhoppertweaks$maxArmorPercent/100d)*50d)*armorPercent);
            reduction = Math.min(damage*armorPercent,maxReduction);
        }
        if(toughness>0f) {
            double toughnessPercent = Math.min(dimhoppertweaks$maxToughnessPercent,dimhoppertweaks$calculateToughnessPercent(toughness))/100d;
            toughnessPercent = (1d-armorPercent)*toughnessPercent;
            double maxReduction = Math.min(10d,((dimhoppertweaks$maxToughnessPercent/100d)*20d)*toughnessPercent);
            reduction+=(Math.min(damage*toughnessPercent,maxReduction));
        }
        return (float)Math.max(0d,damage-reduction);
    }

    @Unique
    private static double dimhoppertweaks$calculateArmorPercent(double armor) {
        armor = Math.min(armor,dimhoppertweaks$maxArmor);
        if(armor<=dimhoppertweaks$maxArmorLinear)
            return dimhoppertweaks$armorPercentFactor*(armor/dimhoppertweaks$maxArmorLinear);
        double factor = Math.log(dimhoppertweaks$maxArmor/armor)/Math.log(2d);
        return dimhoppertweaks$maxArmorPercent-(dimhoppertweaks$armorPercentFactor*factor);
    }

    @Unique
    private static double dimhoppertweaks$calculateToughnessPercent(double toughness) {
        toughness = Math.min(toughness,dimhoppertweaks$maxToughness);
        if(toughness<=dimhoppertweaks$maxToughnessLinear)
            return dimhoppertweaks$toughnessPercentFactor*(toughness/dimhoppertweaks$maxToughnessLinear);
        double factor = Math.log(dimhoppertweaks$maxToughness/toughness)/Math.log(2d);
        return dimhoppertweaks$maxToughnessPercent-(dimhoppertweaks$toughnessPercentFactor*factor);
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