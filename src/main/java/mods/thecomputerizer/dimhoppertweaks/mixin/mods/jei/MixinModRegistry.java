package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import mezz.jei.collect.ListMultiMap;
import mezz.jei.plugins.jei.info.IngredientInfoRecipe;
import mezz.jei.startup.ModRegistry;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.IngredientInfoRecipeAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.ModRegistryAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Mixin(value = ModRegistry.class, remap = false)
public class MixinModRegistry implements ModRegistryAccess {

    @Shadow @Final private ListMultiMap<String,Object> recipes;
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Shadow @Final @Deprecated private List<Object> unsortedRecipes;

    @Override
    public void dimhoppertweaks$clearDescriptions(Collection<Object> objects) {
        if(!this.recipes.containsKey("jei.information")) return;
        List<Object> infoRecipes = this.recipes.get("jei.information");
        if(infoRecipes.isEmpty()) return;
        Iterator<Object> recipeItr = infoRecipes.iterator();
        while(recipeItr.hasNext()) {
            Object recipe = recipeItr.next();
            if(recipe instanceof IngredientInfoRecipe<?>) {
                IngredientInfoRecipeAccess access = (IngredientInfoRecipeAccess)recipe;
                for(Object obj : objects) {
                    if(access.dimhoppertweaks$removeIngredient(obj)) {
                        this.unsortedRecipes.remove(recipe);
                        recipeItr.remove();
                        break;
                    }
                }
            }
        }
    }
}
