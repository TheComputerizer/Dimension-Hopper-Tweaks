package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import crafttweaker.mods.jei.JEIAddonPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.startup.ModRegistry;
import mods.thecomputerizer.dimhoppertweaks.integration.jei.JeiActionSupplier;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.ModRegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = JEIAddonPlugin.class, remap = false)
public class MixinJEIAddonPlugin {

    @Inject(at = @At("RETURN"), method = "register")
    private void dimhoppertweaks$afterRegister(IModRegistry registry, CallbackInfo ci) {
        if(registry instanceof ModRegistry) JeiActionSupplier.injectDescriptionQueues((ModRegistryAccess)registry);
    }
}
