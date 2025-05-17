package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipes.class)
public abstract class MixinShapedRecipes extends Impl<IRecipe> implements IRecipe {
    
    @Inject(at = @At("RETURN"), method = "getCraftingResult", cancellable = true)
    private void dimhoppertweaks$getCraftingResult(InventoryCrafting inv, CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(DelayedModAccess.getCraftingResult(this,inv,cir.getReturnValue()));
    }
}
