package mods.thecomputerizer.dimhoppertweaks.mixin.mods.enderio;

import com.llamalad7.mixinextras.sugar.Local;
import crazypants.enderio.base.invpanel.database.IChangeLog;
import crazypants.enderio.base.machine.gui.AbstractMachineContainer;
import crazypants.enderio.invpanel.invpanel.InventoryCraftingWrapper;
import crazypants.enderio.invpanel.invpanel.InventoryPanelContainer;
import crazypants.enderio.invpanel.invpanel.TileInventoryPanel;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nonnull;

@Mixin(value = InventoryPanelContainer.class, remap = false)
public abstract class MixinInventoryPanelContainer extends AbstractMachineContainer<TileInventoryPanel>
        implements IChangeLog {
    
    public MixinInventoryPanelContainer(@Nonnull InventoryPlayer playerInv, @Nonnull TileInventoryPanel tile) {
        super(playerInv,tile);
    }
    
    @Redirect(at=@At(value="NEW",target="(Lnet/minecraft/inventory/IInventory;Lnet/minecraft/inventory/Container;II)"+
            "Lcrazypants/enderio/invpanel/invpanel/InventoryCraftingWrapper;",remap=true),method="addMachineSlots")
    private InventoryCraftingWrapper dimhoppertweaks$inheritStages(IInventory backing, Container eventHandler,
            int width, int height, @Local(argsOnly=true) InventoryPlayer player) {
        InventoryCraftingWrapper wrapper = new InventoryCraftingWrapper(backing,eventHandler,width,height);
        DelayedModAccess.inheritInventoryStages(player.player,wrapper);
        return wrapper;
    }
}
