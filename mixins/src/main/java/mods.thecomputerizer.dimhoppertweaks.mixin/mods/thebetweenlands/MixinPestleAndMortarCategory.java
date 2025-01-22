package mods.thecomputerizer.dimhoppertweaks.mixin.mods.thebetweenlands;

import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.item.food.ItemAspectrusFruit;
import thebetweenlands.compat.jei.recipes.pam.PestleAndMortarCategory;

import java.util.Arrays;
import java.util.List;

import static mezz.jei.api.ingredients.VanillaTypes.ITEM;
import static thebetweenlands.common.registries.ItemRegistry.ASPECT_VIAL;
import static thebetweenlands.common.registries.ItemRegistry.DENTROTHYST_VIAL;

@Mixin(value = PestleAndMortarCategory.class, remap = false)
public abstract class MixinPestleAndMortarCategory {

    /**
     * @author The_Computerizer
     * @reason Fix render for aspectus fruit recipes
     */
    @Overwrite
    public void setRecipe(IRecipeLayout layout, IRecipeWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup group = layout.getItemStacks();
        group.init(0,true,0,6);
        group.init(1,true,44,6);
        group.init(2,false,88,6);
        List<ItemStack> inputs = ingredients.getInputs(ITEM).get(0);
        ItemStack pestle = ingredients.getInputs(ITEM).get(1).get(0);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("active",true);
        pestle.setTagCompound(compound);
        boolean isAspectus = inputs.get(0).getItem() instanceof ItemAspectrusFruit;
        group.set(0,inputs);
        group.set(1,pestle);
        if(isAspectus) {
            group.init(3,false,108,6);
            Item vial = DENTROTHYST_VIAL;
            group.set(2,Arrays.asList(new ItemStack(vial),new ItemStack(vial,1,2)));
            group.set(3,new ItemStack(ASPECT_VIAL));
        } else group.set(2,ingredients.getOutputs(ITEM).get(0));
    }
}