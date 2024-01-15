package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.gui.textures.Textures;
import mezz.jei.startup.JeiStarter;
import mods.thecomputerizer.dimhoppertweaks.integration.jei.JeiActionSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = JeiStarter.class, remap = false)
public abstract class MixinJeiStarter {

    @Inject(at = @At("HEAD"), method = "start")
    private void dimhoppertweaks$start(List<IModPlugin> plugins, Textures textures, CallbackInfo ci) {
        JeiActionSupplier.injectVisibilityQueues();
    }
}