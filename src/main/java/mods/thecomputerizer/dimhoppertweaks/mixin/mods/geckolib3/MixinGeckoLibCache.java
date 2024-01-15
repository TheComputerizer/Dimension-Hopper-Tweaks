package mods.thecomputerizer.dimhoppertweaks.mixin.mods.geckolib3;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import software.bernie.geckolib3.resource.GeckoLibCache;

@Mixin(value = GeckoLibCache.class, remap = false)
public abstract class MixinGeckoLibCache {

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/lang/Exception;printStackTrace()V", ordinal = 0),
            method = "onResourceManagerReload")
    private void dimhoppertweaks$suppressStackTrace1(Exception ex) {}

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;" +
            "error(Ljava/lang/String;Ljava/lang/Throwable;)V", ordinal = 0), method = "onResourceManagerReload")
    private void dimhoppertweaks$suppressStackTrace2(Logger logger, String s, Throwable throwable) {}
}