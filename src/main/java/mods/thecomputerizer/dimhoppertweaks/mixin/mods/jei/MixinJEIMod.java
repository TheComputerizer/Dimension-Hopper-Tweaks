package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import crafttweaker.mods.jei.JEIMod;
import crafttweaker.mods.jei.recipeWrappers.BrewingRecipeCWrapper;
import crafttweaker.mods.jei.recipeWrappers.CraftingRecipeWrapperShaped;
import crafttweaker.mods.jei.recipeWrappers.CraftingRecipeWrapperShapeless;
import mods.thecomputerizer.dimhoppertweaks.integration.jei.JeiActionSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static crafttweaker.mods.jei.JEI.DESCRIPTIONS;

@Mixin(value = JEIMod.class, remap = false)
public abstract class MixinJEIMod {

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