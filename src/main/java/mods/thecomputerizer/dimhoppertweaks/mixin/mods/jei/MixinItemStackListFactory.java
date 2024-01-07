package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import mezz.jei.plugins.vanilla.ingredients.item.ItemStackListFactory;
import mezz.jei.startup.StackHelper;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

@Mixin(value = ItemStackListFactory.class, remap = false)
public class MixinItemStackListFactory {

    @Inject(at = @At("HEAD"), method = "create")
    private void dimhoppertweaks$resetYeetCount(StackHelper stackHelper, CallbackInfoReturnable<List<ItemStack>> cir) {
        DelayedModAccess.YEET_COUNT.set(0);
    }

    @Inject(at = @At("HEAD"), method = "addItemStack")
    private void dimhoppertweaks$maybeAddStack(StackHelper helper, ItemStack stack, List<ItemStack> list, Set<String> set, CallbackInfo ci) {
        DelayedModAccess.checkYeet(stack);
    }

    @Inject(at = @At("RETURN"), method = "create")
    private void dimhoppertweaks$logYeetCount(StackHelper stackHelper, CallbackInfoReturnable<List<ItemStack>> cir) {
        DelayedModAccess.finalizeYeeting();
    }
}
