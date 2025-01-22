package mods.thecomputerizer.dimhoppertweaks.mixin.mods.extendedcrafting;

import com.blakebr0.extendedcrafting.crafting.table.TableCrafting;
import com.blakebr0.extendedcrafting.lib.IExtendedTable;
import com.blakebr0.extendedcrafting.tile.TileAutomationInterface;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IInventoryCrafting;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.ITileEntity;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings({"DataFlowIssue","SpellCheckingInspection"})
@Mixin(value = TileAutomationInterface.class, remap = false)
public abstract class MixinTileAutomationInterface {

    @Unique
    private TileEntity dimhoppertweaks$cast() {
        return (TileEntity)(Object)this;
    }

    @Redirect(at = @At(value = "NEW", target = "(Lnet/minecraft/inventory/Container;"+
            "Lcom/blakebr0/extendedcrafting/lib/IExtendedTable;)"+
            "Lcom/blakebr0/extendedcrafting/crafting/table/TableCrafting;"), method = "saveRecipe")
    private TableCrafting dimhoppertweaks$inheritTileStages(Container container, IExtendedTable tile) {
        TableCrafting table = new TableCrafting(container,tile);
        ((IInventoryCrafting)table).dimhoppertweaks$setStages(((ITileEntity)dimhoppertweaks$cast()).dimhoppertweaks$getStages());
        return table;
    }
}