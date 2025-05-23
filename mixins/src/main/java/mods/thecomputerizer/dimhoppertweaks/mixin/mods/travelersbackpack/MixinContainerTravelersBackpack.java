package mods.thecomputerizer.dimhoppertweaks.mixin.mods.travelersbackpack;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.tiviacz.travelersbackpack.gui.container.ContainerTravelersBackpack;
import com.tiviacz.travelersbackpack.gui.inventory.IInventoryTravelersBackpack;
import com.tiviacz.travelersbackpack.gui.inventory.InventoryCraftingImproved;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ContainerTravelersBackpack.class, remap = false)
public abstract class MixinContainerTravelersBackpack extends Container {

    @WrapOperation(at=@At(value="NEW",target=
            "(Lcom/tiviacz/travelersbackpack/gui/inventory/IInventoryTravelersBackpack;"+
                    "Lnet/minecraft/inventory/Container;II)"+
                    "Lcom/tiviacz/travelersbackpack/gui/inventory/InventoryCraftingImproved;",remap=true),
            method="<init>")
    private InventoryCraftingImproved dimhoppertweaks$inheritStages(IInventoryTravelersBackpack inventory,
            Container container, int width, int height, Operation<InventoryCraftingImproved> operation,
            @Local(argsOnly=true)InventoryPlayer player) {
        return DelayedModAccess.inheritInventoryStagesAndReturn(player,operation.call(inventory,container,width,height));
    }
}