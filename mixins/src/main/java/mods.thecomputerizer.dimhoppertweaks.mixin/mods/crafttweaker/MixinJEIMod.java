package mods.thecomputerizer.dimhoppertweaks.mixin.mods.crafttweaker;

import crafttweaker.mods.jei.JEIMod;
import crafttweaker.mods.jei.recipeWrappers.BrewingRecipeCWrapper;
import crafttweaker.mods.jei.recipeWrappers.CraftingRecipeWrapperShaped;
import crafttweaker.mods.jei.recipeWrappers.CraftingRecipeWrapperShapeless;
import mods.thecomputerizer.dimhoppertweaks.integration.jei.JeiActionSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static crafttweaker.mods.jei.JEI.DESCRIPTIONS;
import static crafttweaker.mods.jei.JEIAddonPlugin.itemRegistry;
import static mezz.jei.api.ingredients.VanillaTypes.ITEM;
import static mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess.ADDED_ITEMS;

@Mixin(value = JEIMod.class, remap = false)
public abstract class MixinJEIMod {

    @Inject(at = @At("TAIL"), method = "applyActions")
    private static void dimhoppertweaks$applyActions(CallbackInfo ci) {
        if(Objects.nonNull(itemRegistry) && !ADDED_ITEMS.isEmpty())
            itemRegistry.addIngredientsAtRuntime(ITEM,ADDED_ITEMS);
    }

    /**
     * @author The_Computerizer
     * @reason Delay adding descriptions until after the removals are injected
     */
    @Overwrite
    public static void onRegistered() {
        JeiActionSupplier.queueDescriptionAdditions(DESCRIPTIONS);
        BrewingRecipeCWrapper.registerBrewingRecipe();
        CraftingRecipeWrapperShapeless.registerCraftingRecipes();
        CraftingRecipeWrapperShaped.registerCraftingRecipes();
    }
}