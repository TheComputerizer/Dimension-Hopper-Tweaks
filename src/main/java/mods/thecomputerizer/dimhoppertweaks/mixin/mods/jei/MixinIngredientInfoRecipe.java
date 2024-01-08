package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import mezz.jei.plugins.jei.info.IngredientInfoRecipe;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.IngredientInfoRecipeAccess;
import mods.thecomputerizer.dimhoppertweaks.util.ItemUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(value = IngredientInfoRecipe.class, remap = false)
public class MixinIngredientInfoRecipe implements IngredientInfoRecipeAccess {

    @Shadow @Final private List<Object> ingredients;

    @Override
    public boolean dimhoppertweaks$removeIngredient(Object obj) {
        this.ingredients.removeIf(element -> (element instanceof ItemStack &&
                dimhoppertweaks$matchesItemStack((ItemStack) element, obj)) || (element instanceof FluidStack &&
                dimhoppertweaks$matchesFluidStack((FluidStack) element, obj)));
        return this.ingredients.isEmpty();
    }

    @Unique
    private boolean dimhoppertweaks$matchesItemStack(ItemStack stack, Object obj) {
        if(!(obj instanceof ItemStack)) return false;
        ItemStack toMatch = (ItemStack)obj;
        if(stack.getItem()==toMatch.getItem() && stack.getMetadata()==toMatch.getMetadata())
            return ItemUtil.tagsMatch(stack.getTagCompound(),toMatch.getTagCompound(),true);
        return false;
    }

    @Unique
    private boolean dimhoppertweaks$matchesFluidStack(FluidStack stack, Object obj) {
        if(!(obj instanceof FluidStack)) return false;
        FluidStack toMatch = (FluidStack)obj;
        return stack.getFluid()==toMatch.getFluid() && stack.amount==toMatch.amount;
    }
}
