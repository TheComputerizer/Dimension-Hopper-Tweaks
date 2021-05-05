package com.TheComputerizer.DimensionalHopperFinalBoss.util.handlers;

import com.TheComputerizer.DimensionalHopperFinalBoss.entity.EntityFinalBoss;
import com.TheComputerizer.DimensionalHopperFinalBoss.entity.render.RenderFinalBoss;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler
{
    public static void registerEntityRenders()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityFinalBoss.class, RenderFinalBoss::new);
    }
}
