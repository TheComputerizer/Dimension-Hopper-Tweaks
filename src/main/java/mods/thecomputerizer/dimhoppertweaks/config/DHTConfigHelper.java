package mods.thecomputerizer.dimhoppertweaks.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@SuppressWarnings("unused")
public class DHTConfigHelper {

    private static final Logger DEV_LOGGER = LogManager.getLogger("DHT Dev");

    private static double maxArmorLinear = calculateMaxArmorLinear();
    private static double actualMaxArmorReduction = calculateActualMaxArmorReduction();
    private static double maxToughnessLinear = calculateMaxArmorLinear();
    private static double actualMaxToughnessReduction = calculateActualMaxToughnessReduction();

    private static double calculateActualMaxArmorReduction() {
        DHTConfig.Tweaks tweaks = DHTConfig.TWEAKS;
        return (100d/tweaks.maxArmorPercentage)*tweaks.maxArmorReduction;
    }

    private static double calculateActualMaxToughnessReduction() {
        DHTConfig.Tweaks tweaks = DHTConfig.TWEAKS;
        return (100d/tweaks.maxToughnessPercentage)*tweaks.maxToughnessReduction;
    }

    public static double calculateArmorPercentage(double armor) {
        DHTConfig.Tweaks tweaks = DHTConfig.TWEAKS;
        armor = Math.min(armor,tweaks.maxArmor);
        if(armor<=maxArmorLinear)
            return (tweaks.armorPercentageFactor*(armor/maxArmorLinear))/100d;
        double factor = Math.log(tweaks.maxArmor/armor)/Math.log(2d);
        return (tweaks.maxArmorPercentage-(tweaks.armorPercentageFactor*factor))/100d;
    }

    private static double calculateMaxArmorLinear() {
        DHTConfig.Tweaks tweaks = DHTConfig.TWEAKS;
        return tweaks.maxArmor/(Math.pow(2d,tweaks.maxArmorPercentage/tweaks.armorPercentageFactor-1d));
    }

    private static double calculateMaxToughnessLinear() {
        DHTConfig.Tweaks tweaks = DHTConfig.TWEAKS;
        return tweaks.maxToughness/(Math.pow(2d,tweaks.maxToughnessPercentage/tweaks.toughnessPercentageFactor-2d));
    }

    public static double calculateToughnessPercentage(double toughness) {
        DHTConfig.Tweaks tweaks = DHTConfig.TWEAKS;
        toughness = Math.min(toughness,tweaks.maxToughness);
        if(toughness<=maxToughnessLinear)
            return (tweaks.toughnessPercentageFactor*(toughness/maxToughnessLinear))/100d;
        double factor = Math.log(tweaks.maxToughness/toughness)/Math.log(2d);
        return (tweaks.maxToughnessPercentage-(tweaks.toughnessPercentageFactor*factor))/100d;
    }

    public static void devDebug(String msg, Object ... parameters) {
        devLog(Level.DEBUG,msg,parameters);
    }

    public static void devError(String msg, Object ... parameters) {
        devLog(Level.ERROR,msg,parameters);
    }

    public static void devFatal(String msg, Object ... parameters) {
        devLog(Level.FATAL,msg,parameters);
    }

    public static void devInfo(String msg, Object ... parameters) {
        devLog(Level.INFO,msg,parameters);
    }

    public static void devLog(Level level, String msg, Object ... parameters) {
        if(isDevLogEnabled()) DEV_LOGGER.log(level,msg,parameters);
    }

    public static void devWarn(String msg, Object ... parameters) {
        devLog(Level.WARN,msg,parameters);
    }

    public static int getAuraCap() {
        return DHTConfig.SKILLS.auraCap;
    }

    public static int getAuraGains() {
        return DHTConfig.SKILLS.auraGains;
    }

    public static int getLastStandCooldown(int level) {
        return Math.max(2,6-DHTConfig.MODS.lastStandCooldown);
    }

    public static double getMaxArmorPercentage() {
        return DHTConfig.TWEAKS.maxArmorPercentage/100d;
    }

    public static double getMaxArmorReduction() {
        return actualMaxArmorReduction;
    }

    public static double getMaxToughnessPercentage() {
        return DHTConfig.TWEAKS.maxToughnessPercentage/100d;
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
        return rand.nextFloat()<=DHTConfig.WORLD.chestReplacementChance;
    }
}
