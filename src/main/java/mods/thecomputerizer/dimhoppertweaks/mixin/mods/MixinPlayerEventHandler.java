package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import com.legacy.blue_skies.capability.PlayerEventHandler;
import com.legacy.blue_skies.capability.SkiesPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(value = PlayerEventHandler.class, remap = false)
public class MixinPlayerEventHandler {

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/legacy/blue_skies/capability/SkiesPlayer;onUpdate()V"),
            method = "onCapabilityUpdate")
    private void dimhoppertweaks$redirectOnUpdate(SkiesPlayer player) {
        if(Objects.nonNull(player)) player.onUpdate();
    }
}
