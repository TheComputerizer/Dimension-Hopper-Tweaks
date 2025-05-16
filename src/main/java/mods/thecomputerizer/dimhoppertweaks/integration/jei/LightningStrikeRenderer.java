package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import mods.thecomputerizer.dimhoppertweaks.recipes.LightningStrikeRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.init.Blocks.GRASS;

public class LightningStrikeRenderer extends ProxyWorldRender {

    private static final Map<LightningStrikeRecipe,LightningStrikeRenderer> RENDERS = new HashMap<>();
    private static boolean initialized = false;

    public static LightningStrikeRenderer getForRecipe(LightningStrikeRecipe recipe) {
        if(!initialized) initRenderMap();
        return RENDERS.get(recipe);
    }

    public static void initRenderMap() {
        for(LightningStrikeRecipe recipe : LightningStrikeRecipe.getRecipes())
            RENDERS.put(recipe,new LightningStrikeRenderer(recipe));
        initialized = true;
    }

    private final LightningStrikeRecipe recipe;
    private final IBlockState groundState;

    public LightningStrikeRenderer(LightningStrikeRecipe recipe) {
        super();
        this.recipe = recipe;
        this.groundState = GRASS.getDefaultState();
        setUpPosMaps();
    }

    private void setUpPosMaps() {
        addPlatformPositions();
        addEntities();
    }

    private void addEntities() {
        addRenderableEntity(new EntityLightningBolt(this.proxyWorld,0d,1d,0d,true));
        addRenderableEntity(this.recipe.getEntity(this.proxyWorld,0d,1d,0d));
        addEntityItem(0d,0d,this.recipe.getCatalystStack());
        int inputIndex = 0;
        List<ItemStack> stacks = this.recipe.getInputStacks();
        for(int x=-1; x<=1; x++) {
            for(int z=-1; z<=1; z++) {
                if(stacks.size()>inputIndex) addEntityItem(x,z,stacks.get(inputIndex));
                inputIndex++;
            }
        }
    }

    private void addEntityItem(double x, double z, ItemStack stack) {
        EntityItem item = new EntityItem(this.proxyWorld,x,1d,z,stack);
        item.makeFakeItem();
        addRenderableEntity(item);
    }

    private void addPlatformPositions() {
        for(int x=-1; x<=1; x++)
            for(int z=-1; z<=1; z++) addStatePos(new BlockPos(x,0,z),this.groundState);
    }

    @Override protected void updateTileExtra(TileEntity tile) {}

    @Override protected void setUpState(IBlockState state, BlockPos pos) {}
}