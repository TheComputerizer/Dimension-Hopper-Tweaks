package mods.thecomputerizer.dimhoppertweaks.client.render;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

@SuppressWarnings("ConstantConditions")
@Mod.EventBusSubscriber(modid = DHTRef.MODID, value = Side.CLIENT)
public class RenderEvents {
    public static final List<RenderDelayedAOE> ATTACKS = Collections.synchronizedList(new ArrayList<>());

    @SubscribeEvent
    public static void renderAttacks(RenderWorldLastEvent event) {
        synchronized (ATTACKS) {
            for (RenderDelayedAOE attack : ATTACKS)
                attack.render(event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public static void tickTimers(TickEvent.ClientTickEvent event) {
        synchronized (ATTACKS) {
            if (event.phase == TickEvent.Phase.END) ATTACKS.removeIf(RenderDelayedAOE::tick);
        }
    }
}
