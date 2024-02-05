package mods.thecomputerizer.dimhoppertweaks.config;

import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ovh.corail.tombstone.config.ConfigTombstone;

import java.util.Random;

@SuppressWarnings("unused")
public class DHTConfigHelper {

    private static final Logger DEV_LOGGER = LogManager.getLogger("DHT Dev");
    private static double maxArmorLinear = calculateMaxArmorLinear();
    private static double actualMaxArmorReduction = calculateActualMaxArmorReduction();
    private static double maxToughnessLinear = calculateMaxArmorLinear();
    private static double actualMaxToughnessReduction = calculateActualMaxToughnessReduction();

    public static void adjustXPLossPercentage(boolean masochist) {
        ConfigTombstone.player_death.xpLoss = masochist ? DHTConfig.MODS.xpLossMasochist : DHTConfig.MODS.xpLossNormal;
    }

    public static int auraInRadius(World world, BlockPos pos) {
        return IAuraChunk.getAuraInArea(world,pos,DHTConfig.SKILLS.auraRadius);
    }

    private static double calculateActualMaxArmorReduction() {
        return (100d/DHTConfig.TWEAKS.maxArmorPercentage)*DHTConfig.TWEAKS.maxArmorReduction;
    }

    private static double calculateActualMaxToughnessReduction() {
        return (100d/DHTConfig.TWEAKS.maxToughnessPercentage)*DHTConfig.TWEAKS.maxToughnessReduction;
    }

    public static double calculateArmorPercentage(double armor) {
        armor = Math.min(armor,DHTConfig.TWEAKS.maxArmor);
        if(armor<=maxArmorLinear)
            return (DHTConfig.TWEAKS.armorPercentageFactor*(armor/maxArmorLinear))/100d;
        double factor = Math.log(DHTConfig.TWEAKS.maxArmor/armor)/Math.log(2d);
        return (DHTConfig.TWEAKS.maxArmorPercentage-(DHTConfig.TWEAKS.armorPercentageFactor*factor))/100d;
    }

    private static double calculateMaxArmorLinear() {
        return DHTConfig.TWEAKS.maxArmor/(Math.pow(2d,DHTConfig.TWEAKS.maxArmorPercentage/DHTConfig.TWEAKS.armorPercentageFactor-1d));
    }

    private static double calculateMaxToughnessLinear() {
        return DHTConfig.TWEAKS.maxToughness/(Math.pow(2d,DHTConfig.TWEAKS.maxToughnessPercentage/DHTConfig.TWEAKS.toughnessPercentageFactor-2d));
    }

    public static double calculateToughnessPercentage(double toughness) {
        toughness = Math.min(toughness,DHTConfig.TWEAKS.maxToughness);
        if(toughness<=maxToughnessLinear)
            return (DHTConfig.TWEAKS.toughnessPercentageFactor*(toughness/maxToughnessLinear))/100d;
        double factor = Math.log(DHTConfig.TWEAKS.maxToughness/toughness)/Math.log(2d);
        return (DHTConfig.TWEAKS.maxToughnessPercentage-(DHTConfig.TWEAKS.toughnessPercentageFactor*factor))/100d;
    }

    public static boolean canAddAura(int aura) {
        return aura<DHTConfig.SKILLS.auraCap;
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
