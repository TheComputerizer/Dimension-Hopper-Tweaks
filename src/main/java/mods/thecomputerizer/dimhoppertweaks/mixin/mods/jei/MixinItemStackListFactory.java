package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import mezz.jei.plugins.vanilla.ingredients.item.ItemStackListFactory;
import mezz.jei.startup.StackHelper;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

@Mixin(value = ItemStackListFactory.class, remap = false)
public class MixinItemStackListFactory {

    @Inject(at = @At("HEAD"), method = "addItemStack")
    private void dimhoppertweaks$maybeAddStack(StackHelper helper, ItemStack stack, List<ItemStack> list, Set<String> set, CallbackInfo ci) {
        DelayedModAccess.checkYeet(stack);
    }
}
