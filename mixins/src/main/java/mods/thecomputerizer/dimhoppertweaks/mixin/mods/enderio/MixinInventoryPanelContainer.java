package mods.thecomputerizer.dimhoppertweaks.mixin.mods.enderio;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import crazypants.enderio.base.invpanel.database.IChangeLog;
import crazypants.enderio.base.machine.gui.AbstractMachineContainer;
import crazypants.enderio.invpanel.invpanel.InventoryPanelContainer;
import crazypants.enderio.invpanel.invpanel.TileInventoryPanel;
import crazypants.enderio.invpanel.util.SlotCraftingWrapper;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import java.util.Collection;

@Mixin(value = InventoryPanelContainer.class, remap = false)
public abstract class MixinInventoryPanelContainer extends AbstractMachineContainer<TileInventoryPanel>
        implements IChangeLog {

    public MixinInventoryPanelContainer(@Nonnull InventoryPlayer playerInv, @Nonnull TileInventoryPanel tile) {
        super(playerInv,tile);
    }
    
    @Inject(at=@At("TAIL"),method="<init>")
    private void dimhoppertweaks$inheritStages(InventoryPlayer inv, TileInventoryPanel tile, CallbackInfo ci) {
        Collection<String> stages = DelayedModAccess.getPlayerStages(inv.player);
        DelayedModAccess.setTileStages(tile,stages);
        DelayedModAccess.setContainerStages(this,stages);
    }

    @WrapOperation(at=@At(value="NEW", target="(Lnet/minecraft/entity/player/EntityPlayer;" +
            "Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/inventory/IInventory;III)" +
            "Lcrazypants/enderio/invpanel/util/SlotCraftingWrapper;",remap=true),method="addMachineSlots")
    private SlotCraftingWrapper dimhoppertweaks$inheritStages(EntityPlayer player, InventoryCrafting inventory,
            IInventory backing, int index, int x, int y, Operation<SlotCraftingWrapper> operation) {
        DelayedModAccess.inheritInventoryStages(player,inventory);
        return operation.call(player,inventory,backing,index,x,y);
    }

    @WrapOperation(at=@At(value="NEW", target="(Lnet/minecraft/inventory/Container;II)"+
            "Lnet/minecraft/inventory/InventoryCrafting;"),method="onCraftMatrixChanged",remap=true)
    private InventoryCrafting dimhoppertweaks$inheritStages(Container container, int width, int height,
            Operation<InventoryCrafting> operation) {
        return DelayedModAccess.inheritInventoryStagesAndReturn(this,operation.call(container,width,height));
    }
}
