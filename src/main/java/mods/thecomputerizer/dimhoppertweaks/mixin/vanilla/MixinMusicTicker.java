package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.client.audio.MusicTicker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MusicTicker.class, priority = Integer.MAX_VALUE)
public class MixinMusicTicker {

    @Inject(at = @At(value = "HEAD"), method = "update", cancellable = true)
    private void musictriggers_update(CallbackInfo info) {
        info.cancel();
    }
}
