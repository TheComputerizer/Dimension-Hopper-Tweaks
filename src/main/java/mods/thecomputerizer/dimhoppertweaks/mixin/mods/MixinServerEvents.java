package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mods.thecomputerizer.musictriggers.server.ServerEvents;
import mods.thecomputerizer.musictriggers.server.data.IPersistentTriggerData;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(value = ServerEvents.class, remap = false)
public class MixinServerEvents {

    @Redirect(at = @At(value = "INVOKE", target = "Lmods/thecomputerizer/musictriggers/server/data/IPersistentTriggerData;" +
            "onLogin(Lnet/minecraft/entity/player/EntityPlayerMP;)V"), method = "onPlayerLogin")
    private static void dimhoppertweaks$redirectOnPlayerLogin(IPersistentTriggerData data, EntityPlayerMP player) {
        if(Objects.nonNull(data)) data.onLogin(player);
    }
}
