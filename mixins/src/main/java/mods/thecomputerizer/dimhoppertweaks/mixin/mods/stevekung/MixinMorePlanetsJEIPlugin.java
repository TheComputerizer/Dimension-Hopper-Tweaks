package mods.thecomputerizer.dimhoppertweaks.mixin.mods.stevekung;

import mezz.jei.api.IModRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import stevekung.mods.moreplanets.integration.jei.ItemDescription;
import stevekung.mods.moreplanets.integration.jei.JEIRegistryHelper;
import stevekung.mods.moreplanets.integration.jei.MorePlanetsJEIPlugin;
import stevekung.mods.moreplanets.integration.jei.black_hole_storage.BlackHoleStorageRecipeWrapper;
import stevekung.mods.moreplanets.integration.jei.dark_energy_transform.DarkEnergyTransformRecipeWrapper;
import stevekung.mods.moreplanets.recipe.BlackHoleStorageRecipes;
import stevekung.mods.moreplanets.recipe.DarkEnergyRecipeData;

import static micdoodle8.mods.galacticraft.core.GCBlocks.nasaWorkbench;
import static stevekung.mods.moreplanets.init.MPBlocks.*;

@Mixin(value = MorePlanetsJEIPlugin.class, remap = false)
public abstract class MixinMorePlanetsJEIPlugin {

    /**
     * @author The_Computerizer
     * @reason Remove anvil recipes
     */
    @Overwrite
    public void register(IModRegistry registry) {
        JEIRegistryHelper.registry = registry;
        JEIRegistryHelper.guiHelper = registry.getJeiHelpers().getGuiHelper();
        JEIRegistryHelper.vanillaRecipe = registry.getJeiHelpers().getVanillaRecipeFactory();
        DelayedModAccess.callInaccessibleMethod(ItemDescription.class,"init");
        JEIRegistryHelper.registerRecipeHandlers(DarkEnergyRecipeData.class,DarkEnergyTransformRecipeWrapper::new,
                "moreplanets.dark_energy_transform_recipes");
        JEIRegistryHelper.registerRecipeHandlers(INasaWorkbenchRecipe.class,BlackHoleStorageRecipeWrapper::new,
                "moreplanets.black_hole_storage_recipes");
        JEIRegistryHelper.registerRecipe(DarkEnergyRecipeData.getRecipeList(),
                "moreplanets.dark_energy_transform_recipes");
        JEIRegistryHelper.registerRecipe(BlackHoleStorageRecipes.getRecipesList(),
                "moreplanets.black_hole_storage_recipes");
        JEIRegistryHelper.registerStackDisplayRecipe(new ItemStack(DARK_ENERGY_CORE),
                "moreplanets.dark_energy_transform_recipes");
        JEIRegistryHelper.registerStackDisplayRecipe(new ItemStack(nasaWorkbench),
                "moreplanets.black_hole_storage_recipes");
        JEIRegistryHelper.registerStackDisplayRecipe(new ItemStack(CHEESE_SPORE_CRAFTING_TABLE),
                "minecraft.crafting");
        JEIRegistryHelper.registerStackDisplayRecipe(new ItemStack(INFECTED_CRAFTING_TABLE),
                "minecraft.crafting");
        JEIRegistryHelper.registerStackDisplayRecipe(new ItemStack(ALIEN_BERRY_CRAFTING_TABLE),
                "minecraft.crafting");
        JEIRegistryHelper.registerStackDisplayRecipe(new ItemStack(INFECTED_FURNACE),
                "minecraft.smelting");
        JEIRegistryHelper.registerStackDisplayRecipe(new ItemStack(TERRASTONE_FURNACE),
                "minecraft.smelting");
    }
}