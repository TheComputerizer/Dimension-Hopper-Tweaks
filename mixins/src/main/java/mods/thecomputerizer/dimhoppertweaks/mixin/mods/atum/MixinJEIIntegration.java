package mods.thecomputerizer.dimhoppertweaks.mixin.mods.atum;

import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import com.teammetallurgy.atum.api.recipe.spinningwheel.ISpinningWheelRecipe;
import com.teammetallurgy.atum.integration.jei.JEIIntegration;
import com.teammetallurgy.atum.integration.jei.quern.QuernRecipeCategory;
import com.teammetallurgy.atum.integration.jei.quern.QuernRecipeWrapper;
import com.teammetallurgy.atum.integration.jei.spinningwheel.SpinningWheelRecipeCategory;
import com.teammetallurgy.atum.integration.jei.spinningwheel.SpinningWheelRecipeWrapper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static com.teammetallurgy.atum.api.recipe.RecipeHandlers.quernRecipes;
import static com.teammetallurgy.atum.api.recipe.RecipeHandlers.spinningWheelRecipes;
import static com.teammetallurgy.atum.init.AtumBlocks.LIMESTONE_FURNACE;
import static com.teammetallurgy.atum.init.AtumBlocks.QUERN;
import static com.teammetallurgy.atum.init.AtumBlocks.SPINNING_WHEEL;
import static com.teammetallurgy.atum.init.AtumItems.EMMER_DOUGH;
import static com.teammetallurgy.atum.utils.AtumRegistry.HIDE_LIST;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(value = JEIIntegration.class, remap = false)
public abstract class MixinJEIIntegration {

    @Shadow protected abstract void addInfo(ItemStack stack, IModRegistry registry);

    /**
     * @author The_Computerizer
     * @reason Remove kiln category
     */
    @Overwrite
    public void register(IModRegistry registry) {
        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        for(ItemStack stack : HIDE_LIST) blacklist.addIngredientToBlacklist(stack);
        registry.addRecipeCatalyst(new ItemStack(QUERN),"atum.quern");
        registry.handleRecipes(IQuernRecipe.class,recipe -> new QuernRecipeWrapper(registry.getJeiHelpers().getGuiHelper(),
                recipe.getRegistryName(),recipe.getInput(),recipe.getOutput(),recipe.getRotations()),"atum.quern");
        registry.addRecipes(quernRecipes.getValuesCollection(),"atum.quern");
        registry.addRecipeCatalyst(new ItemStack(SPINNING_WHEEL),"atum.spinningwheel");
        registry.handleRecipes(ISpinningWheelRecipe.class,recipe -> new SpinningWheelRecipeWrapper(recipe.getRegistryName(),
                recipe.getInput(),recipe.getOutput(),recipe.getRotations()),"atum.spinningwheel");
        registry.addRecipes(spinningWheelRecipes.getValuesCollection(),"atum.spinningwheel");
        registry.addRecipeCatalyst(new ItemStack(LIMESTONE_FURNACE),"minecraft.smelting","minecraft.fuel");
        this.addInfo(new ItemStack(EMMER_DOUGH), registry);
    }

    /**
     * @author The_Computerizer
     * @reason Remove kiln category
     */
    @Overwrite
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new QuernRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SpinningWheelRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }
}