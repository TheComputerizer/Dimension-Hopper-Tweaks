package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mods.thecomputerizer.dimhoppertweaks.recipes.LightningStrikeRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static bedrockcraft.ModItems.components;
import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.MODID;
import static mods.thecomputerizer.dimhoppertweaks.registry.ItemRegistry.STARGATE_ADDRESSER;
import static net.minecraft.init.Items.AIR;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS;

@JEIPlugin
public class DHTJeiPlugin implements IModPlugin {

    public static ItemStack getCornerStack(int dimension) {
        switch(dimension) {
            case -28: return getItemStack("galacticraftcore:basic_block_moon",4); //moon
            case -1502: return getItemStack("extraplanets:phobos",2); //phobos
            case -31: return getItemStack("galacticraftplanets:venus",2); //venus
            case -30: return getItemStack("galacticraftplanets:asteroids_block",0); //asteroids
            case -29: return getItemStack("galacticraftplanets:mars",9); //mars
            case 816: return getItemStack("aoa3:lunar_block",0); //lunalus
            case -1506: return getItemStack("extraplanets:ganymede",2); //ganymede
            case -1501: return getItemStack("extraplanets:europa",2); //europa
            case -1500: return getItemStack("extraplanets:io",2); //io
            case -15: return getItemStack("extraplanets:jupiter",2); //jupiter
            case -1510: return getItemStack("extraplanets:titania",2); //titania
            case -1509: return getItemStack("extraplanets:oberon",2); //oberon
            case -1508: return getItemStack("extraplanets:titan",8); //titan
            case -16: return getItemStack("extraplanets:saturn",2); //saturn
            case 66: return getItemStack("erebus:umberstone",0); //erebus
            case -19: return getItemStack("extraplanets:pluto",2); //pluto
            default: return getItemStack("minecraft:stone",0); //overworld
        }
    }

    private static ItemStack getItemStack(String regName, int meta) {
        ResourceLocation itemRes = new ResourceLocation(regName);
        Item item = ITEMS.containsKey(itemRes) ? ITEMS.getValue(itemRes) : AIR;
        return new ItemStack(Objects.nonNull(item) ? item : AIR,1,meta);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new StargateCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new LightningStrikeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(IModRegistry registry) {
        String stargateID = MODID+".ancientStargate";
        registry.addRecipeCatalyst(new ItemStack(STARGATE_ADDRESSER),stargateID);
        registry.handleRecipes(StargateRecipe.class,StargatePreviewWrapper::new,stargateID);
        registry.addRecipes(getStargateRecipes(),stargateID);
        String lightningID = MODID+".lightningStrike";
        registry.addRecipeCatalyst(new ItemStack(components,1,9),lightningID);
        registry.handleRecipes(LightningStrikeRecipe.class,LightningStrikeWrapper::new,lightningID);
        registry.addRecipes(LightningStrikeRecipe.getRecipes(),lightningID);
        String tileworkerID = MODID+".tileWorker";
        AutoInfusionRecipeTransferHandle.register(registry.getRecipeTransferRegistry());
    }

    private List<StargateRecipe> getStargateRecipes() {
        List<StargateRecipe> inputs = new ArrayList<>();
        addInput(inputs,-28,2);
        addInput(inputs,-1502,3);
        addInput(inputs,-31,3);
        addInput(inputs,-30,3);
        addInput(inputs,-29,3);
        addInput(inputs,0,3);
        addInput(inputs,816,4);
        addInput(inputs,-1506,5);
        addInput(inputs,-1501,5);
        addInput(inputs,-1500,5);
        addInput(inputs,-15,5);
        addInput(inputs,-1510,6);
        addInput(inputs,-1509,6);
        addInput(inputs,-1508,6);
        addInput(inputs,-16,6);
        addInput(inputs,66,8);
        addInput(inputs,-19,9);
        return inputs;
    }

    private void addInput(List<StargateRecipe> list, int dimension, int level) {
        List<ItemStack> inputs = new ArrayList<>();
        inputs.add(getSchematicStack(level));
        ItemStack heavyDutyStack = getItemStack("contenttweaker:rocket_block_"+level,0);
        heavyDutyStack.setCount(44);
        inputs.add(heavyDutyStack);
        ItemStack cornerStack = getCornerStack(dimension);
        cornerStack.setCount(4);
        inputs.add(cornerStack);
        list.add(new StargateRecipe(dimension,inputs));
    }

    private ItemStack getSchematicStack(int level) {
        int meta = level==2 ? 1 : 0;
        String regname = level==2 ? "galacticraftcore:" : (level==3 ? "galacticraftplanets:" : "extraplanets:");
        regname+="schematic";
        if(level>3) regname+="_tier"+level;
        return getItemStack(regname,meta);
    }
}
