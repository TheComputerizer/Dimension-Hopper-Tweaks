package mods.thecomputerizer.dimhoppertweaks.mixin.mods.vintagefix;

import org.apache.logging.log4j.Logger;
import org.embeddedt.vintagefix.dynamicresources.model.DynamicModelProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = DynamicModelProvider.class, remap = false)
public abstract class MixinDynamicModelProvider {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;"+
            "Ljava/lang/Object;Ljava/lang/Object;)V"), method = "loadModelFromBlockstateOrInventory")
    private void dimhoppertweaks$ingoreLogSpam1(Logger logger, String s, Object o, Object o1) {}

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;"+
            "Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V"), method = "loadModelFromBlockstateOrInventory")
    private void dimhoppertweaks$ingoreLogSpam2(Logger logger, String s, Object o, Object o1, Object o2) {}
}