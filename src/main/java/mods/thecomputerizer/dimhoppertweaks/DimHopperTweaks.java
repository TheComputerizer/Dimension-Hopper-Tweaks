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

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)
public class DimHopperTweaks
{
    @Mod.Instance(Constants.MODID)
    public static DimHopperTweaks INSTANCE;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        Constants.LOGGER = LogManager.getLogger();
        Constants.LOGGER.info("Starting pre-init");
        PacketHandler.registerPackets();
        CapabilityManager.INSTANCE.register(ISkillCapability.class, new SkillCapabilityStorage(), SkillCapability::new);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            OBJLoader.INSTANCE.addDomain(Constants.MODID);
            ClientHandler.registerRenderers();
        }
        Constants.LOGGER.info("Completed pre-init");
    }

    @Mod.EventHandler
    public void start(FMLServerStartingEvent event) {
        Constants.LOGGER.info("Registering commands");
        event.registerServerCommand(new RandomTP());
        event.registerServerCommand(new SummonBoss());
    }
}
