package mods.thecomputerizer.dimhoppertweaks.mixin.mods.avaritia;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import morph.avaritia.client.gui.GUIExtremeCrafting;
import morph.avaritia.compat.jei.AvaritiaJEIPlugin;
import morph.avaritia.compat.jei.extreme.ExtremeCraftingCategory;
import morph.avaritia.compat.jei.extreme.ExtremeRecipeWrapper;
import morph.avaritia.container.ContainerExtremeCrafting;
import morph.avaritia.recipe.extreme.ExtremeShapedRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapelessRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static morph.avaritia.init.ModBlocks.extremeCraftingTable;
import static morph.avaritia.recipe.AvaritiaRecipeManager.EXTREME_RECIPES;

@SuppressWarnings("SpellCheckingInspection") @Mixin(value = AvaritiaJEIPlugin.class, remap = false)
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
        registry.addRecipes(EXTREME_RECIPES.values(),"Avatitia.Extreme");
        registry.handleRecipes(ExtremeShapedRecipe.class,ExtremeRecipeWrapper::new,"Avatitia.Extreme");
        registry.handleRecipes(ExtremeShapelessRecipe.class,ExtremeRecipeWrapper::new,"Avatitia.Extreme");
        registry.addRecipeCatalyst(new ItemStack(extremeCraftingTable),"Avatitia.Extreme");
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerExtremeCrafting.class,"Avatitia.Extreme",1,81,82,36);
        registry.addRecipeClickArea(GUIExtremeCrafting.class,175,79,28,26,"Avatitia.Extreme");
    }

    /**
     * @author The_Computerizer
     * @reason Remove compressor category
     */
    @Overwrite
    private static void setupDrawables(IGuiHelper helper) {
        ResourceLocation location = new ResourceLocation("avaritia:textures/gui/extreme_jei.png");
        extreme_crafting = helper.createDrawable(location,0,0,189,163);
    }
}