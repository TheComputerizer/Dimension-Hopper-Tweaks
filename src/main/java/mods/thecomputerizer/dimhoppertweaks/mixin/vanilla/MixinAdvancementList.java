package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.advancements.AdvancementList;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AdvancementList.class)
public abstract class MixinAdvancementList {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;)V",
            remap = false), method = "loadAdvancements")
    private void dimhoppertweaks$redirectError(Logger logger, String s) {
        DHTRef.LOGGER.debug("Ignoring advancement error");
    }
}