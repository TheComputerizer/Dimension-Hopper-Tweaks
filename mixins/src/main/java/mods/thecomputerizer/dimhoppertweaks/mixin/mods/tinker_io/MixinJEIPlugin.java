package mods.thecomputerizer.dimhoppertweaks.mixin.mods.tinker_io;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import tinker_io.gui.GuiOreCrusher;
import tinker_io.helper.OreCrusherRecipe;
import tinker_io.plugins.jei.JEIPlugin;
import tinker_io.plugins.jei.oreCrusher.OreCrusherRecipeCategory;
import tinker_io.plugins.jei.oreCrusher.OreCrusherRecipeHandler;
import tinker_io.registry.BlockRegistry;
import tinker_io.registry.OreCrusherRecipeRegister;

import static tinker_io.plugins.jei.oreCrusher.OreCrusherRecipeCategory.CATEGORY;

@Mixin(value = JEIPlugin.class, remap = false)
public abstract class MixinJEIPlugin {

    @Shadow public static OreCrusherRecipeCategory oreCrusherRecipeCategory;

    @Shadow public static IJeiHelpers jeiHelpers;

    /**
     * @author The_Computerizer
     * @reason Remove fuel input and smart output categories
     */
    @Overwrite
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        oreCrusherRecipeCategory = new OreCrusherRecipeCategory(guiHelper);
        registry.addRecipeCategories(oreCrusherRecipeCategory);
    }

    /**
     * @author The_Computerizer
     * @reason Remove fuel input and smart output categories
     */
    @Overwrite
    public void register(IModRegistry registry) {
        jeiHelpers = registry.getJeiHelpers();
        registry.handleRecipes(OreCrusherRecipe.class,new OreCrusherRecipeHandler(),CATEGORY);
        registry.addRecipes(OreCrusherRecipeRegister.oreCrusherRecipes,CATEGORY);
        registry.addRecipeClickArea(GuiOreCrusher.class,82,35,24,15,CATEGORY);
        registry.addRecipeCatalyst(new ItemStack(BlockRegistry.oreCrusher),CATEGORY);
    }
}