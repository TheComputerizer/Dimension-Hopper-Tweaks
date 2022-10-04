package mods.thecomputerizer.dimensionhoppertweaks;

import mods.thecomputerizer.dimensionhoppertweaks.client.ClientHandler;
import mods.thecomputerizer.dimensionhoppertweaks.common.commands.RandomTP;
import mods.thecomputerizer.dimensionhoppertweaks.common.commands.SummonBoss;
import mods.thecomputerizer.dimensionhoppertweaks.common.skills.ISkillCapability;
import mods.thecomputerizer.dimensionhoppertweaks.common.skills.SkillCapability;
import mods.thecomputerizer.dimensionhoppertweaks.common.skills.SkillCapabilityStorage;
import mods.thecomputerizer.dimensionhoppertweaks.network.PacketHandler;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = DimensionHopperTweaks.MODID, name = DimensionHopperTweaks.NAME, version = DimensionHopperTweaks.VERSION, dependencies = DimensionHopperTweaks.DEPENDENCIES)
public class DimensionHopperTweaks
{
    public static final String MODID = "dimensionhoppertweaks";
    public static final String NAME = "Dimension Hopper Tweaks";
    public static final String VERSION = "1.7.1";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2860,);required-after:fermion;required-after:avaritia;required-after:sgcraft;required-after:musictriggers;";
    public static Logger LOGGER;

    @Mod.Instance(MODID)
    public static DimensionHopperTweaks INSTANCE;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        LOGGER = LogManager.getLogger();
        PacketHandler.registerPackets();
        CapabilityManager.INSTANCE.register(ISkillCapability.class, new SkillCapabilityStorage(), SkillCapability::new);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            OBJLoader.INSTANCE.addDomain(DimensionHopperTweaks.MODID);
            ClientHandler.registerRenderers();
        }
    }

    @Mod.EventHandler
    public void start(FMLServerStartingEvent event) {
        event.registerServerCommand(new RandomTP());
        event.registerServerCommand(new SummonBoss());
    }
}
