package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import baubles.api.BaublesApi;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = InventoryUtil.class, remap = false)
public class MixinInventoryUtil {

    @Inject(at = @At("HEAD"), method = "actuallyGetBauble", cancellable = true)
    private static void dimhoppertweaks$actuallyGetBauble(Item item, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        if(Objects.isNull(BaublesApi.getBaublesHandler(player))) cir.setReturnValue(ItemStack.EMPTY);
    }
}
