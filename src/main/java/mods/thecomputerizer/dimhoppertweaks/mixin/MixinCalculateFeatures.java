package mods.thecomputerizer.dimhoppertweaks.mixin;

import mods.thecomputerizer.musictriggers.util.CalculateFeatures;
import net.minecraft.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CalculateFeatures.class)
public class MixinCalculateFeatures {

    @Inject(at = @At(value = "HEAD"), method = "nbtChecker", cancellable = true)
    private static void nbtChecker(EntityLiving e, String nbt, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
        cir.cancel();
    }
}
