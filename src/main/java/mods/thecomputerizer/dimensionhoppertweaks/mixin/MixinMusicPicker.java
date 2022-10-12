package mods.thecomputerizer.dimensionhoppertweaks.mixin;

import mods.thecomputerizer.musictriggers.MusicTriggers;
import mods.thecomputerizer.musictriggers.client.MusicPicker;
import net.minecraft.world.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MusicPicker.class, remap = false)
public class MixinMusicPicker {

    @Inject(at = @At(value = "HEAD"), method = "checkDimensionList", cancellable = true)
    private static void checkDimensionList(int playerDim, String resourceList, CallbackInfoReturnable<Boolean> cir) {
        for(String resource : MusicTriggers.stringBreaker(resourceList,";")) {
            if ((playerDim + "").matches(resource)) {
                cir.setReturnValue(true);
                cir.cancel();
            }
        }
        try {
            cir.setReturnValue(MusicPicker.checkResourceList(DimensionType.getById(playerDim).getName(),resourceList,false));
        } catch (Exception e) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
