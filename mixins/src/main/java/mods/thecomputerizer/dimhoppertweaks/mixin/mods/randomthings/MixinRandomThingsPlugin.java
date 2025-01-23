package mods.thecomputerizer.dimhoppertweaks.mixin.mods.randomthings;

import lumien.randomthings.client.gui.GuiImbuingStation;
import lumien.randomthings.container.ContainerImbuingStation;
import lumien.randomthings.handler.compability.jei.DescriptionHandler;
import lumien.randomthings.handler.compability.jei.RandomThingsPlugin;
import lumien.randomthings.handler.compability.jei.imbuing.ImbuingRecipeWrapper;
import lumien.randomthings.recipes.imbuing.ImbuingRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static lumien.randomthings.block.ModBlocks.imbuingStation;
import static lumien.randomthings.block.ModBlocks.lotus;
import static lumien.randomthings.recipes.imbuing.ImbuingRecipeHandler.imbuingRecipes;

@Mixin(value = RandomThingsPlugin.class, remap = false)
public abstract class MixinRandomThingsPlugin {

    @Shadow private IJeiHelpers jeiHelpers;
    @Shadow public static IStackHelper stackHelper;
    @Shadow public static String IMBUE_ID;

    /**
     * @author The_Computerizer
     * @reason Remove anvil recipes
     */
    @Overwrite
    public void register(IModRegistry registry) {
        this.jeiHelpers = registry.getJeiHelpers();
        stackHelper = this.jeiHelpers.getStackHelper();
        registry.handleRecipes(ImbuingRecipe.class,recipe -> new ImbuingRecipeWrapper(recipe.getIngredients(),
                recipe.toImbue(),recipe.getResult()),IMBUE_ID);
        registry.addRecipeCatalyst(new ItemStack(imbuingStation),IMBUE_ID);
        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
        recipeTransferRegistry.addRecipeTransferHandler(ContainerImbuingStation.class,IMBUE_ID,0,4,5,36);
        registry.addRecipes(imbuingRecipes,IMBUE_ID);
        this.jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(lotus));
        registry.addRecipeClickArea(GuiImbuingStation.class,99,54,22,16,IMBUE_ID);
        DescriptionHandler.addDescriptions(registry);
    }
}