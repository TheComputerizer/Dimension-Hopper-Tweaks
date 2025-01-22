package mods.thecomputerizer.dimhoppertweaks.client.shader;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.MODID;
import static net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START;
import static net.minecraftforge.fml.relauncher.Side.CLIENT;

@EventBusSubscriber(modid = MODID,value = CLIENT)
public class ShaderEvents {

    @SubscribeEvent
    public static void preRenderTick(RenderTickEvent event) {
        if(event.phase==START) ShaderManager.getInstance().initShaderFrame(Minecraft.getMinecraft().world);
    }
}