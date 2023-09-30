package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.RecipeInfoTieredAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thelm.packagedexcrafting.recipe.RecipeInfoUltimate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Mixin(value = RecipeInfoUltimate.class, remap = false)
public class MixinRecipeInfoUltimate implements RecipeInfoTieredAccess {

    @Unique
    private final Set<String> dimhoppertweaks$stages = new HashSet<>();

    @Override
    public void dimhoppertweaks$setStages(Collection<String> stages) {
        this.dimhoppertweaks$stages.addAll(stages);
    }

    @Override
    public Collection<String> dimhoppertweaks$getStages() {
        return this.dimhoppertweaks$stages;
    }
}
