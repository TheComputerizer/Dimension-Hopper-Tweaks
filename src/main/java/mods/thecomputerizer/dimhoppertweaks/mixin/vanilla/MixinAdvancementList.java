package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.advancements.AdvancementList;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AdvancementList.class)
public class MixinAdvancementList {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;)V"), method = "loadAdvancements")
    private void dimhoppertweaks$redirectError(Logger logger, String s) {
        Constants.LOGGER.debug("Ignoring advancement error");
    }
}
