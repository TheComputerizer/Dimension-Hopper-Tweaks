package mods.thecomputerizer.dimhoppertweaks.mixin.forge;

import net.minecraftforge.common.crafting.CraftingHelper;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = CraftingHelper.class, remap = false)
public class MixinCraftingHelper {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;" +
            "error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), method = "lambda$loadRecipes$22")
    private static void dimhoppertweaks$ignoreRecipeErrors(Logger logger, String s, Object o, Object o1) {}
}
