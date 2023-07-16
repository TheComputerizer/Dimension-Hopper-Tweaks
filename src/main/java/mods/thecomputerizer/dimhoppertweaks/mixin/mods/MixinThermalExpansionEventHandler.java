package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import cofh.thermalexpansion.proxy.EventHandler;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = EventHandler.class, remap = false)
public class MixinThermalExpansionEventHandler {
    @Overwrite
    public void handleEntityItemPickup(EntityItemPickupEvent event) {

    }
}
