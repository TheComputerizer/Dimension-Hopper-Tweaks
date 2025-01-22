package mods.thecomputerizer.dimhoppertweaks.mixin.mods.vintagefix;

import org.apache.logging.log4j.Logger;
import org.embeddedt.vintagefix.dynamicresources.model.DynamicBakedModelProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = DynamicBakedModelProvider.class, remap = false)
public abstract class MixinDynamicBakedModelProvider {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;" +
            "error(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 0), method = "loadBakedModel")
    private void dimhoppertweaks$IgnoreResourceErrors1(Logger logger, String s, Object o) {}

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;" +
            "error(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 1), method = "loadBakedModel")
    private void dimhoppertweaks$IgnoreResourceErrors2(Logger logger, String s, Object o) {}

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;" +
            "error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", ordinal = 0), method = "loadBakedModel")
    private void dimhoppertweaks$IgnoreResourceErrors3(Logger logger, String s, Object o, Object o1) {}

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;" +
            "error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", ordinal = 0), method = "loadBakedModel")
    private void dimhoppertweaks$IgnoreResourceErrors4(Logger logger, String s, Object o, Object o1) {}
}