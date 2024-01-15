package mods.thecomputerizer.dimhoppertweaks.mixin.mods.silentlib;

import net.minecraft.item.ItemStack;
import net.silentchaos512.lib.guidebook.GuideBook;
import net.silentchaos512.lib.guidebook.IGuidePage;
import net.silentchaos512.lib.guidebook.misc.GuideBookUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = GuideBookUtils.class, remap = false)
public abstract class MixinGuideBookUtils {

    @Inject(at = @At("HEAD"), method = "findFirstPageForStack", cancellable = true)
    private static void dimhoppertweaks$findFirstPageForStack(
            GuideBook book, ItemStack stack, CallbackInfoReturnable<IGuidePage> cir) {
        if(Objects.isNull(stack)) cir.setReturnValue(null);
    }
}