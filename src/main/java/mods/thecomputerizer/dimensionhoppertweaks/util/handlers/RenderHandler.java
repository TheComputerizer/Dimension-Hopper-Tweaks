package mods.thecomputerizer.dimensionhoppertweaks.util.handlers;

import mods.thecomputerizer.dimensionhoppertweaks.entity.EntityFinalBoss;
import mods.thecomputerizer.dimensionhoppertweaks.entity.render.RenderFinalBoss;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler
{
    public static void registerEntityRenders()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityFinalBoss.class, RenderFinalBoss::new);
    }
}
