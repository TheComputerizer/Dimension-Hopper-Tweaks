package mods.thecomputerizer.dimhoppertweaks.mixin.mods.extendedcrafting;

import com.blakebr0.extendedcrafting.crafting.table.TableCrafting;
import com.blakebr0.extendedcrafting.lib.IExtendedTable;
import com.blakebr0.extendedcrafting.tile.TileAutomationInterface;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.InventoryCraftingAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.TileEntityAccess;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("DataFlowIssue")
@Mixin(value = TileAutomationInterface.class, remap = false)
public class MixinTileAutomationInterface {

    @Unique private TileEntity dimhoppertweaks$cast() {
        return (TileEntity)(Object)this;
    }

    @Redirect(at = @At(value = "NEW", target = "(Lnet/minecraft/inventory/Container;"+
            "Lcom/blakebr0/extendedcrafting/lib/IExtendedTable;)"+
            "Lcom/blakebr0/extendedcrafting/crafting/table/TableCrafting;"), method = "saveRecipe")
    private TableCrafting dimhoppertweaks$inheritTileStages(Container container, IExtendedTable tile) {
        TableCrafting table = new TableCrafting(container,tile);
        ((InventoryCraftingAccess)table).dimhoppertweaks$setStages(((TileEntityAccess)dimhoppertweaks$cast()).dimhoppertweaks$getStages());
        return table;
    }
}
