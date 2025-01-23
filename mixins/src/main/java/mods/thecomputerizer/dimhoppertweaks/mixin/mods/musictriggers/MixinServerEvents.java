package mods.thecomputerizer.dimhoppertweaks.mixin.mods.musictriggers;

import mods.thecomputerizer.musictriggers.server.ServerEvents;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ServerEvents.class, remap = false)
public class MixinServerEvents {
    
    /**
     * @author The_Computerizer
     * @reason Remove the lag generator
     */
    @SubscribeEvent @Overwrite
    public static void onEntitySpawned(EntityJoinWorldEvent e) {}
}