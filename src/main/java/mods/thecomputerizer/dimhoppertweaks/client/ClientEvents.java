package mods.thecomputerizer.dimhoppertweaks.client;

import mods.thecomputerizer.dimhoppertweaks.Constants;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Constants.MODID, value = Side.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        if(ClientHandler.FOG_DENSITY_OVERRIDE>=0)
            event.setDensity(ClientHandler.FOG_DENSITY_OVERRIDE);
    }

    @SubscribeEvent
    public static void onFogColors(EntityViewRenderEvent.FogColors event) {
        event.setRed(0.5f);
        event.setGreen(0.5f);
        event.setBlue(1f);
    }
}
