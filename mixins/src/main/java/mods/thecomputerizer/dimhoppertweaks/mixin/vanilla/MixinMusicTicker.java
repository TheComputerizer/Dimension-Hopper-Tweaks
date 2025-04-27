package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.musictriggers.api.data.channel.ChannelHelper;
import net.minecraft.client.audio.MusicTicker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicTicker.class)
public class MixinMusicTicker {
    
    @Inject(at = @At("HEAD"),method = "update")
    private void dimhoppertweaks$update(CallbackInfo info) {
        if(!ChannelHelper.stopVanillaMusicTicker()) info.cancel();
    }
}