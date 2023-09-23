package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraftforge.common.ForgeHooks;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ForgeHooks.class, remap = false)
public class MixinForgeHooks {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;" +
            "error(Ljava/lang/String;Ljava/lang/Throwable;)V", ordinal = 0), method = "lambda$loadAdvancements$0")
    private static void dimhoppertweaks$stopSpammingAdvancementErrors1(Logger logger, String s, Throwable throwable) {
        logger.debug("Ingoring errored advacncement from {}",s.replaceAll("Ingoring errored advacncement from ",""));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;" +
            "error(Ljava/lang/String;Ljava/lang/Throwable;)V", ordinal = 1), method = "lambda$loadAdvancements$0")
    private static void dimhoppertweaks$stopSpammingAdvancementErrors2(Logger logger, String s, Throwable throwable) {
        logger.debug("Ingoring unreadable advacncement {}",s.replaceAll("Couldn't read advancement ",""));
    }
}
