package mods.thecomputerizer.dimhoppertweaks.mixin.mods.avaritia;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import morph.avaritia.client.gui.GUIExtremeCrafting;
import morph.avaritia.client.gui.GUINeutroniumCompressor;
import morph.avaritia.compat.jei.AvaritiaJEIPlugin;
import morph.avaritia.compat.jei.compressor.CompressorRecipeWrapper;
import morph.avaritia.compat.jei.extreme.ExtremeCraftingCategory;
import morph.avaritia.compat.jei.extreme.ExtremeRecipeWrapper;
import morph.avaritia.container.ContainerExtremeCrafting;
import morph.avaritia.init.ModBlocks;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapedRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapelessRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = AvaritiaJEIPlugin.class, remap = false)
public abstract class MixinAvaritiaJEIPlugin {

    @Shadow public static IJeiHelpers jeiHelpers;

    @Shadow public static IDrawableStatic extreme_crafting;

    /**
     * @author The_Computerizer
     * @reason Remove compressor category
     */
    @Overwrite
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new ExtremeCraftingCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    /**
     * @author The_Computerizer
     * @reason Remove compressor category
     */
    @Overwrite
    public void register(IModRegistry registry) {
        jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        setupDrawables(guiHelper);
        registry.addRecipes(AvaritiaRecipeManager.EXTREME_RECIPES.values(),"Avatitia.Extreme");
        registry.handleRecipes(ExtremeShapedRecipe.class,ExtremeRecipeWrapper::new,"Avatitia.Extreme");
        registry.handleRecipes(ExtremeShapelessRecipe.class,ExtremeRecipeWrapper::new,"Avatitia.Extreme");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.extremeCraftingTable),"Avatitia.Extreme");
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerExtremeCrafting.class,"Avatitia.Extreme",1,81,82,36);
        registry.addRecipeClickArea(GUIExtremeCrafting.class, 175, 79, 28, 26,"Avatitia.Extreme");
        registry.addRecipes(AvaritiaRecipeManager.COMPRESSOR_RECIPES.values(),"Avatitia.Compressor");
        registry.handleRecipes(ICompressorRecipe.class,CompressorRecipeWrapper::new,"Avatitia.Compressor");
        registry.addRecipeClickArea(GUINeutroniumCompressor.class,62,35,22,15,"Avatitia.Compressor");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.neutronium_compressor), "Avatitia.Compressor");
    }

    /**
     * @author The_Computerizer
     * @reason Remove compressor category
     */
    @Overwrite
    private static void setupDrawables(IGuiHelper helper) {
        ResourceLocation location = new ResourceLocation("avaritia:textures/gui/extreme_jei.png");
        extreme_crafting = helper.createDrawable(location, 0, 0, 189, 163);
    }
}