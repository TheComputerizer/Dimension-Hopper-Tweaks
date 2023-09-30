package mods.thecomputerizer.dimhoppertweaks.mixin.access;

import java.util.Collection;

public interface RecipeInfoTieredAccess {

    void dimhoppertweaks$setStages(Collection<String> stages);
    Collection<String> dimhoppertweaks$getStages();
}
