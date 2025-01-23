package mods.thecomputerizer.dimhoppertweaks.mixin.forge;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraftforge.common.ForgeVersion$1")
public abstract class MixinForgeVersion {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;debug(Ljava/lang/String;" +
            "Ljava/lang/Throwable;)V"), method = "process", remap = false)
    private void dimhoppertweaks$stopLoggingVersionErrors(Logger logger, String s, Throwable throwable) {
        logger.debug(s);
    }
}