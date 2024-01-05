package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.ingredients.IngredientBlacklistInternal;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import java.util.Set;

@Mixin(value = IngredientBlacklistInternal.class, remap = false)
public class MixinIngredientBlacklistInternal {

    @Shadow @Final private Set<String> ingredientBlacklist;

    /**
     * @author The_Computerizer
     * @reason Add full nbt check to blacklist
     */
    @Overwrite
    public <V> void addIngredientToBlacklist(V ingredient, IIngredientHelper<V> ingredientHelper) {
        String uniqueName = DelayedModAccess.getIngredientUid(ingredient,ingredientHelper);
        this.ingredientBlacklist.add(uniqueName);
    }

    /**
     * @author The_Computerizer
     * @reason Add full nbt check to blacklist
     */
    @Overwrite
    public <V> void removeIngredientFromBlacklist(V ingredient, IIngredientHelper<V> ingredientHelper) {
        String uniqueName = DelayedModAccess.getIngredientUid(ingredient,ingredientHelper);
        this.ingredientBlacklist.remove(uniqueName);
    }
}
