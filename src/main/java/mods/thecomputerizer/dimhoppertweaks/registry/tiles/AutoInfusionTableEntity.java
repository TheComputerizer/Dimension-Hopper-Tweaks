package mods.thecomputerizer.dimhoppertweaks.registry.tiles;

import lombok.Getter;
import lombok.Setter;
import mcjty.lib.container.DefaultSidedInventory;
import mcjty.lib.container.InventoryHelper;
import mcjty.lib.tileentity.GenericEnergyReceiverTileEntity;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.ItemStackList;
import mcjty.lib.varia.NullSidedInvWrapper;
import mcjty.lib.varia.RedstoneMode;
import mcjty.rftools.compat.jei.JEIRecipeAcceptor;
import mcjty.rftools.craftinggrid.CraftingRecipe;
import mcjty.rftools.craftinggrid.CraftingRecipe.CraftMode;
import mcjty.rftools.items.storage.StorageFilterCache;
import mcjty.rftools.items.storage.StorageFilterItem;
import mods.thecomputerizer.dimhoppertweaks.common.containers.InventoryAutoInfusion;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.recipes.AutoInfusionRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.oredict.OreDictionary;
import net.tslat.aoa3.common.containers.ContainerInfusionTable.InventoryInfusion;
import net.tslat.aoa3.utils.player.PlayerDataManager;
import net.tslat.aoa3.utils.player.PlayerUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import static mcjty.lib.gui.widgets.ImageChoiceLabel.PARAM_CHOICE_IDX;
import static mcjty.rftools.blocks.crafter.CrafterConfiguration.MAXENERGY;
import static mcjty.rftools.blocks.crafter.CrafterConfiguration.RECEIVEPERTICK;
import static mcjty.rftools.blocks.crafter.CrafterConfiguration.rfPerOperation;
import static mcjty.rftools.blocks.crafter.CrafterConfiguration.speedOperations;
import static mcjty.rftools.blocks.crafter.CrafterContainer.BUFFEROUT_SIZE;
import static mcjty.rftools.blocks.crafter.CrafterContainer.BUFFER_SIZE;
import static mcjty.rftools.craftinggrid.CraftingRecipe.CraftMode.EXTC;
import static mcjty.rftools.craftinggrid.CraftingRecipe.CraftMode.INT;
import static mods.thecomputerizer.dimhoppertweaks.common.containers.AutoInfusionContainer.FACTORY;
import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;
import static net.minecraft.item.ItemStack.EMPTY;
import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
import static net.tslat.aoa3.library.Enums.Skills.INFUSION;

@SuppressWarnings("NullableProblems")
public class AutoInfusionTableEntity extends GenericEnergyReceiverTileEntity
        implements ITickable, DefaultSidedInventory, JEIRecipeAcceptor {
    
    private final InventoryHelper helper;
    private final InventoryInfusion infusionInventory;
    private final AutoInfusionRecipe[] recipes;
    private StorageFilterCache filterCache;
    @Getter private ItemStackList ghostSlots;
    public boolean noRecipesWork;
    @Getter @Setter private int speedMode;
    @Getter private int infusionCap;
    
    public AutoInfusionTableEntity() {
        super(MAXENERGY.get(),RECEIVEPERTICK.get());
        this.helper = new InventoryHelper(this,FACTORY,42);
        this.ghostSlots = ItemStackList.create(BUFFER_SIZE+BUFFEROUT_SIZE);
        this.filterCache = null;
        this.noRecipesWork = false;
        this.speedMode = 0;
        this.infusionInventory = new InventoryAutoInfusion(new Container() {
            public boolean canInteractWith(@Nonnull EntityPlayer player) {
                return false;
            }
        });
        this.recipes = new AutoInfusionRecipe[4];
        for(int i=0;i<this.recipes.length;i++) this.recipes[i] = new AutoInfusionRecipe();
    }
    
    @Override public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return FACTORY.isOutputSlot(index);
    }
    
    @Override public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
        if(!isItemValidForSlot(index, stack)) return false;
        return FACTORY.isInputSlot(index);
    }
    
    protected void checkStateServer() {
        if(isMachineEnabled() && !this.noRecipesWork) {
            int rf = (int)((float)rfPerOperation.get()*(2f-this.getInfusedFactor())/2f);
            int steps = this.speedMode==1 ? speedOperations.get() : 1;
            if(rf>0) steps = (int)Math.min(steps,this.getStoredPower()/(long)rf);
            if(steps>0) DelayedModAccess.inheritInventoryStages(this,this.infusionInventory);
            int i;
            for(i=0;i<steps;i++) {
                if(!craftOneCycle()) {
                    this.noRecipesWork = true;
                    break;
                }
            }
            rf*=i;
            if(rf>0) this.consumeEnergy(rf);
        }
    }
    
    private boolean craftOneCycle() {
        boolean craftedAtLeastOneThing = false;
        for(CraftingRecipe craftingRecipe : this.recipes)
            if(craftOneItemNew(craftingRecipe)) craftedAtLeastOneThing = true;
        return craftedAtLeastOneThing;
    }
    
    @SuppressWarnings("ConstantValue")
    private boolean craftOneItemNew(CraftingRecipe craftingRecipe) {
        IRecipe recipe = craftingRecipe.getCachedRecipe(this.getWorld());
        if(Objects.isNull(recipe)) return false;
        else {
            Map<Integer,ItemStack> undo = new HashMap<>();
            if(!testAndConsumeCraftingItems(craftingRecipe,undo,true)) {
                undo(undo);
                if(!testAndConsumeCraftingItems(craftingRecipe,undo,false)) {
                    undo(undo);
                    return false;
                }
            }
            ItemStack result = EMPTY;
            try {
                result = recipe.getCraftingResult(this.infusionInventory);
            } catch(RuntimeException ex) {
                LOGGER.error("Problem with recipe!",ex);
            }
            CraftMode mode = craftingRecipe.getCraftMode();
            if(!result.isEmpty() && placeResult(mode,result,undo)) {
                List<ItemStack> remaining = recipe.getRemainingItems(this.infusionInventory);
                if(Objects.nonNull(remaining)) {
                    CraftMode remainingMode = mode==EXTC ? INT : mode;
                    for(ItemStack s : remaining) {
                        if(!s.isEmpty() && !placeResult(remainingMode,s,undo)) {
                            undo(undo);
                            return false;
                        }
                    }
                }
                return true;
            } else {
                undo(undo);
                return false;
            }
        }
    }
    
    @Override public @Nonnull ItemStack decrStackSize(int index, int count) {
        this.noRecipesWork = false;
        if(index==41) this.filterCache = null;
        return this.helper.decrStackSize(index,count);
    }
    
    @Override public boolean execute(EntityPlayerMP player, String command, TypedMap params) {
        boolean rc = super.execute(player,command,params);
        if(rc) return true;
        switch(command) {
            case "crafter.setRsMode":
                setRSMode(RedstoneMode.values()[params.get(PARAM_CHOICE_IDX)]);
                return true;
            case "crafter.setMode":
                setSpeedMode(params.get(PARAM_CHOICE_IDX));
                return true;
            case "crafter.remember":
                rememberItems();
                return true;
            case "crafter.forget":
                forgetItems();
                return true;
            default: return false;
        }
    }
    
    private void forgetItems() {
        Collections.fill(this.ghostSlots,EMPTY);
        this.noRecipesWork = false;
        markDirtyClient();
    }
    
    private void getFilterCache() {
        if(Objects.isNull(this.filterCache))
            this.filterCache = StorageFilterItem.getCache(this.helper.getStackInSlot(41));
    }
    
    @Override public InventoryHelper getInventoryHelper() {
        return this.helper;
    }
    
    public CraftingRecipe getRecipe(int index) {
        return this.recipes[index];
    }
    
    @Override public @Nonnull int[] getSlotsForFace(EnumFacing side) {
        return FACTORY.getAccessibleSlots();
    }
    
    public int getSupportedRecipes() {
        return this.recipes.length;
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability==ITEM_HANDLER_CAPABILITY || super.hasCapability(capability,facing);
    }
    
    @SuppressWarnings("unchecked")
    @Override public <T> @Nullable T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==ITEM_HANDLER_CAPABILITY) {
            if(Objects.isNull(this.invHandlerNull)) this.invHandlerNull = new NullSidedInvWrapper(this);
            return (T)this.invHandlerNull;
        }
        return super.getCapability(capability,facing);
    }
    
    @Override public boolean isEmpty() {
        return false;
    }
    
    @Override public boolean isItemValidForSlot(int index, ItemStack stack) {
        if(index>=0 && index<=10) return false;
        else {
            if(index>=11 && index<37) {
                ItemStack ghostSlot = this.ghostSlots.get(index-11);
                if(!ghostSlot.isEmpty() && !ghostSlot.isItemEqual(stack)) return false;
                if(this.helper.containsItem(41)) {
                    this.getFilterCache();
                    if(Objects.nonNull(this.filterCache)) return this.filterCache.match(stack);
                }
            } else if(index>=37 && index<41) {
                ItemStack ghostSlot = this.ghostSlots.get(index-37+26);
                return ghostSlot.isEmpty() || ghostSlot.isItemEqual(stack);
            }
            return true;
        }
    }
    
    private static boolean match(ItemStack target, ItemStack input, boolean strict) {
        if(strict) return OreDictionary.itemMatches(target,input,false);
        if((!input.isEmpty() || target.isEmpty()) && (input.isEmpty() || !target.isEmpty()))
            return target.getItem()==input.getItem();
        return false;
    }
    
    @Override protected boolean needsCustomInvWrapper() {
        return true;
    }
    
    @Override protected boolean needsRedstoneMode() {
        return true;
    }
    
    private boolean placeResult(CraftMode mode, ItemStack result, Map<Integer,ItemStack> undo) {
        int start;
        int stop;
        if(mode==INT) {
            start = 11;
            stop = 37;
        } else {
            start = 37;
            stop = 41;
        }
        return InventoryHelper.mergeItemStack(this,true,result,start,stop,undo)==0;
    }
    
    private void readGhostBufferFromNBT(NBTTagCompound tag) {
        NBTTagList list = tag.getTagList("GItems",10);
        for(int i=0;i<list.tagCount();i++) {
            NBTTagCompound ghostTag = list.getCompoundTagAt(i);
            this.ghostSlots.set(i,new ItemStack(ghostTag));
        }
    }
    
    private void readRecipesFromNBT(NBTTagCompound tag) {
        NBTTagList recipes = tag.getTagList("Recipes",10);
        for(int i=0;i<recipes.tagCount();i++) {
            NBTTagCompound recipeTag = recipes.getCompoundTagAt(i);
            this.recipes[i].readFromNBT(recipeTag);
        }
    }
    
    @Override public void readRestorableFromNBT(NBTTagCompound tag) {
        super.readRestorableFromNBT(tag);
        readBufferFromNBT(tag,this.helper);
        readGhostBufferFromNBT(tag);
        readRecipesFromNBT(tag);
        setSpeedMode(tag.getByte("speedMode"));
    }
    
    private void rememberItems() {
        for(int i=0;i<this.ghostSlots.size();i++) {
            int slotId;
            if(i<26) slotId = i+11;
            else slotId = i+37-26;
            if(this.helper.containsItem(slotId)) {
                ItemStack stack = this.helper.getStackInSlot(slotId).copy();
                stack.setCount(1);
                this.ghostSlots.set(i,stack);
            }
        }
        this.noRecipesWork = false;
        this.markDirtyClient();
    }
    
    @Override public @Nonnull ItemStack removeStackFromSlot(int index) {
        this.noRecipesWork = false;
        if(index==41) this.filterCache = null;
        return this.helper.removeStackFromSlot(index);
    }
    
    public void selectRecipe(int index) {
        CraftingRecipe recipe = this.recipes[index];
        setInventorySlotContents(10,recipe.getResult());
        InventoryCrafting inv = recipe.getInventory();
        int size = inv.getSizeInventory();
        for(int i=0;i<size;i++) setInventorySlotContents(i,inv.getStackInSlot(i));
    }
    
    @Override public void setInventorySlotContents(int index, ItemStack stack) {
        this.noRecipesWork = false;
        if(index==41) this.filterCache = null;
        this.helper.setInventorySlotContents(getInventoryStackLimit(),index,stack);
    }
    
    @Override public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return canPlayerAccess(player);
    }
    
    @Override public void setGridContents(List<ItemStack> stacks) {
        setInventorySlotContents(10,stacks.get(0));
        for(int i=1;i<stacks.size();i++)
            setInventorySlotContents(i-1,stacks.get(i));
    }
    
    @Override public boolean setOwner(EntityPlayer player) {
        PlayerDataManager manager = PlayerUtil.getAdventPlayer(player);
        this.infusionCap = player.capabilities.isCreativeMode ? 999 : manager.stats().getLevel(INFUSION);
        return super.setOwner(player);
    }
    
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean testAndConsumeCraftingItems(CraftingRecipe craftingRecipe, Map<Integer,ItemStack> undo, boolean strictDamage) {
        int keep = craftingRecipe.isKeepOne() ? 1 : 0;
        InventoryCrafting inventory = craftingRecipe.getInventory();
        for(int i=0;i<inventory.getSizeInventory();i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if(stack.isEmpty()) this.infusionInventory.setInventorySlotContents(i,EMPTY);
            else {
                int count = stack.getCount();
                for(int j=0;j<26;j++) {
                    int slotId = 11+j;
                    ItemStack input = this.helper.getStackInSlot(slotId);
                    if(!input.isEmpty() && input.getCount()>keep && match(stack,input,strictDamage)) {
                        this.infusionInventory.setInventorySlotContents(i,input.copy());
                        int ss = count;
                        if(input.getCount()-count<keep) ss = input.getCount()-keep;
                        count-=ss;
                        if(!undo.containsKey(slotId)) undo.put(slotId,input.copy());
                        input.splitStack(ss);
                        if(input.isEmpty()) this.helper.setStackInSlot(slotId,EMPTY);
                    }
                    if(count==0) break;
                }
                if(count>0) return false;
            }
        }
        IRecipe recipe = craftingRecipe.getCachedRecipe(this.getWorld());
        return recipe.matches(this.infusionInventory,this.getWorld());
    }
    
    private void undo(Map<Integer, ItemStack> undo) {
        for(Entry<Integer,ItemStack> entry : undo.entrySet())
            this.helper.setStackInSlot(entry.getKey(),entry.getValue());
        undo.clear();
    }
    
    @Override public void update() {
        if(!getWorld().isRemote) checkStateServer();
    }
    
    private void writeGhostBufferToNBT(NBTTagCompound tag) {
        NBTTagList list = new NBTTagList();
        for(ItemStack stack : this.ghostSlots) {
            NBTTagCompound stackTag = new NBTTagCompound();
            if(!stack.isEmpty()) stack.writeToNBT(stackTag);
            list.appendTag(stackTag);
        }
        tag.setTag("GItems",list);
    }
    
    private void writeRecipesToNBT(NBTTagCompound tag) {
        NBTTagList recipes = new NBTTagList();
        for(CraftingRecipe recipe : this.recipes) {
            NBTTagCompound recipeTag = new NBTTagCompound();
            recipe.writeToNBT(recipeTag);
            recipes.appendTag(recipeTag);
        }
        tag.setTag("Recipes", recipes);
    }
    
    @Override public void writeRestorableToNBT(NBTTagCompound tagCompound) {
        super.writeRestorableToNBT(tagCompound);
        writeBufferToNBT(tagCompound,this.helper);
        writeGhostBufferToNBT(tagCompound);
        writeRecipesToNBT(tagCompound);
        tagCompound.setByte("speedMode",(byte)this.speedMode);
    }
}