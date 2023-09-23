package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.client.core.handler.HUDHandler;

import java.util.Objects;

@Mixin(value = HUDHandler.class, remap = false)
public class MixinHUDHandler {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/IItemHandler;" +
            "getStackInSlot(I)Lnet/minecraft/item/ItemStack;", ordinal = 0), method = "onDrawScreenPre")
    private static ItemStack dimhoppertweaks$redirectGetStackInSlot1(IItemHandler handler, int i) {
        return Objects.nonNull(handler) ? handler.getStackInSlot(i) : ItemStack.EMPTY;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/IItemHandler;" +
            "getStackInSlot(I)Lnet/minecraft/item/ItemStack;", ordinal = 1), method = "onDrawScreenPre")
    private static ItemStack dimhoppertweaks$redirectGetStackInSlot2(IItemHandler handler, int i) {
        return Objects.nonNull(handler) ? handler.getStackInSlot(i) : ItemStack.EMPTY;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/IItemHandler;" +
            "getStackInSlot(I)Lnet/minecraft/item/ItemStack;", ordinal = 2), method = "onDrawScreenPre")
    private static ItemStack dimhoppertweaks$redirectGetStackInSlot3(IItemHandler handler, int i) {
        return Objects.nonNull(handler) ? handler.getStackInSlot(i) : ItemStack.EMPTY;
    }
}
