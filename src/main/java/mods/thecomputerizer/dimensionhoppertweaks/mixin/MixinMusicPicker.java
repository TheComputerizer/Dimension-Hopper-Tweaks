package mods.thecomputerizer.dimensionhoppertweaks.mixin;

import mods.thecomputerizer.musictriggers.client.MusicPicker;
import net.minecraft.world.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ResultOfMethodCallIgnored")
@Mixin(value = MusicPicker.class, remap = false)
public class MixinMusicPicker {

    @Inject(at = @At(value = "HEAD"), method = "checkDimensionList", cancellable = true)
    private static void checkDimensionList(int playerDim, String resourceList, CallbackInfoReturnable<Boolean> cir) {
        try {
            DimensionType.getById(playerDim).getName();
        } catch (Exception e) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
