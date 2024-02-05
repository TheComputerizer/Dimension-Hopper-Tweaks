package mods.thecomputerizer.dimhoppertweaks.mixin.mods.industrialforegoing;

import com.buuz135.industrial.api.extractor.ExtractorEntry;
import com.buuz135.industrial.api.recipe.BioReactorEntry;
import com.buuz135.industrial.api.recipe.FluidDictionaryEntry;
import com.buuz135.industrial.api.recipe.LaserDrillEntry;
import com.buuz135.industrial.api.recipe.ProteinReactorEntry;
import com.buuz135.industrial.api.recipe.ore.OreFluidEntrySieve;
import com.buuz135.industrial.book.BookCategory;
import com.buuz135.industrial.config.CustomConfiguration;
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
import com.buuz135.industrial.proxy.BlockRegistry;
import com.buuz135.industrial.proxy.FluidsRegistry;
import com.buuz135.industrial.proxy.ItemRegistry;
import com.buuz135.industrial.tile.block.CustomOrientedBlock;
import com.buuz135.industrial.tile.generator.PetrifiedFuelGeneratorTile;
import com.buuz135.industrial.tile.world.MaterialStoneWorkFactoryTile.Mode;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.fluids.FluidRegistry;
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
        if(BlockRegistry.sludgeRefinerBlock.isEnabled()) {
            this.sludgeRefinerRecipeCategory = new SludgeRefinerRecipeCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.sludgeRefinerRecipeCategory);
        }
        if(BlockRegistry.bioReactorBlock.isEnabled()) {
            this.bioReactorRecipeCategory = new ReactorRecipeCategory(registry.getJeiHelpers().getGuiHelper(),
                    "Bioreactor accepted items");
            registry.addRecipeCategories(this.bioReactorRecipeCategory);
        }
        if(BlockRegistry.proteinReactorBlock.isEnabled()) {
            this.proteinReactorRecipeCategory = new ReactorRecipeCategory(registry.getJeiHelpers().getGuiHelper(),
                    "Protein reactor accepted items");
            registry.addRecipeCategories(this.proteinReactorRecipeCategory);
        }
        if(BlockRegistry.laserBaseBlock.isEnabled() || BlockRegistry.laserDrillBlock.isEnabled()) {
            this.laserRecipeCategory = new LaserRecipeCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.laserRecipeCategory);
        }
        this.machineProduceCategory = new MachineProduceCategory(registry.getJeiHelpers().getGuiHelper());
        registry.addRecipeCategories(this.machineProduceCategory);
        if(BlockRegistry.petrifiedFuelGeneratorBlock.isEnabled()) {
            this.petrifiedBurnTimeCategory = new PetrifiedBurnTimeCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.petrifiedBurnTimeCategory);
        }
        if(BlockRegistry.fluidDictionaryConverterBlock.isEnabled() && !FluidDictionaryEntry.FLUID_DICTIONARY_RECIPES.isEmpty()) {
            this.fluidDictionaryCategory = new FluidDictionaryCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.fluidDictionaryCategory);
        }
        if(BlockRegistry.materialStoneWorkFactoryBlock.isEnabled()) {
            this.stoneWorkCategory = new StoneWorkCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.stoneWorkCategory);
        }
        if(BlockRegistry.treeFluidExtractorBlock.isEnabled()) {
            this.extractorRecipeCategory = new ExtractorRecipeCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.extractorRecipeCategory);
        }
        if(CustomConfiguration.enableBookEntriesInJEI) {
            this.manualCategory = new ManualCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(this.manualCategory);
        }
        if(BlockRegistry.oreSieveBlock.isEnabled()) {
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
        if(BlockRegistry.sludgeRefinerBlock.isEnabled()) {
            int maxWeight = WeightedRandom.getTotalWeight(BlockRegistry.sludgeRefinerBlock.getItems());
            List<SludgeRefinerRecipeWrapper> wrappers = new ArrayList<>();
            BlockRegistry.sludgeRefinerBlock.getItems().forEach(item -> wrappers.add(new SludgeRefinerRecipeWrapper(item,maxWeight)));
            registry.addRecipes(wrappers,this.sludgeRefinerRecipeCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(BlockRegistry.sludgeRefinerBlock),this.sludgeRefinerRecipeCategory.getUid());
        }
        if(BlockRegistry.bioReactorBlock.isEnabled()) {
            List<ReactorRecipeWrapper> wrappers = new ArrayList<>();
            BioReactorEntry.BIO_REACTOR_ENTRIES.forEach(entry -> wrappers.add(
                    new ReactorRecipeWrapper(entry.getStack(),FluidsRegistry.BIOFUEL,BlockRegistry.bioReactorBlock.getBaseAmount())));
            registry.addRecipes(wrappers,this.bioReactorRecipeCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(BlockRegistry.bioReactorBlock),this.bioReactorRecipeCategory.getUid());
        }
        if(BlockRegistry.proteinReactorBlock.isEnabled()) {
            List<ReactorRecipeWrapper> wrappers = new ArrayList<>();
            ProteinReactorEntry.PROTEIN_REACTOR_ENTRIES.forEach(entry -> wrappers.add(
                    new ReactorRecipeWrapper(entry.getStack(),FluidsRegistry.PROTEIN,BlockRegistry.proteinReactorBlock.getBaseAmount())));
            registry.addRecipes(wrappers,this.proteinReactorRecipeCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(BlockRegistry.proteinReactorBlock),this.proteinReactorRecipeCategory.getUid());
        }
        if(BlockRegistry.laserBaseBlock.isEnabled() || BlockRegistry.laserDrillBlock.isEnabled()) {
            List<LaserRecipeWrapper> wrappers = new ArrayList<>();
            LaserDrillEntry.LASER_DRILL_UNIQUE_VALUES.forEach(entry -> wrappers.add(new LaserRecipeWrapper(entry)));
            registry.addRecipes(wrappers,this.laserRecipeCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(BlockRegistry.laserDrillBlock),this.laserRecipeCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(BlockRegistry.laserBaseBlock),this.laserRecipeCategory.getUid());
        }
        if(BlockRegistry.resourcefulFurnaceBlock.isEnabled())
            registry.addRecipeCatalyst(new ItemStack(BlockRegistry.resourcefulFurnaceBlock),"minecraft.smelting");
        if(BlockRegistry.potionEnervatorBlock.isEnabled())
            registry.addRecipeCatalyst(new ItemStack(BlockRegistry.potionEnervatorBlock),"minecraft.brewing");
        registry.addRecipes(Stream.of(new MachineProduceWrapper(BlockRegistry.sporesRecreatorBlock,
                new ItemStack(Blocks.BROWN_MUSHROOM)),new MachineProduceWrapper(BlockRegistry.sporesRecreatorBlock,
                new ItemStack(Blocks.RED_MUSHROOM)),new MachineProduceWrapper(BlockRegistry.sewageCompostSolidiferBlock,
                new ItemStack(ItemRegistry.fertilizer)),new MachineProduceWrapper(BlockRegistry.dyeMixerBlock,
                new ItemStack(ItemRegistry.artificalDye,1,32767)),
                new MachineProduceWrapper(BlockRegistry.lavaFabricatorBlock,new ItemStack(Items.LAVA_BUCKET)),
                new MachineProduceWrapper(BlockRegistry.waterResourcesCollectorBlock,new ItemStack(Items.FISH,1,32767)),
                new MachineProduceWrapper(BlockRegistry.mobRelocatorBlock,FluidUtil.getFilledBucket(new FluidStack(FluidsRegistry.ESSENCE,1000))),
                new MachineProduceWrapper(BlockRegistry.cropRecolectorBlock,FluidUtil.getFilledBucket(new FluidStack(FluidsRegistry.SLUDGE,1000))),
                new MachineProduceWrapper(BlockRegistry.waterCondensatorBlock,FluidUtil.getFilledBucket(new FluidStack(FluidRegistry.WATER,1000))),
                new MachineProduceWrapper(BlockRegistry.animalResourceHarvesterBlock, FluidUtil.getFilledBucket(new FluidStack(FluidsRegistry.MILK,1000))),
                new MachineProduceWrapper(BlockRegistry.mobSlaughterFactoryBlock,FluidUtil.getFilledBucket(new FluidStack(FluidsRegistry.MEAT,1000))),
                new MachineProduceWrapper(BlockRegistry.mobSlaughterFactoryBlock,FluidUtil.getFilledBucket(new FluidStack(FluidsRegistry.PINK_SLIME,1000))),
                new MachineProduceWrapper(BlockRegistry.latexProcessingUnitBlock,new ItemStack(ItemRegistry.tinyDryRubber)),
                new MachineProduceWrapper(BlockRegistry.animalByproductRecolectorBlock,FluidUtil.getFilledBucket(new FluidStack(FluidsRegistry.SEWAGE,1000))),
                new MachineProduceWrapper(BlockRegistry.lavaFabricatorBlock,FluidUtil.getFilledBucket(new FluidStack(FluidRegistry.LAVA,1000))),
                new MachineProduceWrapper(BlockRegistry.proteinReactorBlock,FluidUtil.getFilledBucket(new FluidStack(FluidsRegistry.PROTEIN,1000))),
                new MachineProduceWrapper(BlockRegistry.frosterBlock,new ItemStack(Items.SNOWBALL)),
                new MachineProduceWrapper(BlockRegistry.frosterBlock,new ItemStack(Blocks.ICE)),
                new MachineProduceWrapper(BlockRegistry.frosterBlock,new ItemStack(Blocks.PACKED_ICE)))
                        .filter(wrapper -> ((CustomOrientedBlock<?>)wrapper.getBlock()).isEnabled()).collect(Collectors.toList()),
                this.machineProduceCategory.getUid());
        if(BlockRegistry.materialStoneWorkFactoryBlock.isEnabled()) {
            List<StoneWorkWrapper> perfectWrappers = new ArrayList<>();
            List<StoneWorkWrapper> wrappers = findAllStoneWorkOutputs(new ArrayList<>());
            for(StoneWorkWrapper workWrapper : new ArrayList<>(wrappers)) {
                if(perfectWrappers.stream().noneMatch(stoneWorkWrapper -> workWrapper.getOutput().isItemEqual(stoneWorkWrapper.getOutput()))) {
                    boolean isSomoneShorter = false;
                    for(StoneWorkWrapper workWrapperCompare : new ArrayList<>(wrappers)) {
                        if(workWrapper.getOutput().isItemEqual(workWrapperCompare.getOutput())) {
                            List<Mode> workWrapperCompareModes = new ArrayList<>(workWrapperCompare.getModes());
                            workWrapperCompareModes.removeIf(Mode.NONE::equals);
                            List<Mode> workWrapperModes = new ArrayList<>(workWrapper.getModes());
                            workWrapperModes.removeIf(Mode.NONE::equals);
                            if(workWrapperModes.size()>workWrapperCompareModes.size()) {
                                isSomoneShorter = true;
                                break;
                            }
                        }
                    }
                    if(!isSomoneShorter) perfectWrappers.add(workWrapper);
                }
            }
            registry.addRecipes(perfectWrappers, stoneWorkCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(BlockRegistry.materialStoneWorkFactoryBlock), stoneWorkCategory.getUid());
        }

        if(BlockRegistry.petrifiedFuelGeneratorBlock.isEnabled()) {
            List<PetrifiedBurnTimeWrapper> wrappers = new ArrayList<>();
            registry.getIngredientRegistry().getFuels().stream().filter(PetrifiedFuelGeneratorTile::acceptsInputStack)
                    .forEach(stack -> wrappers.add(new PetrifiedBurnTimeWrapper(stack,TileEntityFurnace.getItemBurnTime(stack))));
            registry.addRecipes(wrappers,this.petrifiedBurnTimeCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(BlockRegistry.petrifiedFuelGeneratorBlock),this.petrifiedBurnTimeCategory.getUid());
        }
        if(CustomConfiguration.enableBookEntriesInJEI) {
            for(BookCategory category : BookCategory.values())
                registry.addRecipes(category.getEntries().values().stream().map(ManualWrapper::new).collect(Collectors.toList()),manualCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(ItemRegistry.bookManualItem),manualCategory.getUid());
        }
        if(Objects.nonNull(this.fluidDictionaryCategory)) {
            registry.addRecipeCatalyst(new ItemStack(BlockRegistry.fluidDictionaryConverterBlock),this.fluidDictionaryCategory.getUid());
            registry.addRecipes(FluidDictionaryEntry.FLUID_DICTIONARY_RECIPES.stream().map(FluidDictionaryWrapper::new)
                    .collect(Collectors.toList()),this.fluidDictionaryCategory.getUid());
        }
        if(Objects.nonNull(this.extractorRecipeCategory)) {
            registry.addRecipeCatalyst(new ItemStack(BlockRegistry.treeFluidExtractorBlock),this.extractorRecipeCategory.getUid());
            registry.addRecipes(ExtractorEntry.EXTRACTOR_ENTRIES.stream().map(ExtractorRecipeWrapper::new)
                    .collect(Collectors.toList()), this.extractorRecipeCategory.getUid());
        }
        if(Objects.nonNull(this.oreSieveCategory)) {
            registry.addRecipeCatalyst(new ItemStack(BlockRegistry.oreSieveBlock),this.oreSieveCategory.getUid());
            registry.addRecipes(OreFluidEntrySieve.ORE_FLUID_SIEVE.stream().map(OreSieveWrapper::new)
                    .collect(Collectors.toList()), this.oreSieveCategory.getUid());
        }
        registry.addGhostIngredientHandler(GuiConveyor.class, new ConveyorGhostSlotHandler());
    }
}