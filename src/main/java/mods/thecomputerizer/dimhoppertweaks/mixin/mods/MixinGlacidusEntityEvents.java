package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import com.legacy.glacidus.events.GlacidusEntityEvents;
import com.legacy.glacidus.player.PlayerCapability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(value = GlacidusEntityEvents.class, remap = false)
public class MixinGlacidusEntityEvents {

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/legacy/glacidus/player/PlayerCapability;onUpdate()V"),
            method = "onLivingUpdate")
    private void dimhoppetweaks$redirectOnUpdate(PlayerCapability cap) {
        if(Objects.nonNull(cap)) cap.onUpdate();
    }
}
