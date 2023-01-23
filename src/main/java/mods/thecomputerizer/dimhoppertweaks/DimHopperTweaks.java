package mods.thecomputerizer.dimhoppertweaks;

import mods.thecomputerizer.dimhoppertweaks.client.ClientHandler;
import mods.thecomputerizer.dimhoppertweaks.common.commands.RandomTP;
import mods.thecomputerizer.dimhoppertweaks.common.commands.SummonBoss;
import mods.thecomputerizer.dimhoppertweaks.common.skills.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillCapabilityStorage;
import mods.thecomputerizer.dimhoppertweaks.network.PacketHandler;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = DimHopperTweaks.MODID, name = DimHopperTweaks.NAME, version = DimHopperTweaks.VERSION, dependencies = DimHopperTweaks.DEPENDENCIES)
public class DimHopperTweaks
{
    public static final String MODID = "dimhoppertweaks";
    public static final String NAME = "Dimension Hopper Tweaks";
    public static final String VERSION = "1.7.1";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2860,);required-after:fermion;" +
            "required-after:avaritia;required-after:sgcraft;";
    public static Logger LOGGER;

    @Mod.Instance(MODID)
    public static DimHopperTweaks INSTANCE;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        LOGGER = LogManager.getLogger();
        PacketHandler.registerPackets();
        CapabilityManager.INSTANCE.register(ISkillCapability.class, new SkillCapabilityStorage(), SkillCapability::new);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            OBJLoader.INSTANCE.addDomain(DimHopperTweaks.MODID);
            ClientHandler.registerRenderers();
        }
    }

    @Mod.EventHandler
    public void start(FMLServerStartingEvent event) {
        event.registerServerCommand(new RandomTP());
        event.registerServerCommand(new SummonBoss());
    }
}
