package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jeresources;

import jeresources.entry.*;
import jeresources.jei.JEIConfig;
import jeresources.jei.dungeon.DungeonCategory;
import jeresources.jei.dungeon.DungeonWrapper;
import jeresources.jei.mob.MobCategory;
import jeresources.jei.mob.MobWrapper;
import jeresources.jei.plant.PlantCategory;
import jeresources.jei.plant.PlantWrapper;
import jeresources.jei.worldgen.WorldGenCategory;
import jeresources.jei.worldgen.WorldGenWrapper;
import jeresources.registry.*;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;

import static jeresources.JEResources.PROXY;

@Mixin(value = JEIConfig.class, remap = false)
public abstract class MixinJEIConfig {

    @Shadow private static IJeiHelpers jeiHelpers;

    /**
     * @author The_Computerizer
     * @reason Remove enchantment and villager categories
     */
    @Overwrite
    public void register(@Nonnull IModRegistry registry) {
        registry.handleRecipes(WorldGenEntry.class,WorldGenWrapper::new,"jeresources.worldgen");
        registry.handleRecipes(PlantEntry.class,PlantWrapper::new,"jeresources.plant");
        registry.handleRecipes(MobEntry.class,MobWrapper::new,"jeresources.mob");
        registry.handleRecipes(DungeonEntry.class,DungeonWrapper::new,"jeresources.dungeon");
        PROXY.initCompatibility();
        registry.addRecipes(WorldGenRegistry.getInstance().getWorldGen(),"jeresources.worldgen");
        registry.addRecipes(PlantRegistry.getInstance().getAllPlants(),"jeresources.plant");
        registry.addRecipes(MobRegistry.getInstance().getMobs(),"jeresources.mob");
        registry.addRecipes(DungeonRegistry.getInstance().getDungeons(),"jeresources.dungeon");
    }

    /**
     * @author The_Computerizer
     * @reason Remove enchantment and villager categories
     */
    @Overwrite
    public void registerCategories(IRecipeCategoryRegistration registry) {
        jeiHelpers = registry.getJeiHelpers();
        registry.addRecipeCategories(new PlantCategory(),new WorldGenCategory(),new MobCategory(),new DungeonCategory());
    }
}