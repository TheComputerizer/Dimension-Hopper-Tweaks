package mods.thecomputerizer.dimhoppertweaks.common.containers;

import mcjty.lib.container.BaseSlot;
import mcjty.lib.container.ContainerFactory;
import mcjty.lib.container.GenericContainer;
import mcjty.lib.container.SlotFactory;
import mcjty.lib.container.SlotType;
import mcjty.rftools.blocks.crafter.CrafterBaseTE;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.registry.tiles.AutoInfusionTableEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

import static mcjty.lib.container.SlotType.SLOT_INPUT;

@ParametersAreNonnullByDefault
public class AutoInfusionContainer extends GenericContainer {
    
    public static final ContainerFactory FACTORY = new ContainerFactory(DHTRef.res("gui/auto_infusion.gui"));
    
    private final AutoInfusionTableEntity crafterEntity;
    
    public CrafterBaseTE getCrafterTE() {
        return this.crafterEntity;
    }
    
    public AutoInfusionContainer(EntityPlayer player, IInventory inventory) {
        super(FACTORY);
        this.crafterEntity = (AutoInfusionTableEntity)inventory;
        addInventory("container",inventory);
        addInventory("player",player.inventory);
        generateSlots();
    }
    
    @Override
    protected Slot createSlot(SlotFactory slotFactory, IInventory inventory, int index, int x, int y, SlotType type) {
        if(index>=11 && index<37 && type==SLOT_INPUT) {
            return new BaseSlot(inventory,index,x,y) {
                public boolean isItemValid(ItemStack stack) {
                    return AutoInfusionContainer.this.crafterEntity.isItemValidForSlot(getSlotIndex(),stack) && super.isItemValid(stack);
                }
                public void onSlotChanged() {
                    AutoInfusionContainer.this.crafterEntity.noRecipesWork = false;
                    super.onSlotChanged();
                }
            };
        } else {
            return index>=37 && index<41 ? new BaseSlot(inventory,index,x,y) {
                public boolean isItemValid(ItemStack stack) {
                    return AutoInfusionContainer.this.crafterEntity.isItemValidForSlot(getSlotIndex(),stack) && super.isItemValid(stack);
                }
                public void onSlotChanged() {
                    AutoInfusionContainer.this.crafterEntity.noRecipesWork = false;
                    super.onSlotChanged();
                }
            } : super.createSlot(slotFactory,inventory,index,x,y,type);
        }
    }
}