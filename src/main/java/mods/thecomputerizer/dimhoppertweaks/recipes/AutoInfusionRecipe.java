package mods.thecomputerizer.dimhoppertweaks.recipes;

import mcjty.rftools.craftinggrid.CraftingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.Objects;

import static net.minecraft.item.ItemStack.EMPTY;

public class AutoInfusionRecipe extends CraftingRecipe {
    
    private ItemStack catalyst;
    private IRecipe recipe;
    private boolean recipePresent;
    
    public AutoInfusionRecipe() {
        this.catalyst = EMPTY;
        this.recipe = null;
        this.recipePresent = false;
    }
    
    @Override
    public IRecipe getCachedRecipe(World world) {
        if(!this.recipePresent) {
            this.recipePresent = true;
            this.recipe = findRecipe(world,getInventory());
        }
        return this.recipe;
    }
    
    @SuppressWarnings("ConstantValue") @Override
    public void readFromNBT(NBTTagCompound tag) {
        NBTTagList grid = tag.getTagList("Items",10);
        for(int i=1;i<grid.tagCount(); ++i) {
            NBTTagCompound stack = grid.getCompoundTagAt(i);
            getInventory().setInventorySlotContents(i,new ItemStack(stack));
        }
        NBTTagCompound catalyst = tag.getCompoundTag("Catalyst");
        if(Objects.nonNull(catalyst)) this.catalyst = new ItemStack(catalyst);
        NBTTagCompound result = tag.getCompoundTag("Result");
        if(Objects.nonNull(result)) setResult(new ItemStack(result));
        else setResult(EMPTY);
        setKeepOne(tag.getBoolean("Keep"));
        setCraftMode(CraftingRecipe.CraftMode.values()[tag.getByte("Int")]);
        this.recipePresent = false;
    }
    
    @Override
    public void setRecipe(ItemStack[] items, ItemStack result) {
        super.setRecipe(items,result);
        this.recipePresent = false;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag) {
        NBTTagList grid = new NBTTagList();
        for(int i=1;i<10;i++) {
            ItemStack stack = getInventory().getStackInSlot(i);
            NBTTagCompound stackTag = new NBTTagCompound();
            if(!stack.isEmpty()) stack.writeToNBT(stackTag);
            grid.appendTag(stackTag);
        }
        NBTTagCompound catalyst = new NBTTagCompound();
        if(!this.catalyst.isEmpty()) getResult().writeToNBT(catalyst);
        NBTTagCompound result = new NBTTagCompound();
        if(!getResult().isEmpty()) getResult().writeToNBT(result);
        tag.setTag("Result",result);
        tag.setTag("Catalyst",catalyst);
        tag.setTag("Items",grid);
        tag.setBoolean("Keep",isKeepOne());
        tag.setByte("Int",(byte)getCraftMode().ordinal());
    }
}