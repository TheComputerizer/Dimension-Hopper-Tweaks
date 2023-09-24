package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import com.legacy.goodnightsleep.GNSEventHandler;
import com.legacy.goodnightsleep.player.PlayerGNS;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(value = GNSEventHandler.class, remap = false)
public class MixinGNSEventHandler {

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/legacy/goodnightsleep/player/PlayerGNS;onUpdate()V"), method = "onPlayerUpdate")
    private void dimhoppertweaks$redirectOnUpdate(PlayerGNS player) {
        if(Objects.nonNull(player)) player.onUpdate();
    }
}
