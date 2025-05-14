package mods.thecomputerizer.dimhoppertweaks.mixin.mods.recipestages;

import com.blamejared.recipestages.recipes.RecipeStage;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RecipeStage.class, remap = false)
public abstract class MixinRecipeStage extends Impl<IRecipe> implements IRecipe {
    
    @Shadow private String tier;
    
    @Inject(at = @At("RETURN"), method = "isGoodForCrafting", cancellable = true)
    private void dimhoppertweaks$isGoodForCrafting(InventoryCrafting inv, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValueZ() || DelayedModAccess.getCraftingStages(inv).contains(this.tier));
    }
}