package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.util.Lumberjack;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = AvaritiaRecipeManager.class, remap = false)
public class MixinAvaritiaRecipeManager {

    @Redirect(at = @At(value = "INVOKE", target = "Lmorph/avaritia/util/Lumberjack;log(Lorg/apache/logging/log4j/Level;" +
            "Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V"), method = "lambda$loadRecipes$6")
    private static void dimhoppertweaks$ignoreRecipeErrors(Level logLevel, Throwable throwable, String format, Object[] objects) {
        Lumberjack.log(logLevel,format,objects);
    }
}
