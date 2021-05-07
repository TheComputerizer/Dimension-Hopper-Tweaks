package com.TheComputerizer.DimensionalHopperFinalBoss;

import com.TheComputerizer.DimensionalHopperFinalBoss.proxy.CommonProxy;
import com.TheComputerizer.DimensionalHopperFinalBoss.util.handlers.RegistryHandler;
import com.TheComputerizer.DimensionalHopperFinalBoss.util.handlers.RenderHandler;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = DimensionalHopperFinalBoss.MODID, name = DimensionalHopperFinalBoss.NAME, version = DimensionalHopperFinalBoss.VERSION)
public class DimensionalHopperFinalBoss
{
    public static final String MODID = "dimensionalhopperfinalboss";
    public static final String NAME = "Dimension Hopper Final Boss";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        RenderHandler.registerEntityRenders();
        logger = event.getModLog();
    }

    @EventHandler
    public static void init(FMLInitializationEvent event)
    {
        //
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        RegistryHandler.preInitRegistries();
    }

    @EventHandler
    public static void postInit(FMLPostInitializationEvent event)
    {
    }

    @Instance
    public static DimensionalHopperFinalBoss instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;
}
