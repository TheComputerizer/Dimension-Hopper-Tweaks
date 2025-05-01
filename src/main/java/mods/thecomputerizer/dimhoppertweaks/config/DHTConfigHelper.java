package mods.thecomputerizer.dimhoppertweaks.config;

import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

import static mods.thecomputerizer.dimhoppertweaks.config.DHTConfig.MODS;
import static mods.thecomputerizer.dimhoppertweaks.config.DHTConfig.SKILLS;
import static mods.thecomputerizer.dimhoppertweaks.config.DHTConfig.TWEAKS;
import static mods.thecomputerizer.dimhoppertweaks.config.DHTConfig.WORLD;
import static org.apache.logging.log4j.Level.*;
import static ovh.corail.tombstone.config.ConfigTombstone.player_death;

public class DHTConfigHelper {

    private static final Logger DEV_LOGGER = LogManager.getLogger("DHT Dev");
    private static double maxArmorLinear = calculateMaxArmorLinear();
    private static double actualMaxArmorReduction = calculateActualMaxArmorReduction();
    private static double maxToughnessLinear = calculateMaxArmorLinear();
    private static double actualMaxToughnessReduction = calculateActualMaxToughnessReduction();
    
    public static double airTerminalEfficiency(int meta) {
        return MODS.airTerminalEfficiency*getLightningEfficiency(meta);
    }

    public static void adjustXPLossPercentage(boolean masochist) {
        player_death.xpLoss = masochist ? MODS.xpLossMasochist : MODS.xpLossNormal;
    }

    public static int auraInRadius(World world, BlockPos pos) {
        return IAuraChunk.getAuraInArea(world,pos,SKILLS.auraRadius);
    }

    private static double calculateActualMaxArmorReduction() {
        return (100d/TWEAKS.maxArmorPercentage)*TWEAKS.maxArmorReduction;
    }

    private static double calculateActualMaxToughnessReduction() {
        return (100d/TWEAKS.maxToughnessPercentage)*TWEAKS.maxToughnessReduction;
    }

    public static double calculateArmorPercentage(double armor) {
        armor = Math.min(armor, TWEAKS.maxArmor);
        if(armor<=maxArmorLinear)
            return (TWEAKS.armorPercentageFactor*(armor/maxArmorLinear))/100d;
        double factor = Math.log(TWEAKS.maxArmor/armor)/Math.log(2d);
        return (TWEAKS.maxArmorPercentage-(TWEAKS.armorPercentageFactor*factor))/100d;
    }

    private static double calculateMaxArmorLinear() {
        return TWEAKS.maxArmor/(Math.pow(2d,TWEAKS.maxArmorPercentage/TWEAKS.armorPercentageFactor-1d));
    }

    private static double calculateMaxToughnessLinear() {
        return TWEAKS.maxToughness/(Math.pow(2d,TWEAKS.maxToughnessPercentage/TWEAKS.toughnessPercentageFactor-2d));
    }

    public static double calculateToughnessPercentage(double toughness) {
        toughness = Math.min(toughness, TWEAKS.maxToughness);
        if(toughness<=maxToughnessLinear)
            return (TWEAKS.toughnessPercentageFactor*(toughness/maxToughnessLinear))/100d;
        double factor = Math.log(TWEAKS.maxToughness/toughness)/Math.log(2d);
        return (TWEAKS.maxToughnessPercentage-(TWEAKS.toughnessPercentageFactor*factor))/100d;
    }

    public static boolean canAddAura(int aura) {
        return aura<SKILLS.auraCap;
    }

    public static void devDebug(String msg, Object ... parameters) {
        devLog(DEBUG,msg,parameters);
    }
    
    @SuppressWarnings("unused")
    public static void devError(String msg, Object ... parameters) {
        devLog(ERROR,msg,parameters);
    }
    
    @SuppressWarnings("unused")
    public static void devFatal(String msg, Object ... parameters) {
        devLog(FATAL,msg,parameters);
    }

    public static void devInfo(String msg, Object ... parameters) {
        devLog(INFO,msg,parameters);
    }

    public static void devLog(Level level, String msg, Object ... parameters) {
        if(isDevLogEnabled()) DEV_LOGGER.log(level,msg,parameters);
    }
    
    @SuppressWarnings("unused")
    public static void devWarn(String msg, Object ... parameters) {
        devLog(WARN,msg,parameters);
    }

    public static int getAuraGains() {
        return SKILLS.auraGains;
    }

    public static double getLightningEfficiency(int meta) {
        switch(meta) {
            case 0: return MODS.lightningEfficiencyIron;
            case 1: return MODS.lightningEfficiencySteel;
            case 2: return MODS.lightningEfficiencyLead;
            case 3: return MODS.lightningEfficiencyTin;
            case 4: return MODS.lightningEfficiencyAluminum;
            case 5: return MODS.lightningEfficiencyGold;
            case 6: return MODS.lightningEfficiencyCopper;
            case 7: return MODS.lightningEfficiencyElectricium;
            case 8: return MODS.lightningEfficiencySkyfather;
            case 9: return MODS.lightningEfficiencyMystic;
            case 10: return MODS.lightningEfficiencyAwakenedDraconium;
            case 11: return MODS.lightningEfficiencyInsanium;
            case 12: return MODS.lightningEfficiencyUltimate;
            case 13: return MODS.lightningEfficiencyInfinity;
            default: return 0.01d;
        }
    }

    public static double getMaxArmorPercentage() {
        return TWEAKS.maxArmorPercentage/100d;
    }

    public static double getMaxArmorReduction() {
        return actualMaxArmorReduction;
    }

    public static double getMaxToughnessPercentage() {
        return TWEAKS.maxToughnessPercentage/100d;
    }

    public static double getMaxToughnessReduction() {
        return actualMaxToughnessReduction;
    }

    public static boolean isDevLogEnabled() {
        return DHTConfig.DEBUG.devLog;
    }

    public static void onConfigReloaded() {
        recalculateArmor();
    }

    private static void recalculateArmor() {
        maxArmorLinear = calculateMaxArmorLinear();
        actualMaxArmorReduction = calculateActualMaxArmorReduction();
        maxToughnessLinear = calculateMaxToughnessLinear();
        actualMaxToughnessReduction = calculateActualMaxToughnessReduction();
    }

    public static boolean shouldReplaceChest(Random rand) {
        return rand.nextFloat()<=WORLD.chestReplacementChance;
    }
}
