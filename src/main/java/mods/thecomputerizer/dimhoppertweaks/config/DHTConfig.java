package mods.thecomputerizer.dimhoppertweaks.config;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DHTRef.MODID)
@Config(modid = DHTRef.MODID, name = DHTRef.NAME, category = "")
public final class DHTConfig {

    @Name("debug")
    @LangKey("config.dimhoppertweaks.debug")
    @Comment("Dev options for debugging")
    public static Debug DEBUG = new Debug();

    @Name("mods")
    @LangKey("config.dimhoppertweaks.mods")
    @Comment("Options regarding mod specific tweaks and fixes")
    public static Mods MODS = new Mods();

    @Name("skills")
    @LangKey("config.dimhoppertweaks.skills")
    @Comment("Options regarding the skills, traits, and other skill stuff")
    public static Skills SKILLS = new Skills();

    @Name("tweaks")
    @LangKey("config.dimhoppertweaks.tweaks")
    @Comment("Options regarding tweaks that are not specific to a single mod")
    public static Tweaks TWEAKS = new Tweaks();

    @Name("world")
    @LangKey("config.dimhoppertweaks.world")
    @Comment("World specific options")
    public static World WORLD = new World();

    public static final class Debug {

        @Name("devLog")
        @LangKey("config.dimhoppertweaks.debug.devLog")
        @Comment("Enables to dev log. May result in log spam")
        public boolean devLog = false;
    }

    public static final class Mods {
        
        @Name("airTerminalEfficiency")
        @LangKey("config.dimhoppertweaks.mods.airTerminalEfficiency")
        @Comment("Determines the base percentage efficiency for air terminals. Material efficiencies are be applied on top of this.")
        public double airTerminalEfficiency = 2.0;

        @Name("lastStandCooldown")
        @LangKey("config.dimhoppertweaks.mods.lastStandMaxCooldown")
        @Comment("The cooldown in ticks for the last stand enchantment at level 1. Each additional level subtracts a tick to the cooldown with a minimum possible cooldown of 2 ticks")
        public int lastStandCooldown = 5;
        
        @Name("lightningEfficiencyIron")
        @LangKey("config.dimhoppertweaks.mods.lightningEfficiencyIron")
        @Comment("Determines the percentage efficiency of iron as a material when working with LE")
        public double lightningEfficiencyIron = 0.2d;
        
        @Name("lightningEfficiencySteel")
        @LangKey("config.dimhoppertweaks.mods.lightningEfficiencySteel")
        @Comment("Determines the percentage efficiency of steel as a material when working with LE")
        public double lightningEfficiencySteel = 0.3d;
        
        @Name("lightningEfficiencyLead")
        @LangKey("config.dimhoppertweaks.mods.lightningEfficiencyLead")
        @Comment("Determines the percentage efficiency of lead as a material when working with LE")
        public double lightningEfficiencyLead = 0.4d;
        
        @Name("lightningEfficiencyTin")
        @LangKey("config.dimhoppertweaks.mods.lightningEfficiencyTin")
        @Comment("Determines the percentage efficiency of tin as a material when working with LE")
        public double lightningEfficiencyTin = 0.5d;
        
        @Name("lightningEfficiencyAluminum")
        @LangKey("config.dimhoppertweaks.mods.lightningEfficiencyAluminum")
        @Comment("Determines the percentage efficiency of aluminum as a material when working with LE")
        public double lightningEfficiencyAluminum = 0.6d;
        
        @Name("lightningEfficiencyGold")
        @LangKey("config.dimhoppertweaks.mods.lightningEfficiencyGold")
        @Comment("Determines the percentage efficiency of gold as a material when working with LE")
        public double lightningEfficiencyGold = 0.7d;
        
        @Name("lightningEfficiencyCopper")
        @LangKey("config.dimhoppertweaks.mods.lightningEfficiencyCopper")
        @Comment("Determines the percentage efficiency of copper as a material when working with LE")
        public double lightningEfficiencyCopper = 0.8d;
        
        @Name("lightningEfficiencyElectricium")
        @LangKey("config.dimhoppertweaks.mods.lightningEfficiencyElectricium")
        @Comment("Determines the percentage efficiency of electricium as a material when working with LE")
        public double lightningEfficiencyElectricium = 1d;
        
        @Name("lightningEfficiencySkyfather")
        @LangKey("config.dimhoppertweaks.mods.lightningEfficiencySkyfather")
        @Comment("Determines the percentage efficiency of skyfather as a material when working with LE")
        public double lightningEfficiencySkyfather = 1.5d;
        
        @Name("lightningEfficiencyMystic")
        @LangKey("config.dimhoppertweaks.mods.lightningEfficiencyMystic")
        @Comment("Determines the percentage efficiency of mystic as a material when working with LE")
        public double lightningEfficiencyMystic = 2d;
        
        @Name("lightningEfficiencyAwakenedDraconium")
        @LangKey("config.dimhoppertweaks.mods.lightningEfficiencyAwakenedDraconium")
        @Comment("Determines the percentage efficiency of awakened draconium as a material when working with LE")
        public double lightningEfficiencyAwakenedDraconium = 3d;
        
        @Name("lightningEfficiencyInsanium")
        @LangKey("config.dimhoppertweaks.mods.lightningEfficiencyInsanium")
        @Comment("Determines the percentage efficiency of insanium as a material when working with LE")
        public double lightningEfficiencyInsanium = 4d;
        
        @Name("lightningEfficiencyUltimate")
        @LangKey("config.dimhoppertweaks.mods.lightningEfficiencyUltimate")
        @Comment("Determines the percentage efficiency of ultimate as a material when working with LE")
        public double lightningEfficiencyUltimate = 5d;
        
        @Name("lightningEfficiencyInfinity")
        @LangKey("config.dimhoppertweaks.mods.lightningEfficiencyInfinity")
        @Comment("Determines the percentage efficiency of infinity as a material when working with LE")
        public double lightningEfficiencyInfinity = 10d;

        @Name("xpLossMasochist")
        @LangKey("config.dimhoppertweaks.mods.xpLossMasochist")
        @Comment("The percentage of XP lost on death for masochist mode players")
        public int xpLossMasochist = 50;

        @Name("xpLossNormal")
        @LangKey("config.dimhoppertweaks.mods.xpLossDefault")
        @Comment("The percentage of XP lost on death for non masochist mode players")
        public int xpLossNormal = 10;
    }

    public static final class Skills {

        @Name("auraCap")
        @LangKey("config.dimhoppertweaks.skills.auraCap")
        @Comment("The maximum amount of aura the Nature's aura trait can balance to (neutral is 1000000)")
        public int auraCap = 2000000;

        @Name("auraGains")
        @LangKey("config.dimhoppertweaks.skills.auraGains")
        @Comment("The amount of aura added per second with the Nature's aura trait when the current chunk is below the auraCap")
        public int auraGains = 10000;

        @Name("auraRadius")
        @LangKey("config.dimhoppertweaks.skills.auraRadius")
        @Comment("The range in blocks to use when checking the auraCap around a player with the Nature's Aura trait")
        public int auraRadius = 30;
    }

    public static final class Tweaks {

        @Name("maxArmor")
        @LangKey("config.dimhoppertweaks.tweaks.maxArmor")
        @Comment("The maximum armor in regards to custom damage reduction calculations")
        public double maxArmor = 200d;

        @Name("maxArmorPercentage")
        @LangKey("config.dimhoppertweaks.tweaks.maxArmorPercentage")
        @Comment("The maximum percentage of damage that can be blocked by the maxArmor value. The actual value cannot exceed maxArmorReduction")
        public double maxArmorPercentage = 90d;

        @Name("armorPercentageFactor")
        @LangKey("config.dimhoppertweaks.tweaks.armorPercentageFactor")
        @Comment("The percentage factor of damage reduced by armor in regards to maxArmorPercentage - (the power of 2 of armor/maxArmor)")
        public double armorPercentageFactor = 15d;

        @Name("maxArmorReduction")
        @LangKey("config.dimhoppertweaks.tweaks.maxArmorReduction")
        @Comment("The maximum amount of damage that can be blocked at maxArmor")
        public double maxArmorReduction = 50d;

        @Name("maxToughness")
        @LangKey("config.dimhoppertweaks.tweaks.maxToughness")
        @Comment("The maximum toughness in regards to custom damage reduction calculations")
        public double maxToughness = 50d;

        @Name("maxToughnessPercentage")
        @LangKey("config.dimhoppertweaks.tweaks.maxToughnessPercentage")
        @Comment("The maximum percentage of the remaining damage after the armor calculations are run that can be blocked by the maxToughness value. The actual value cannot exceed maxToughnessReduction")
        public double maxToughnessPercentage = 50d;

        @Name("toughnessPercentageFactor")
        @LangKey("config.dimhoppertweaks.tweaks.toughnessPercentageFactor")
        @Comment("The percentage factor of damage reduced by toughness in regards to maxToughnessPercentage - (the power of 2 toughness/maxToughness)")
        public double toughnessPercentageFactor = 10d;

        @Name("maxToughnessReduction")
        @LangKey("config.dimhoppertweaks.tweaks.maxToughnessReduction")
        @Comment("The maximum amount of damage that can be blocked at maxToughness")
        public double maxToughnessReduction = 25d;
    }

    public static final class World {

        @Name("chestReplacementChance")
        @LangKey("config.dimhoppertweaks.world.chestReplacementChance")
        @Comment("The chance of a chest being replaced with a small storage crate after it is generated")
        public float chestReplacementChance = 0.02f;
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(event.getModID().equals(DHTRef.MODID)) {
            ConfigManager.sync(event.getModID(),Config.Type.INSTANCE);
            DHTConfigHelper.onConfigReloaded();
        }
    }
}
