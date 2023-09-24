package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mcjty.lib.McJtyLib;
import mcjty.lib.preferences.PreferencesProperties;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(value = McJtyLib.EventHandler.class, remap = false)
public class MixinEventHandler {

    @Redirect(at = @At(value = "INVOKE", target = "Lmcjty/lib/preferences/PreferencesProperties;" +
            "tick(Lnet/minecraft/entity/player/EntityPlayerMP;)V"), method = "onPlayerTickEvent")
    private void dimhoppertweaks$redirectTick(PreferencesProperties properties, EntityPlayerMP player) {
        if(Objects.nonNull(properties)) properties.tick(player);
    }
}
