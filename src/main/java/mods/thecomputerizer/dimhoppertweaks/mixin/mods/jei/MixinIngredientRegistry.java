package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import mezz.jei.gui.ingredients.IIngredientListElement;
import mezz.jei.ingredients.IngredientFilter;
import mezz.jei.ingredients.IngredientRegistry;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = IngredientRegistry.class, remap = false)
public class MixinIngredientRegistry {

    @Redirect(at = @At(value = "INVOKE", target = "Lmezz/jei/ingredients/IngredientFilter;"+
            "findMatchingElements(Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/List;"),
            method = "lambda$removeIngredientsAtRuntime$2")
    private <V> List<IIngredientListElement<V>> dimhoppertweaks$enableNBTRemovals(
            IngredientFilter filter, IIngredientListElement<V> searchElementUid) {
        return filter.getMatches(searchElementUid,DelayedModAccess::getIngredientUid);
    }
}
