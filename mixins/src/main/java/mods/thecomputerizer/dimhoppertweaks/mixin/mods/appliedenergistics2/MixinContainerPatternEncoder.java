package mods.thecomputerizer.dimhoppertweaks.mixin.mods.appliedenergistics2;

import appeng.api.implementations.guiobjects.IGuiItemObject;
import appeng.api.storage.ITerminalHost;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.container.implementations.ContainerPatternEncoder;
import appeng.container.slot.IOptionalSlotHost;
import appeng.helpers.IContainerCraftingPacket;
import appeng.util.inv.IAEAppEngInventory;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ContainerPatternEncoder.class, remap = false)
public abstract class MixinContainerPatternEncoder extends ContainerMEMonitorable implements IAEAppEngInventory,
        IOptionalSlotHost, IContainerCraftingPacket {
    
    protected MixinContainerPatternEncoder(InventoryPlayer ip, ITerminalHost monitorable,
            IGuiItemObject object, boolean bindInventory) {
        super(ip,monitorable,object,bindInventory);
    }
    
    @WrapOperation(at=@At(value="NEW",target="(Lnet/minecraft/inventory/Container;II)"+
            "Lnet/minecraft/inventory/InventoryCrafting;",remap=true),method="getAndUpdateOutput")
    private InventoryCrafting dimhoppertweaks$inheritStages1(Container container, int width, int height,
            Operation<InventoryCrafting> operation) {
        InventoryCrafting inventory = operation.call(container,width,height);
        DelayedModAccess.inheritInventoryStages(getInventoryPlayer().player,inventory);
        return inventory;
    }

    @WrapOperation(at=@At(value="NEW",target="(Lnet/minecraft/inventory/Container;II)"+
            "Lnet/minecraft/inventory/InventoryCrafting;",remap=true),method="craftOrGetItem")
    private InventoryCrafting dimhoppertweaks$inheritStages2(Container container, int width, int height,
            Operation<InventoryCrafting> operation) {
        InventoryCrafting inventory = operation.call(container,width,height);
        DelayedModAccess.inheritInventoryStages(getInventoryPlayer().player,inventory);
        return inventory;
    }
}
