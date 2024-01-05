package mods.thecomputerizer.dimhoppertweaks.mixin.mods.crafttweaker;

import crafttweaker.mods.jei.JEIAddonPlugin;
import crafttweaker.mods.jei.JEIMod;
import mezz.jei.api.ingredients.VanillaTypes;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = JEIMod.class, remap = false)
public class MixinJEIMod {

    @Inject(at = @At("TAIL"), method = "applyActions")
    private static void dimhoppertweaks$applyActions(CallbackInfo ci) {
        if(Objects.nonNull(JEIAddonPlugin.itemRegistry) && !DelayedModAccess.ADDED_ITEMS.isEmpty())
            JEIAddonPlugin.itemRegistry.addIngredientsAtRuntime(VanillaTypes.ITEM,DelayedModAccess.ADDED_ITEMS);
    }
}
