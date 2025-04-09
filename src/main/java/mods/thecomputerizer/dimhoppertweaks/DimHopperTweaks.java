package mods.thecomputerizer.dimhoppertweaks;

import mods.thecomputerizer.dimhoppertweaks.client.DHTClient;
import mods.thecomputerizer.dimhoppertweaks.common.capability.chunk.ExtraChunkData;
import mods.thecomputerizer.dimhoppertweaks.common.capability.chunk.ExtraChunkDataStorage;
import mods.thecomputerizer.dimhoppertweaks.common.capability.chunk.IExtraChunkData;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillCapabilityStorage;
import mods.thecomputerizer.dimhoppertweaks.integration.dropt.DroptRules;
import mods.thecomputerizer.dimhoppertweaks.network.*;
import mods.thecomputerizer.dimhoppertweaks.registry.RegistryHandler;
import mods.thecomputerizer.theimpossiblelibrary.api.core.CoreAPI;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.*;
import static net.minecraftforge.common.capabilities.CapabilityManager.INSTANCE;

@SuppressWarnings("unused")
@Mod(modid=MODID,name=NAME,version=VERSION,dependencies=DEPENDENCIES)
public class DimHopperTweaks {

    public DimHopperTweaks() {
        DHTNetwork.initCommon();
        if(CoreAPI.isClient()) DHTNetwork.initClient();
    }

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Starting pre-init");
        INSTANCE.register(ISkillCapability.class,new SkillCapabilityStorage(),SkillCapability::new);
        INSTANCE.register(IExtraChunkData.class,new ExtraChunkDataStorage(),ExtraChunkData::new);
        if(CLIENT) {
            OBJLoader.INSTANCE.addDomain(MODID);
            DHTClient.registerRenderers();
        }
        LOGGER.info("Completed pre-init");
    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {
        RegistryHandler.addLightningStrikeRecipes();
    }

    @EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        if(CLIENT) RegistryHandler.onPostInit(event);
    }

    @EventHandler
    public static void loadComplete(FMLLoadCompleteEvent event) {
        DroptRules.reload();
    }

    @EventHandler
    public void start(FMLServerStartingEvent event) {
        RegistryHandler.onServerStarting(event);
    }
}