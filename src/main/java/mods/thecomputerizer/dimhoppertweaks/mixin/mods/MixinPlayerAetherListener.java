package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import com.gildedgames.aether.api.registrar.CapabilitiesAether;
import com.gildedgames.aether.common.capabilities.entity.player.PlayerAether;
import com.gildedgames.aether.common.events.listeners.player.PlayerAetherListener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@Mixin(value = PlayerAetherListener.class, remap = false)
public class MixinPlayerAetherListener {

    /**
     * @author The_Computerizer
     * @reason Fix null capability issues
     */
    @SubscribeEvent
    @Overwrite
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(Objects.nonNull(CapabilitiesAether.PLAYER_DATA) && event.player.hasCapability(CapabilitiesAether.PLAYER_DATA,null)) {
            PlayerAether aePlayer = PlayerAether.getPlayer(event.player);
            aePlayer.onPlayerTick(event);
        }
    }
}
