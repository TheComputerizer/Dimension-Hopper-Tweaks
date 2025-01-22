package mods.thecomputerizer.dimhoppertweaks.client.render;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import java.util.*;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.MODID;
import static net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END;
import static net.minecraftforge.fml.relauncher.Side.CLIENT;

@SuppressWarnings("ConstantConditions")
@EventBusSubscriber(modid = MODID,value = CLIENT)
public class RenderEvents {
    public static final List<RenderDelayedAOE> ATTACKS = Collections.synchronizedList(new ArrayList<>());

    @SubscribeEvent
    public static void renderAttacks(RenderWorldLastEvent event) {
        synchronized(ATTACKS) {
            for(RenderDelayedAOE attack : ATTACKS) attack.render(event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public static void tickTimers(ClientTickEvent event) {
        synchronized(ATTACKS) {
            if(event.phase==END) ATTACKS.removeIf(RenderDelayedAOE::tick);
        }
    }
}
