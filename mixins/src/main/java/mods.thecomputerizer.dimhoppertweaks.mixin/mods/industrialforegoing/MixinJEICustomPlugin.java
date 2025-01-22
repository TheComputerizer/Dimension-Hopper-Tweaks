package mods.thecomputerizer.dimhoppertweaks.mixin.mods.industrialforegoing;

import com.buuz135.industrial.book.BookCategory;
import com.buuz135.industrial.gui.conveyor.GuiConveyor;
import com.buuz135.industrial.jei.JEICustomPlugin;
import com.buuz135.industrial.jei.extractor.ExtractorRecipeCategory;
import com.buuz135.industrial.jei.extractor.ExtractorRecipeWrapper;
import com.buuz135.industrial.jei.fluiddictionary.FluidDictionaryCategory;
import com.buuz135.industrial.jei.fluiddictionary.FluidDictionaryWrapper;
import com.buuz135.industrial.jei.ghost.ConveyorGhostSlotHandler;
import com.buuz135.industrial.jei.laser.LaserRecipeCategory;
import com.buuz135.industrial.jei.laser.LaserRecipeWrapper;
import com.buuz135.industrial.jei.machineproduce.MachineProduceCategory;
import com.buuz135.industrial.jei.machineproduce.MachineProduceWrapper;
import com.buuz135.industrial.jei.manual.ManualCategory;
import com.buuz135.industrial.jei.manual.ManualWrapper;
import com.buuz135.industrial.jei.ore.OreSieveCategory;
import com.buuz135.industrial.jei.ore.OreSieveWrapper;
import com.buuz135.industrial.jei.petrifiedgen.PetrifiedBurnTimeCategory;
import com.buuz135.industrial.jei.petrifiedgen.PetrifiedBurnTimeWrapper;
import com.buuz135.industrial.jei.reactor.ReactorRecipeCategory;
import com.buuz135.industrial.jei.reactor.ReactorRecipeWrapper;
import com.buuz135.industrial.jei.sludge.SludgeRefinerRecipeCategory;
import com.buuz135.industrial.jei.sludge.SludgeRefinerRecipeWrapper;
import com.buuz135.industrial.jei.stonework.StoneWorkCategory;
import com.buuz135.industrial.jei.stonework.StoneWorkWrapper;
import com.buuz135.industrial.tile.block.CustomOrientedBlock;
import com.buuz135.industrial.tile.generator.PetrifiedFuelGeneratorTile;
import com.buuz135.industrial.tile.world.MaterialStoneWorkFactoryTile.Mode;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.buuz135.industrial.api.extractor.ExtractorEntry.EXTRACTOR_ENTRIES;
import static com.buuz135.industrial.api.recipe.BioReactorEntry.BIO_REACTOR_ENTRIES;
import static com.buuz135.industrial.api.recipe.FluidDictionaryEntry.FLUID_DICTIONARY_RECIPES;
import static com.buuz135.industrial.api.recipe.LaserDrillEntry.LASER_DRILL_UNIQUE_VALUES;
import static com.buuz135.industrial.api.recipe.ProteinReactorEntry.PROTEIN_REACTOR_ENTRIES;
import static com.buuz135.industrial.api.recipe.ore.OreFluidEntrySieve.ORE_FLUID_SIEVE;
import static com.buuz135.industrial.config.CustomConfiguration.enableBookEntriesInJEI;
import static com.buuz135.industrial.proxy.BlockRegistry.*;
import static com.buuz135.industrial.proxy.FluidsRegistry.*;
import static com.buuz135.industrial.proxy.ItemRegistry.artificalDye;
import static com.buuz135.industrial.proxy.ItemRegistry.bookManualItem;
import static com.buuz135.industrial.proxy.ItemRegistry.fertilizer;
import static com.buuz135.industrial.proxy.ItemRegistry.tinyDryRubber;
import static com.buuz135.industrial.tile.world.MaterialStoneWorkFactoryTile.Mode.NONE;
import static net.minecraft.init.Blocks.BROWN_MUSHROOM;
import static net.minecraft.init.Blocks.ICE;
import static net.minecraft.init.Blocks.PACKED_ICE;
import static net.minecraft.init.Blocks.RED_MUSHROOM;
import static net.minecraft.init.Items.FISH;
import static net.minecraft.init.Items.LAVA_BUCKET;
import static net.minecraft.init.Items.SNOWBALL;
import static net.minecraftforge.fluids.FluidRegistry.LAVA;
import static net.minecraftforge.fluids.FluidRegistry.WATER;

@Mixin(value = JEICustomPlugin.class, remap = false)
public abstract class MixinJEICustomPlugin {

    @Shadow private SludgeRefinerRecipeCategory sludgeRefinerRecipeCategory;
    @Shadow private ReactorRecipeCategory bioReactorRecipeCategory;
    @Shadow private ReactorRecipeCategory proteinReactorRecipeCategory;
    @Shadow private LaserRecipeCategory laserRecipeCategory;
    @Shadow private MachineProduceCategory machineProduceCategory;
    @Shadow private PetrifiedBurnTimeCategory petrifiedBurnTimeCategory;
    @Shadow private FluidDictionaryCategory fluidDictionaryCategory;
    @Shadow private StoneWorkCategory stoneWorkCategory;
    @Shadow private ExtractorRecipeCategory extractorRecipeCategory;
    @Shadow private ManualCategory manualCategory;
    @Shadow private OreSieveCategory oreSieveCategory;

    @Shadow public abstract List<StoneWorkWrapper> findAllStoneWorkOutputs(List<Mode> usedModes);

    /**
     * @author The_Computerizer
     * @reason Remove ore washer and fermenter categories
     */
    @Overwrite
    public void registerCategories(IRecipeCategoryRegistration registry) {
        if(sludgeRefinerBlock.isEnabled()) {
            this.sludgeRefinerRecipeCategory = new SludgeRefinerRecipeCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.sludgeRefinerRecipeCategory);
        }
        if(bioReactorBlock.isEnabled()) {
            this.bioReactorRecipeCategory = new ReactorRecipeCategory(registry.getJeiHelpers().getGuiHelper(),
                    "Bioreactor accepted items");
            registry.addRecipeCategories(this.bioReactorRecipeCategory);
        }
        if(proteinReactorBlock.isEnabled()) {
            this.proteinReactorRecipeCategory = new ReactorRecipeCategory(registry.getJeiHelpers().getGuiHelper(),
                    "Protein reactor accepted items");
            registry.addRecipeCategories(this.proteinReactorRecipeCategory);
        }
        if(laserBaseBlock.isEnabled() || laserDrillBlock.isEnabled()) {
            this.laserRecipeCategory = new LaserRecipeCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.laserRecipeCategory);
        }
        this.machineProduceCategory = new MachineProduceCategory(registry.getJeiHelpers().getGuiHelper());
        registry.addRecipeCategories(this.machineProduceCategory);
        if(petrifiedFuelGeneratorBlock.isEnabled()) {
            this.petrifiedBurnTimeCategory = new PetrifiedBurnTimeCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.petrifiedBurnTimeCategory);
        }
        if(fluidDictionaryConverterBlock.isEnabled() && !FLUID_DICTIONARY_RECIPES.isEmpty()) {
            this.fluidDictionaryCategory = new FluidDictionaryCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.fluidDictionaryCategory);
        }
        if(materialStoneWorkFactoryBlock.isEnabled()) {
            this.stoneWorkCategory = new StoneWorkCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.stoneWorkCategory);
        }
        if(treeFluidExtractorBlock.isEnabled()) {
            this.extractorRecipeCategory = new ExtractorRecipeCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.extractorRecipeCategory);
        }
        if(enableBookEntriesInJEI) {
            this.manualCategory = new ManualCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.manualCategory);
        }
        if(oreSieveBlock.isEnabled()) {
            this.oreSieveCategory = new OreSieveCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.oreSieveCategory);
        }
    }

    /**
     * @author The_Computerizer
     * @reason Remove ore washer and fermenter categories
     */
    @Overwrite
    public void register(IModRegistry registry) {
        if(sludgeRefinerBlock.isEnabled()) {
            int maxWeight = WeightedRandom.getTotalWeight(sludgeRefinerBlock.getItems());
            List<SludgeRefinerRecipeWrapper> wrappers = new ArrayList<>();
            sludgeRefinerBlock.getItems().forEach(item -> wrappers.add(new SludgeRefinerRecipeWrapper(item,maxWeight)));
            registry.addRecipes(wrappers,this.sludgeRefinerRecipeCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(sludgeRefinerBlock),this.sludgeRefinerRecipeCategory.getUid());
        }
        if(bioReactorBlock.isEnabled()) {
            List<ReactorRecipeWrapper> wrappers = new ArrayList<>();
            BIO_REACTOR_ENTRIES.forEach(entry -> wrappers.add(
                    new ReactorRecipeWrapper(entry.getStack(),BIOFUEL,bioReactorBlock.getBaseAmount())));
            registry.addRecipes(wrappers,this.bioReactorRecipeCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(bioReactorBlock),this.bioReactorRecipeCategory.getUid());
        }
        if(proteinReactorBlock.isEnabled()) {
            List<ReactorRecipeWrapper> wrappers = new ArrayList<>();
            PROTEIN_REACTOR_ENTRIES.forEach(entry -> wrappers.add(
                    new ReactorRecipeWrapper(entry.getStack(),PROTEIN,proteinReactorBlock.getBaseAmount())));
            registry.addRecipes(wrappers,this.proteinReactorRecipeCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(proteinReactorBlock),this.proteinReactorRecipeCategory.getUid());
        }
        if(laserBaseBlock.isEnabled() || laserDrillBlock.isEnabled()) {
            List<LaserRecipeWrapper> wrappers = new ArrayList<>();
            LASER_DRILL_UNIQUE_VALUES.forEach(entry -> wrappers.add(new LaserRecipeWrapper(entry)));
            registry.addRecipes(wrappers,this.laserRecipeCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(laserDrillBlock),this.laserRecipeCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(laserBaseBlock),this.laserRecipeCategory.getUid());
        }
        if(resourcefulFurnaceBlock.isEnabled())
            registry.addRecipeCatalyst(new ItemStack(resourcefulFurnaceBlock),"minecraft.smelting");
        if(potionEnervatorBlock.isEnabled())
            registry.addRecipeCatalyst(new ItemStack(potionEnervatorBlock),"minecraft.brewing");
        registry.addRecipes(Stream.of(new MachineProduceWrapper(sporesRecreatorBlock,
                new ItemStack(BROWN_MUSHROOM)),new MachineProduceWrapper(sporesRecreatorBlock,
                new ItemStack(RED_MUSHROOM)),new MachineProduceWrapper(sewageCompostSolidiferBlock,
                new ItemStack(fertilizer)),new MachineProduceWrapper(dyeMixerBlock,
                new ItemStack(artificalDye,1,32767)),
                new MachineProduceWrapper(lavaFabricatorBlock,new ItemStack(LAVA_BUCKET)),
                new MachineProduceWrapper(waterResourcesCollectorBlock,new ItemStack(FISH,1,32767)),
                new MachineProduceWrapper(mobRelocatorBlock,FluidUtil.getFilledBucket(new FluidStack(ESSENCE,1000))),
                new MachineProduceWrapper(cropRecolectorBlock,FluidUtil.getFilledBucket(new FluidStack(SLUDGE,1000))),
                new MachineProduceWrapper(waterCondensatorBlock,FluidUtil.getFilledBucket(new FluidStack(WATER,1000))),
                new MachineProduceWrapper(animalResourceHarvesterBlock, FluidUtil.getFilledBucket(new FluidStack(MILK,1000))),
                new MachineProduceWrapper(mobSlaughterFactoryBlock,FluidUtil.getFilledBucket(new FluidStack(MEAT,1000))),
                new MachineProduceWrapper(mobSlaughterFactoryBlock,FluidUtil.getFilledBucket(new FluidStack(PINK_SLIME,1000))),
                new MachineProduceWrapper(latexProcessingUnitBlock,new ItemStack(tinyDryRubber)),
                new MachineProduceWrapper(animalByproductRecolectorBlock,FluidUtil.getFilledBucket(new FluidStack(SEWAGE,1000))),
                new MachineProduceWrapper(lavaFabricatorBlock,FluidUtil.getFilledBucket(new FluidStack(LAVA,1000))),
                new MachineProduceWrapper(proteinReactorBlock,FluidUtil.getFilledBucket(new FluidStack(PROTEIN,1000))),
                new MachineProduceWrapper(frosterBlock,new ItemStack(SNOWBALL)),
                new MachineProduceWrapper(frosterBlock,new ItemStack(ICE)),
                new MachineProduceWrapper(frosterBlock,new ItemStack(PACKED_ICE)))
                        .filter(wrapper -> ((CustomOrientedBlock<?>)wrapper.getBlock()).isEnabled()).collect(Collectors.toList()),
                this.machineProduceCategory.getUid());
        if(materialStoneWorkFactoryBlock.isEnabled()) {
            List<StoneWorkWrapper> perfectWrappers = new ArrayList<>();
            List<StoneWorkWrapper> wrappers = findAllStoneWorkOutputs(new ArrayList<>());
            for(StoneWorkWrapper workWrapper : new ArrayList<>(wrappers)) {
                if(perfectWrappers.stream().noneMatch(stoneWorkWrapper -> workWrapper.getOutput().isItemEqual(stoneWorkWrapper.getOutput()))) {
                    boolean isSomeoneShorter = false;
                    for(StoneWorkWrapper workWrapperCompare : new ArrayList<>(wrappers)) {
                        if(workWrapper.getOutput().isItemEqual(workWrapperCompare.getOutput())) {
                            List<Mode> workWrapperCompareModes = new ArrayList<>(workWrapperCompare.getModes());
                            workWrapperCompareModes.removeIf(NONE::equals);
                            List<Mode> workWrapperModes = new ArrayList<>(workWrapper.getModes());
                            workWrapperModes.removeIf(NONE::equals);
                            if(workWrapperModes.size()>workWrapperCompareModes.size()) {
                                isSomeoneShorter = true;
                                break;
                            }
                        }
                    }
                    if(!isSomeoneShorter) perfectWrappers.add(workWrapper);
                }
            }
            registry.addRecipes(perfectWrappers,stoneWorkCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(materialStoneWorkFactoryBlock),stoneWorkCategory.getUid());
        }
        if(petrifiedFuelGeneratorBlock.isEnabled()) {
            List<PetrifiedBurnTimeWrapper> wrappers = new ArrayList<>();
            registry.getIngredientRegistry().getFuels().stream().filter(PetrifiedFuelGeneratorTile::acceptsInputStack)
                    .forEach(stack -> wrappers.add(new PetrifiedBurnTimeWrapper(stack,TileEntityFurnace.getItemBurnTime(stack))));
            registry.addRecipes(wrappers,this.petrifiedBurnTimeCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(petrifiedFuelGeneratorBlock),this.petrifiedBurnTimeCategory.getUid());
        }
        if(enableBookEntriesInJEI) {
            for(BookCategory category : BookCategory.values())
                registry.addRecipes(category.getEntries().values().stream().map(ManualWrapper::new).collect(Collectors.toList()),manualCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(bookManualItem),manualCategory.getUid());
        }
        if(Objects.nonNull(this.fluidDictionaryCategory)) {
            registry.addRecipeCatalyst(new ItemStack(fluidDictionaryConverterBlock),this.fluidDictionaryCategory.getUid());
            registry.addRecipes(FLUID_DICTIONARY_RECIPES.stream().map(FluidDictionaryWrapper::new)
                    .collect(Collectors.toList()),this.fluidDictionaryCategory.getUid());
        }
        if(Objects.nonNull(this.extractorRecipeCategory)) {
            registry.addRecipeCatalyst(new ItemStack(treeFluidExtractorBlock),this.extractorRecipeCategory.getUid());
            registry.addRecipes(EXTRACTOR_ENTRIES.stream().map(ExtractorRecipeWrapper::new)
                    .collect(Collectors.toList()),this.extractorRecipeCategory.getUid());
        }
        if(Objects.nonNull(this.oreSieveCategory)) {
            registry.addRecipeCatalyst(new ItemStack(oreSieveBlock), this.oreSieveCategory.getUid());
            registry.addRecipes(ORE_FLUID_SIEVE.stream().map(OreSieveWrapper::new)
                    .collect(Collectors.toList()),this.oreSieveCategory.getUid());
        }
        registry.addGhostIngredientHandler(GuiConveyor.class, new ConveyorGhostSlotHandler());
    }
}