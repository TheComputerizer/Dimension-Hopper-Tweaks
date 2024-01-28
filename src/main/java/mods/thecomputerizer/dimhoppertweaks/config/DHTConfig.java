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

        @Name("lastStandCooldown")
        @LangKey("config.dimhoppertweaks.mods.lastStandMaxCooldown")
        @Comment("The cooldown in ticks for the last stand enchantment at level 1. Each additional level subtracts a tick to the cooldown with a minimum possible cooldown of 2 ticks")
        public int lastStandCooldown = 5;
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
