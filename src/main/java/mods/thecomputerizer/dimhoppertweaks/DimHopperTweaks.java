package mods.thecomputerizer.dimhoppertweaks;

import mods.thecomputerizer.dimhoppertweaks.client.ClientRegistryHandler;
import mods.thecomputerizer.dimhoppertweaks.common.capability.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillCapabilityStorage;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.network.*;
import mods.thecomputerizer.dimhoppertweaks.registry.RegistryHandler;
import mods.thecomputerizer.theimpossiblelibrary.network.NetworkHandler;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@SuppressWarnings("unused")
@Mod(modid = DHTRef.MODID, name = DHTRef.NAME, version = DHTRef.VERSION, dependencies = DHTRef.DEPENDENCIES)
public class DimHopperTweaks {

    public DimHopperTweaks() {
        NetworkHandler.queueServerPacketRegistries(PacketSendKeyPressed.class,PacketSyncGuiData.class);
        NetworkHandler.queueClientPacketRegistries(PacketBossClientEffects.class,PacketGrayScaleTimer.class,
                PacketOpenGui.class, PacketQueryGenericClient.class,PacketRenderBossAttack.class,
                PacketSyncCapabilityData.class,PacketSyncPlayerHealth.class,PacketTileEntityClassQuery.class,
                PacketUpdateBossRender.class);
    }

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        DHTRef.LOGGER.info("Starting pre-init");
        CapabilityManager.INSTANCE.register(ISkillCapability.class,new SkillCapabilityStorage(),SkillCapability::new);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            OBJLoader.INSTANCE.addDomain(DHTRef.MODID);
            ClientRegistryHandler.registerRenderers();
        }
        DHTRef.LOGGER.info("Completed pre-init");
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        if(FMLCommonHandler.instance().getSide().isClient()) RegistryHandler.onPostInit(event);
    }

    @Mod.EventHandler
    public void start(FMLServerStartingEvent event) {
        RegistryHandler.onServerStarting(event);
    }
}