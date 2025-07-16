package mods.thecomputerizer.dimhoppertweaks.mixin.mods.performancemonitoringenhanc;

import iamfmgod.performancemonitoringenhanc.hud.HudManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = HudManager.class, remap = false)
public abstract class MixinHudManager {

    /**
     * Set the default visibility of the performance hud to false
     */
    @Shadow private static boolean visible = false;
}