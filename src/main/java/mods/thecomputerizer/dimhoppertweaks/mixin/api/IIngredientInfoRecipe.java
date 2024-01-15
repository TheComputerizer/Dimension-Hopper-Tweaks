package mods.thecomputerizer.dimhoppertweaks.mixin.api;
public interface IIngredientInfoRecipe {

    /**
     * This needs to be Object since the ingredients can be either ItemStacks or FluidStacks
     */
    boolean dimhoppertweaks$removeIngredient(Object obj);
}