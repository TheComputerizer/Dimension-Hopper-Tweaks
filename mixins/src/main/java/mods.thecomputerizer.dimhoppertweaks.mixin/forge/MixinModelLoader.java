package mods.thecomputerizer.dimhoppertweaks.mixin.forge;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.MODID;

@Mixin(value = ModelLoader.class, remap = false)
public abstract class MixinModelLoader {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;" +
            "Ljava/lang/Throwable;)V"), method = "onPostBakeEvent")
    private void dimhoppertweaks$stopSpammingResourceErrors1(Logger logger, String s, Throwable throwable) {
        if(s.contains(MODID)) logger.error(s);
        else logger.debug("Ignored model and/or texture error");
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;" +
            "Ljava/lang/Object;Ljava/lang/Object;)V", ordinal = 0), method = "onPostBakeEvent")
    private void dimhoppertweaks$stopSpammingResourceErrors2(Logger logger, String s, Object o, Object o1) {
        if(o instanceof String && ((String)o).contains(MODID)) logger.error(s);
        else logger.debug("Ignored model and/or texture error");
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;" +
            "Ljava/lang/Object;Ljava/lang/Object;)V", ordinal = 1), method = "onPostBakeEvent")
    private void dimhoppertweaks$stopSpammingResourceErrors3(Logger logger, String s, Object o, Object o1) {
        if(o instanceof String && ((String)o).contains(MODID)) logger.error(s);
        else logger.debug("Ignored model and/or texture error");
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;" +
            "Ljava/lang/Object;Ljava/lang/Object;)V", ordinal = 2), method = "onPostBakeEvent")
    private void dimhoppertweaks$stopSpammingResourceErrors4(Logger logger, String s, Object o, Object o1) {
        if(o instanceof ResourceLocation && ((ResourceLocation)o).getNamespace().matches(MODID))
            logger.error(s);
        else logger.debug("Ignored model and/or texture error");
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;fatal(Ljava/lang/String;"+
            "Ljava/lang/Object;)V"), method = "onPostBakeEvent")
    private void dimhoppertweaks$stopSpammingResourceErrors5(Logger logger, String s, Object o) {
        if(o instanceof ModelResourceLocation && ((ModelResourceLocation)o).getNamespace().matches(MODID))
            logger.fatal(s);
        else logger.debug("Ignored model and/or texture error");
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;fatal(Ljava/lang/String;"+
            "Ljava/lang/Object;Ljava/lang/Object;)V"), method = "onPostBakeEvent")
    private void dimhoppertweaks$stopSpammingResourceErrors6(Logger logger, String s, Object o, Object o1) {
        if(o instanceof String && ((String)o).contains(MODID)) logger.error(s);
        else logger.debug("Ignored model and/or texture error");
    }
}