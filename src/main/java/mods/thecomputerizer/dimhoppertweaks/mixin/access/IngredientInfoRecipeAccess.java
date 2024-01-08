package mods.thecomputerizer.dimhoppertweaks.mixin.access;
public interface IngredientInfoRecipeAccess {

    /**
     * This needs to be Object since the ingredients can be either ItemStacks or FluidStacks
     */
    boolean dimhoppertweaks$removeIngredient(Object obj);
}
