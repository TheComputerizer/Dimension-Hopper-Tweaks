package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import software.bernie.geckolib3.file.AnimationFileLoader;

@Mixin(value = AnimationFileLoader.class, remap = false)
public class MixinAnimationFileLoader {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;" +
            "error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), method = "loadAllAnimations")
    private void dimhoppertweaks$removeAnimationErrorStackTrace(Logger logger, String s, Object o, Object o1) {
        logger.error(s,o);
    }
}
