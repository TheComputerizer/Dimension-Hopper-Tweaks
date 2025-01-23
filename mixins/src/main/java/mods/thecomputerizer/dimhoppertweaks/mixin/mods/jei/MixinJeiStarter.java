package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.gui.textures.Textures;
import mezz.jei.startup.JeiStarter;
import mezz.jei.startup.ModRegistry;
import mods.thecomputerizer.dimhoppertweaks.integration.jei.JeiActionSupplier;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IModRegistry;
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

    @Inject(at = @At("TAIL"), method = "start")
    private void dimhoppertweaks$started(List<IModPlugin> plugins, Textures textures, CallbackInfo ci) {
        JeiActionSupplier.finish();
    }

    @Inject(at = @At("TAIL"), method = "registerPlugins")
    private static void dimhoppertweaks$runtime(List<IModPlugin> plugins, ModRegistry registry, CallbackInfo ci) {
        JeiActionSupplier.injectDescriptionQueues((IModRegistry)registry);
        JeiActionSupplier.injectDescriptionAdditions();
    }
}