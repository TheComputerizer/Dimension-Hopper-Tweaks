package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.ingredients.IngredientFilter;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = IngredientFilter.class, remap = false)
public class MixinIngredientFilter {

    @Redirect(at = @At(value = "INVOKE", target = "Lmezz/jei/api/ingredients/IIngredientHelper;"+
            "getUniqueId(Ljava/lang/Object;)Ljava/lang/String;"),
            method = "getMatches")
    private <V> String dimhoppertweaks$enableNBTMatches(IIngredientHelper<V> helper, V ingredient) {
        return DelayedModAccess.getIngredientUid(ingredient,helper);
    }
}
