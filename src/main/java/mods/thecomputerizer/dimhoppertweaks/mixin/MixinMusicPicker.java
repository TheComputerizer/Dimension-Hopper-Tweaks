package mods.thecomputerizer.dimhoppertweaks.mixin;

import mods.thecomputerizer.musictriggers.MusicTriggers;
import mods.thecomputerizer.musictriggers.client.MusicPicker;
import net.minecraft.world.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MusicPicker.class, remap = false)
public abstract class MixinMusicPicker {

    @Inject(at = @At(value = "HEAD"), method = "checkDimensionList", cancellable = true)
    private static void checkDimensionList(int playerDim, String resourceList, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(mixinCheckDimensionList(playerDim, resourceList));
    }

    private static boolean mixinCheckDimensionList(int playerDim, String resourceList) {
        for(String resource : MusicTriggers.stringBreaker(resourceList,";")) if ((playerDim)==Integer.parseInt(resource)) return true;
        try {
            return MusicPicker.checkResourceList(DimensionType.getById(playerDim).getName(),resourceList,false);
        } catch (Exception e) {
            return false;
        }
    }
}
