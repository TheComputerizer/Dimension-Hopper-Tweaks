package mods.thecomputerizer.dimhoppertweaks.mixin.mods.crafttweaker;

import crafttweaker.mods.jei.JEIAddonPlugin;
import crafttweaker.mods.jei.JEIMod;
import crafttweaker.mods.jei.recipeWrappers.BrewingRecipeCWrapper;
import crafttweaker.mods.jei.recipeWrappers.CraftingRecipeWrapperShaped;
import crafttweaker.mods.jei.recipeWrappers.CraftingRecipeWrapperShapeless;
import mezz.jei.api.ingredients.VanillaTypes;
import mods.thecomputerizer.dimhoppertweaks.integration.jei.JeiActionSupplier;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static crafttweaker.mods.jei.JEI.DESCRIPTIONS;

@Mixin(value = JEIMod.class, remap = false)
public abstract class MixinJEIMod {

    @Inject(at = @At("TAIL"), method = "applyActions")
    private static void dimhoppertweaks$applyActions(CallbackInfo ci) {
        if(Objects.nonNull(JEIAddonPlugin.itemRegistry) && !DelayedModAccess.ADDED_ITEMS.isEmpty())
            JEIAddonPlugin.itemRegistry.addIngredientsAtRuntime(VanillaTypes.ITEM,DelayedModAccess.ADDED_ITEMS);
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