package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import baubles.api.BaublesApi;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = BaublesApi.class, remap = false)
public abstract class MixinBaublesApi {

    @Shadow public static IBaublesItemHandler getBaublesHandler(EntityPlayer player) {
        return null;
    }

    @Inject(at = @At("HEAD"), method = "getBaublesHandler", cancellable = true)
    private static void dimhoppertweaks$getBaublesHandler(EntityPlayer player, CallbackInfoReturnable<IBaublesItemHandler> cir) {
        if(Objects.nonNull(player) || Objects.isNull(BaublesCapabilities.CAPABILITY_BAUBLES)) cir.setReturnValue(null);
    }

    @Inject(at = @At("HEAD"), method = "getBaubles", cancellable = true)
    private static void dimhoppertweaks$getBauble(EntityPlayer player, CallbackInfoReturnable<IBaublesItemHandler> cir) {
        if(Objects.nonNull(player) || Objects.isNull(BaublesCapabilities.CAPABILITY_BAUBLES)) cir.setReturnValue(null);
    }

    @Inject(at = @At("HEAD"), method = "isBaubleEquipped", cancellable = true)
    private static void dimhoppertweaks$isBaubleEquipped(EntityPlayer player, Item bauble, CallbackInfoReturnable<Integer> cir) {
        if(Objects.isNull(getBaublesHandler(player))) cir.setReturnValue(-1);
    }
}