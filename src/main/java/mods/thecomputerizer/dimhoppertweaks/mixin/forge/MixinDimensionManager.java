package mods.thecomputerizer.dimhoppertweaks.mixin.forge;

import net.minecraftforge.common.DimensionManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = DimensionManager.class, remap = false)
public class MixinDimensionManager {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;" +
            "error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), method = "initDimension")
    private static void dimhoppertweaks$ignoreErroringDimensions(Logger logger, String s, Object o, Object o1) {}
}
