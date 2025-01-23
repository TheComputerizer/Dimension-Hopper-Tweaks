package mods.thecomputerizer.dimhoppertweaks.mixin.mods.toolprogression;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tyra314.toolprogression.compat.gamestages.GSHelper;

@Mixin(value = GSHelper.class, remap = false)
public abstract class MixinGSHelper {

    /**
     * @author The_Computerizer
     * @reason It checks for the wrong classes...
     */
    @Overwrite
    public static boolean isLoaded() {
        return true;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V"),
            method = "<clinit>")
    private static void dimhoppertweaks$redirectLog(Logger logger, String s) {}
}