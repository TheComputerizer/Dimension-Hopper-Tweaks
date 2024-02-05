package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import mezz.jei.Internal;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mezz.jei.gui.GuiHelper;
import mezz.jei.plugins.vanilla.VanillaPlugin;
import mezz.jei.plugins.vanilla.brewing.BrewingRecipeCategory;
import mezz.jei.plugins.vanilla.brewing.BrewingRecipeMaker;
import mezz.jei.plugins.vanilla.brewing.PotionSubtypeInterpreter;
import mezz.jei.plugins.vanilla.crafting.*;
import mezz.jei.plugins.vanilla.furnace.FurnaceSmeltingCategory;
import mezz.jei.plugins.vanilla.furnace.SmeltingRecipeMaker;
import mezz.jei.plugins.vanilla.ingredients.enchant.EnchantDataHelper;
import mezz.jei.plugins.vanilla.ingredients.enchant.EnchantDataListFactory;
import mezz.jei.plugins.vanilla.ingredients.enchant.EnchantDataRenderer;
import mezz.jei.plugins.vanilla.ingredients.enchant.EnchantedBookCache;
import mezz.jei.plugins.vanilla.ingredients.fluid.FluidStackHelper;
import mezz.jei.plugins.vanilla.ingredients.fluid.FluidStackListFactory;
import mezz.jei.plugins.vanilla.ingredients.fluid.FluidStackRenderer;
import mezz.jei.plugins.vanilla.ingredients.item.ItemStackHelper;
import mezz.jei.plugins.vanilla.ingredients.item.ItemStackListFactory;
import mezz.jei.plugins.vanilla.ingredients.item.ItemStackRenderer;
import mezz.jei.runtime.JeiHelpers;
import mezz.jei.startup.StackHelper;
import mezz.jei.transfer.PlayerRecipeTransferHandler;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.*;

@SuppressWarnings("deprecation")
@Mixin(value = VanillaPlugin.class, remap = false)
public abstract class MixinVanillaPlugin {

    @Unique private static final Map<String,Function<Class<?>,Constructor<?>>> dimhoppertweaks$CONSTRUCTOR_FINDERS = dimhoppertweaks$mapConstructors();

    @Unique
    private static Map<String,Function<Class<?>,Constructor<?>>> dimhoppertweaks$mapConstructors() {
        Map<String,Function<Class<?>,Constructor<?>>> map = new HashMap<>();
        map.put("InventoryEffectRendererGuiHandler",clazz -> {
            try {
                return clazz.getDeclaredConstructor();
            } catch(NoSuchMethodException ex) {
                DHTRef.LOGGER.error("Unable to find constructor for class {}",clazz.getName(),ex);
            }
            return null;
        });
        map.put("RecipeBookGuiHandler",clazz -> {
            try {
                return clazz.getDeclaredConstructor(Class.class);
            } catch(NoSuchMethodException ex) {
                DHTRef.LOGGER.error("Unable to find constructor for class {}",clazz.getName(),ex);
            }
            return null;
        });
        return map;
    }

    @Shadow @Nullable private ISubtypeRegistry subtypeRegistry;

    /**
     * @author The_Computerizer
     * @reason Remove spawn egg interpreter
     */
    @Overwrite
    public void registerSubtypes(ISubtypeRegistry subtypeRegistry) {
        this.subtypeRegistry = subtypeRegistry;
        subtypeRegistry.registerSubtypeInterpreter(Items.TIPPED_ARROW,PotionSubtypeInterpreter.INSTANCE);
        subtypeRegistry.registerSubtypeInterpreter(Items.POTIONITEM,PotionSubtypeInterpreter.INSTANCE);
        subtypeRegistry.registerSubtypeInterpreter(Items.SPLASH_POTION,PotionSubtypeInterpreter.INSTANCE);
        subtypeRegistry.registerSubtypeInterpreter(Items.LINGERING_POTION,PotionSubtypeInterpreter.INSTANCE);
        subtypeRegistry.registerSubtypeInterpreter(Items.BANNER,stack -> {
            EnumDyeColor baseColor = ItemBanner.getBaseColor(stack);
            return baseColor.toString();
        });
        subtypeRegistry.registerSubtypeInterpreter(Items.ENCHANTED_BOOK, (itemStack) -> {
            List<String> enchNames = new ArrayList<>();
            NBTTagList enchants = ItemEnchantedBook.getEnchantments(itemStack);
            for(NBTBase based : enchants) {
                if(based instanceof NBTTagCompound) {
                    NBTTagCompound tag = (NBTTagCompound)based;
                    int id = tag.getShort("id");
                    Enchantment ench = Enchantment.getEnchantmentByID(id);
                    if(Objects.nonNull(ench)) {
                        String enchUID = ench.getName()+".lvl"+tag.getShort("lvl");
                        enchNames.add(enchUID);
                    }
                }
            }
            enchNames.sort(null);
            return enchNames.toString();
        });
    }

    /**
     * @author The_Computerizer
     * @reason Just putting this here in case I want to change it later
     */
    @Overwrite
    public void registerIngredients(IModIngredientRegistration ingredientRegistration) {
        Preconditions.checkState(Objects.nonNull(this.subtypeRegistry));
        StackHelper internalHelper = Internal.getStackHelper();
        ItemStackListFactory factory = new ItemStackListFactory(this.subtypeRegistry);
        List<ItemStack> stacks = factory.create(internalHelper);
        ItemStackHelper helper = new ItemStackHelper(internalHelper);
        ItemStackRenderer renderer = new ItemStackRenderer();
        ingredientRegistration.register(VanillaTypes.ITEM,stacks,helper,renderer);
        List<FluidStack> fluids = FluidStackListFactory.create();
        FluidStackHelper fluidHelper = new FluidStackHelper();
        FluidStackRenderer fluidRenderer = new FluidStackRenderer();
        ingredientRegistration.register(VanillaTypes.FLUID,fluids,fluidHelper,fluidRenderer);
        List<EnchantmentData> enchants = EnchantDataListFactory.create();
        EnchantedBookCache enchantCache = new EnchantedBookCache();
        MinecraftForge.EVENT_BUS.register(enchantCache);
        EnchantDataHelper enchantHelper = new EnchantDataHelper(enchantCache,helper);
        EnchantDataRenderer enchantRenderer = new EnchantDataRenderer(renderer,enchantCache);
        ingredientRegistration.register(VanillaTypes.ENCHANT,enchants,enchantHelper,enchantRenderer);
    }
    
    /**
     * @author The_Computerizer
     * @reason Remove anvil and furnace fuel categories
     */
    @Overwrite
    public void registerCategories(IRecipeCategoryRegistration registry) {
        JeiHelpers jeiHelpers = Internal.getHelpers();
        GuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registry.addRecipeCategories(new CraftingRecipeCategory(guiHelper),new FurnaceSmeltingCategory(guiHelper),
                new BrewingRecipeCategory(guiHelper));
    }

    /**
     * @author The_Computerizer
     * @reason Remove anvil and furnace fuel categories
     */
    @Overwrite
    public void register(IModRegistry registry) {
        IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
        IJeiHelpers helpers = registry.getJeiHelpers();
        registry.addRecipes(CraftingRecipeChecker.getValidRecipes(helpers),"minecraft.crafting");
        registry.addRecipes(SmeltingRecipeMaker.getFurnaceRecipes(helpers),"minecraft.smelting");
        registry.addRecipes(BrewingRecipeMaker.getBrewingRecipes(ingredientRegistry),"minecraft.brewing");
        registry.addRecipes(TippedArrowRecipeMaker.getTippedArrowRecipes(),"minecraft.crafting");
        registry.handleRecipes(ShapedOreRecipe.class,recipe ->
                new ShapedOreRecipeWrapper(helpers,recipe),"minecraft.crafting");
        registry.handleRecipes(ShapedRecipes.class,recipe ->
                new ShapedRecipesWrapper(helpers,recipe),"minecraft.crafting");
        registry.handleRecipes(ShapelessOreRecipe.class,recipe ->
                new ShapelessRecipeWrapper<>(helpers,recipe),"minecraft.crafting");
        registry.handleRecipes(ShapelessRecipes.class,recipe ->
                new ShapelessRecipeWrapper<>(helpers,recipe),"minecraft.crafting");
        registry.addRecipeClickArea(GuiCrafting.class,88,32,28,23,"minecraft.crafting");
        registry.addRecipeClickArea(GuiInventory.class,137,29,10,13,"minecraft.crafting");
        registry.addRecipeClickArea(GuiBrewingStand.class,97,16,14,30,"minecraft.brewing");
        registry.addRecipeClickArea(GuiFurnace.class,78,32,28,23,"minecraft.smelting");
        IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();
        transferRegistry.addRecipeTransferHandler(ContainerWorkbench.class,"minecraft.crafting",1,9,10,36);
        transferRegistry.addRecipeTransferHandler(new PlayerRecipeTransferHandler(
                helpers.recipeTransferHandlerHelper()),"minecraft.crafting");
        transferRegistry.addRecipeTransferHandler(ContainerFurnace.class,"minecraft.smelting",0,1,3,36);
        transferRegistry.addRecipeTransferHandler(ContainerBrewingStand.class,"minecraft.brewing",0,4,5,36);
        registry.addRecipeCatalyst(new ItemStack(Blocks.CRAFTING_TABLE),"minecraft.crafting");
        registry.addRecipeCatalyst(new ItemStack(Blocks.FURNACE),"minecraft.smelting");
        registry.addRecipeCatalyst(new ItemStack(Items.BREWING_STAND),"minecraft.brewing");
        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        blacklist.addIngredientToBlacklist(new ItemStack(Items.SKULL,1,3));
        blacklist.addIngredientToBlacklist(new ItemStack(Items.ENCHANTED_BOOK,1,32767));
        dimhoppertweaks$addAdvancedGuiHandler(registry,"InventoryEffectRendererGuiHandler");
        dimhoppertweaks$addAdvancedGuiHandler(registry,"RecipeBookGuiHandler",GuiInventory.class);
        dimhoppertweaks$addAdvancedGuiHandler(registry,"RecipeBookGuiHandler",GuiCrafting.class);
    }

    @Unique
    private void dimhoppertweaks$addAdvancedGuiHandler(IModRegistry registry, String className, Object ... args) {
        Object instance = DelayedModAccess.instantiateInaccessibleClass("mezz.jei.plugins.vanilla."+className,
                dimhoppertweaks$CONSTRUCTOR_FINDERS.get(className),args);
        if(instance instanceof IAdvancedGuiHandler<?>)
            registry.addAdvancedGuiHandlers((IAdvancedGuiHandler<?>)instance);
    }
}