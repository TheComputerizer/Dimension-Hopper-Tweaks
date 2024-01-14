package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import mezz.jei.plugins.jei.info.IngredientInfoRecipe;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.IngredientInfoRecipeAccess;
import mods.thecomputerizer.dimhoppertweaks.util.TagUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(value = IngredientInfoRecipe.class, remap = false)
public class MixinIngredientInfoRecipe implements IngredientInfoRecipeAccess {

    @Shadow @Final @Mutable private List<? super Object> ingredients;

    @Override
    public boolean dimhoppertweaks$removeIngredient(Object obj) {
        List<? super Object> updatedIngredients = new ArrayList<>(this.ingredients);
        updatedIngredients.removeIf(element -> (element instanceof ItemStack &&
                dimhoppertweaks$matchesItemStack((ItemStack) element, obj)) || (element instanceof FluidStack &&
                dimhoppertweaks$matchesFluidStack((FluidStack) element, obj)));
        this.ingredients = Collections.unmodifiableList(updatedIngredients);
        return this.ingredients.isEmpty();
    }

    @Unique
    private boolean dimhoppertweaks$matchesItemStack(ItemStack stack, Object obj) {
        if(!(obj instanceof ItemStack)) return false;
        ItemStack toMatch = (ItemStack)obj;
        if(stack.getItem()==toMatch.getItem() && stack.getMetadata()==toMatch.getMetadata())
            return TagUtil.tagsMatch(stack.getTagCompound(),toMatch.getTagCompound(),true);
        return false;
    }

    @Unique
    private boolean dimhoppertweaks$matchesFluidStack(FluidStack stack, Object obj) {
        if(!(obj instanceof FluidStack)) return false;
        FluidStack toMatch = (FluidStack)obj;
        return stack.getFluid()==toMatch.getFluid() && stack.amount==toMatch.amount;
    }
}
