package mods.thecomputerizer.dimhoppertweaks.mixin.mods.packagedexcrafting;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IRecipeInfoTiered;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thelm.packagedexcrafting.recipe.RecipeInfoBasic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Mixin(value = RecipeInfoBasic.class, remap = false)
public abstract class MixinRecipeInfoBasic implements IRecipeInfoTiered {

    @Unique private final Set<String> dimhoppertweaks$stages = new HashSet<>();

    @Override
    public void dimhoppertweaks$setStages(Collection<String> stages) {
        this.dimhoppertweaks$stages.addAll(stages);
    }

    @Override
    public Collection<String> dimhoppertweaks$getStages() {
        return this.dimhoppertweaks$stages;
    }
}