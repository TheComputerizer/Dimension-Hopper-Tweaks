package mods.thecomputerizer.dimhoppertweaks.mixin.forge;

import net.minecraftforge.fml.common.FMLModContainer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FMLModContainer.class,remap = false)
public class MixinFMLModContainer {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;log(Lorg/apache/logging/log4j/Level;"+
            "Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V"), method = "constructMod")
    private void dimhoppertweaks$noSignaturesForYou(Logger logger, Level level, String s, Object o, Object o1, Object o2) {}
}
