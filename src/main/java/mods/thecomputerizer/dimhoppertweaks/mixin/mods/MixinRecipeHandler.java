package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thelm.jaopca.recipes.RecipeHandler;

@Mixin(value = RecipeHandler.class, remap = false)
public class MixinRecipeHandler {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;" +
            "Ljava/lang/Object;Ljava/lang/Object;)V"), method = "lambda$registerEarlyRecipes$0")
    private static void dimhoppertweaks$removeLogSpam1(Logger instance, String s, Object o, Object o1) {}

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;" +
            "Ljava/lang/Object;Ljava/lang/Object;)V"), method = "lambda$registerRecipes$1")
    private static void dimhoppertweaks$removeLogSpam2(Logger instance, String s, Object o, Object o1) {}

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;" +
            "Ljava/lang/Object;Ljava/lang/Object;)V"), method = "lambda$registerLateRecipes$2")
    private static void dimhoppertweaks$removeLogSpam3(Logger instance, String s, Object o, Object o1) {}
}
