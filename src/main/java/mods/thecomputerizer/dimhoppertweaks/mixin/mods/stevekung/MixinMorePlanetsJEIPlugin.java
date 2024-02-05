package mods.thecomputerizer.dimhoppertweaks.mixin.mods.stevekung;

import mezz.jei.api.IModRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import stevekung.mods.moreplanets.init.MPBlocks;
import stevekung.mods.moreplanets.init.MPItems;
import stevekung.mods.moreplanets.integration.jei.ItemDescription;
import stevekung.mods.moreplanets.integration.jei.JEIRegistryHelper;
import stevekung.mods.moreplanets.integration.jei.MPJEIRecipes;
import stevekung.mods.moreplanets.integration.jei.MorePlanetsJEIPlugin;
import stevekung.mods.moreplanets.integration.jei.black_hole_storage.BlackHoleStorageRecipeWrapper;
import stevekung.mods.moreplanets.integration.jei.dark_energy_transform.DarkEnergyTransformRecipeWrapper;
import stevekung.mods.moreplanets.recipe.BlackHoleStorageRecipes;
import stevekung.mods.moreplanets.recipe.DarkEnergyRecipeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        JEIRegistryHelper.registerStackDisplayRecipe(new ItemStack(MPBlocks.DARK_ENERGY_CORE),
                "moreplanets.dark_energy_transform_recipes");
        JEIRegistryHelper.registerStackDisplayRecipe(new ItemStack(GCBlocks.nasaWorkbench),
                "moreplanets.black_hole_storage_recipes");
        JEIRegistryHelper.registerStackDisplayRecipe(new ItemStack(MPBlocks.CHEESE_SPORE_CRAFTING_TABLE),
                "minecraft.crafting");
        JEIRegistryHelper.registerStackDisplayRecipe(new ItemStack(MPBlocks.INFECTED_CRAFTING_TABLE),
                "minecraft.crafting");
        JEIRegistryHelper.registerStackDisplayRecipe(new ItemStack(MPBlocks.ALIEN_BERRY_CRAFTING_TABLE),
                "minecraft.crafting");
        JEIRegistryHelper.registerStackDisplayRecipe(new ItemStack(MPBlocks.INFECTED_FURNACE),
                "minecraft.smelting");
        JEIRegistryHelper.registerStackDisplayRecipe(new ItemStack(MPBlocks.TERRASTONE_FURNACE),
                "minecraft.smelting");
    }
}