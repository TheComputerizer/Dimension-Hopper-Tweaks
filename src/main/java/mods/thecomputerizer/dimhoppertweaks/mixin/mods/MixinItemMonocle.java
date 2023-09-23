package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;

import java.util.Objects;

@Mixin(value = ItemMonocle.class, remap = false)
public class MixinItemMonocle {

    @Redirect(at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;" +
            "getStackInSlot(I)Lnet/minecraft/item/ItemStack;"), method = "hasMonocle")
    private static ItemStack dimhoppertweaks$redirectHasMonocle(IBaublesItemHandler handler, int i) {
        return Objects.nonNull(handler) ? handler.getStackInSlot(i) : ItemStack.EMPTY;
    }
}
