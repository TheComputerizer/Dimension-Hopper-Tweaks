package mods.thecomputerizer.dimhoppertweaks.mixin.mods.extraplanets;

import com.mjr.extraplanets.jei.ExtraPlanetsJEI;
import com.mjr.extraplanets.jei.blocksmasher.BlockSmasherRecipeCategory;
import com.mjr.extraplanets.jei.blocksmasher.BlockSmasherRecipeMaker;
import com.mjr.extraplanets.jei.blocksmasher.BlockSmasherRecipeWrapper;
import com.mjr.extraplanets.jei.chemicalInjector.ChemicalInjectorRecipeCategory;
import com.mjr.extraplanets.jei.chemicalInjector.ChemicalInjectorRecipeMaker;
import com.mjr.extraplanets.jei.chemicalInjector.ChemicalInjectorRecipeWrapper;
import com.mjr.extraplanets.jei.crystallizer.CrystallizerRecipeCategory;
import com.mjr.extraplanets.jei.crystallizer.CrystallizerRecipeMaker;
import com.mjr.extraplanets.jei.crystallizer.CrystallizerRecipeWrapper;
import com.mjr.extraplanets.jei.densifier.DensifierRecipeCategory;
import com.mjr.extraplanets.jei.densifier.DensifierRecipeMaker;
import com.mjr.extraplanets.jei.densifier.DensifierRecipeWrapper;
import com.mjr.extraplanets.jei.purifier.PurifierRecipeCategory;
import com.mjr.extraplanets.jei.purifier.PurifierRecipeMaker;
import com.mjr.extraplanets.jei.purifier.PurifierRecipeWrapper;
import com.mjr.extraplanets.jei.rockets.tier10Electric.Tier10ElectricRocketRecipeCategory;
import com.mjr.extraplanets.jei.solarEvaporationChamber.SolarEvaporationChamberRecipeCategory;
import com.mjr.extraplanets.jei.solarEvaporationChamber.SolarEvaporationChamberRecipeMaker;
import com.mjr.extraplanets.jei.solarEvaporationChamber.SolarEvaporationChamberRecipeWrapper;
import com.mjr.extraplanets.jei.vehicles.marsRover.MarsRoverRecipeCategory;
import com.mjr.extraplanets.jei.vehicles.marsRover.MarsRoverRecipeMaker;
import com.mjr.extraplanets.jei.vehicles.marsRover.MarsRoverRecipeWrapper;
import com.mjr.extraplanets.jei.vehicles.venusRover.VenusRoverRecipeCategory;
import com.mjr.extraplanets.jei.vehicles.venusRover.VenusRoverRecipeMaker;
import com.mjr.extraplanets.jei.vehicles.venusRover.VenusRoverRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static com.mjr.extraplanets.Config.JEI_SUPPORT;
import static com.mjr.extraplanets.Config.RADIATION;
import static com.mjr.extraplanets.blocks.machines.ExtraPlanets_Machines.*;
import static micdoodle8.mods.galacticraft.core.GCBlocks.nasaWorkbench;

@Mixin(value = ExtraPlanetsJEI.class, remap = false)
public abstract class MixinExtraPlanetsJEI {

    /**
     * @author The_Computerizer
     * @reason Remove rocket categories
     */
    @Overwrite
    public void register(IModRegistry registry) {
        if(JEI_SUPPORT) {
            registry.handleRecipes(INasaWorkbenchRecipe.class,MarsRoverRecipeWrapper::new,"extraplanets.marsRover");
            registry.handleRecipes(INasaWorkbenchRecipe.class,VenusRoverRecipeWrapper::new,"extraplanets.venusRover");
            if(RADIATION) {
                registry.handleRecipes(BlockSmasherRecipeWrapper.class,recipe -> recipe,"extraplanets.blockSmasher");
                registry.handleRecipes(SolarEvaporationChamberRecipeWrapper.class,recipe -> recipe,"extraplanets.solarEvaporationChamber");
                registry.handleRecipes(ChemicalInjectorRecipeWrapper.class,recipe -> recipe,"extraplanets.chemicalInjector");
                registry.handleRecipes(CrystallizerRecipeWrapper.class,recipe -> recipe,"extraplanets.crystallizer");
                registry.handleRecipes(PurifierRecipeWrapper.class,recipe -> recipe,"extraplanets.purifier");
                registry.handleRecipes(DensifierRecipeWrapper.class,recipe -> recipe,"extraplanets.densifier");
            }
            registry.addRecipes(MarsRoverRecipeMaker.getRecipesList(),"extraplanets.marsRover");
            registry.addRecipes(VenusRoverRecipeMaker.getRecipesList(),"extraplanets.venusRover");
            if(RADIATION) {
                registry.addRecipes(BlockSmasherRecipeMaker.getRecipesList(),"extraplanets.blockSmasher");
                registry.addRecipes(SolarEvaporationChamberRecipeMaker.getRecipesList(),"extraplanets.solarEvaporationChamber");
                registry.addRecipes(ChemicalInjectorRecipeMaker.getRecipesList(),"extraplanets.chemicalInjector");
                registry.addRecipes(CrystallizerRecipeMaker.getRecipesList(),"extraplanets.crystallizer");
                registry.addRecipes(PurifierRecipeMaker.getRecipesList(),"extraplanets.purifier");
                registry.addRecipes(DensifierRecipeMaker.getRecipesList(),"extraplanets.densifier");
            }
            registry.addRecipeCatalyst(new ItemStack(nasaWorkbench),"extraplanets.marsRover","extraplanets.venusRover");
            if(RADIATION) {
                registry.addRecipeCatalyst(new ItemStack(BASIC_CHEMICAL_INJECTOR),"extraplanets.chemicalInjector");
                registry.addRecipeCatalyst(new ItemStack(BASIC_CRYSALLIZER),"extraplanets.crystallizer");
                registry.addRecipeCatalyst(new ItemStack(BASIC_DENSIFIER),"extraplanets.densifier");
                registry.addRecipeCatalyst(new ItemStack(BASIC_PURIFIER),"extraplanets.purifier");
                registry.addRecipeCatalyst(new ItemStack(BASIC_SMASHER),"extraplanets.blockSmasher");
                registry.addRecipeCatalyst(new ItemStack(BASIC_SOLAR_EVAPORTATION_CHAMBER),"extraplanets.solarEvaporationChamber");
            }
        }
    }

    /**
     * @author The_Computerizer
     * @reason Remove rocket categories
     */
    @Overwrite
    public void registerCategories(IRecipeCategoryRegistration registry) {
        if(JEI_SUPPORT) {
            IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
            registry.addRecipeCategories(new MarsRoverRecipeCategory(guiHelper),new VenusRoverRecipeCategory(guiHelper),
                    new Tier10ElectricRocketRecipeCategory(guiHelper));
            if(RADIATION)
                registry.addRecipeCategories(new BlockSmasherRecipeCategory(guiHelper),
                        new SolarEvaporationChamberRecipeCategory(guiHelper),
                        new ChemicalInjectorRecipeCategory(guiHelper),new CrystallizerRecipeCategory(guiHelper),
                        new PurifierRecipeCategory(guiHelper),new DensifierRecipeCategory(guiHelper));
        }
    }
}