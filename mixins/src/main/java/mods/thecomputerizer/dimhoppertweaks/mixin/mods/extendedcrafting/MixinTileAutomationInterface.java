package mods.thecomputerizer.dimhoppertweaks.mixin.mods.extendedcrafting;

import com.blakebr0.extendedcrafting.crafting.table.TableCrafting;
import com.blakebr0.extendedcrafting.lib.IExtendedTable;
import com.blakebr0.extendedcrafting.tile.TileAutomationInterface;
import com.blakebr0.extendedcrafting.tile.TileInventoryBase;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = TileAutomationInterface.class, remap = false)
public abstract class MixinTileAutomationInterface extends TileInventoryBase {

    public MixinTileAutomationInterface() {
        super("interface");
    }

    @WrapOperation(at=@At(value="NEW",target="(Lnet/minecraft/inventory/Container;"+
            "Lcom/blakebr0/extendedcrafting/lib/IExtendedTable;)"+
            "Lcom/blakebr0/extendedcrafting/crafting/table/TableCrafting;",remap=true),method="saveRecipe")
    private TableCrafting dimhoppertweaks$inheritTileStages(Container container, IExtendedTable tile,
            Operation<TableCrafting> operation) {
        return DelayedModAccess.inheritInventoryStagesAndReturn(this,operation.call(container,tile));
    }
}