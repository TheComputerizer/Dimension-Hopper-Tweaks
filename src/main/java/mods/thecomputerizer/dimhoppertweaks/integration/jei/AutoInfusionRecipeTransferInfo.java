package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mods.thecomputerizer.dimhoppertweaks.common.containers.AutoInfusionContainer;
import net.minecraft.inventory.Slot;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class AutoInfusionRecipeTransferInfo implements IRecipeTransferInfo<AutoInfusionContainer> {
    
    @Override public Class<AutoInfusionContainer> getContainerClass() {
        return AutoInfusionContainer.class;
    }
    
    @Override public String getRecipeCategoryUid() {
        return "aoa3.infusion";
    }
    
    @Override public boolean canHandle(AutoInfusionContainer container) {
        return true;
    }
    
    @Override public List<Slot> getRecipeSlots(AutoInfusionContainer container) {
        List<Slot> slots = new ArrayList<>(10);
        for(int i=1;i<=11;i++) slots.add(container.getSlot(i));
        return slots;
    }
    
    @Override public List<Slot> getInventorySlots(AutoInfusionContainer container) {
        List<Slot> slots = new ArrayList<>(container.inventorySlots.size()-11);
        for(int i=12;i<container.inventorySlots.size();i++) slots.add(container.getSlot(i));
        return slots;
    }
}
