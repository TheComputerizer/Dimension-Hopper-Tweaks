package mods.thecomputerizer.dimhoppertweaks.mixin.mods.vintagefix;

import org.apache.logging.log4j.Logger;
import org.embeddedt.vintagefix.dynamicresources.model.DynamicBakedModelProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = DynamicBakedModelProvider.class, remap = false)
public abstract class MixinDynamicBakedModelProvider {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;" +
            "error(Ljava/lang/String;Ljava/lang/Object;)V"), method = "loadBakedModel")
    private void dimhoppertweaks$IgnoreResourceErrors1(Logger logger, String s, Object o) {}
}