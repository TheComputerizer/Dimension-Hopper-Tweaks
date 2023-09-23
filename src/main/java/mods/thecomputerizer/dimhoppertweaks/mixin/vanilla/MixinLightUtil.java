package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = LightUtil.class, remap = false)
public class MixinLightUtil {

    @Inject(at = @At("HEAD"), method = "putBakedQuad")
    private static void dimhoppertweaks$putBakedQuad(IVertexConsumer consumer, BakedQuad quad, CallbackInfo ci) {
        Constants.LOGGER.error("CONSUMER CLASS IS {}",Objects.nonNull(consumer) ? consumer.getClass().getName() : "NULL");
        Constants.LOGGER.error("BAKED QUAD CLASS IS {}",Objects.nonNull(quad) ? quad.getClass().getName() : "NULL");
    }
}
