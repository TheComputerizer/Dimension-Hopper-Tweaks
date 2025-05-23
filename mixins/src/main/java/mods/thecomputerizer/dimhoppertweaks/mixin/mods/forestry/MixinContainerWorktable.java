package mods.thecomputerizer.dimhoppertweaks.mixin.mods.forestry;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import forestry.core.gui.ContainerTile;
import forestry.core.gui.IContainerCrafting;
import forestry.core.gui.IGuiSelectable;
import forestry.core.gui.slots.SlotCrafter;
import forestry.worktable.gui.ContainerWorktable;
import forestry.worktable.inventory.InventoryCraftingForestry;
import forestry.worktable.tiles.ICrafterWorktable;
import forestry.worktable.tiles.TileWorktable;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;

@Mixin(value = ContainerWorktable.class, remap = false)
public abstract class MixinContainerWorktable extends ContainerTile<TileWorktable> implements IContainerCrafting, IGuiSelectable {
    
    @Shadow @Final private InventoryCraftingForestry craftMatrix;

    @SuppressWarnings("unused")
    public MixinContainerWorktable(EntityPlayer player, TileWorktable tile) {
        super(tile);
    }

    @WrapOperation(at=@At(value="NEW", target="(Lnet/minecraft/entity/player/EntityPlayer;" +
            "Lforestry/worktable/inventory/InventoryCraftingForestry;Lnet/minecraft/inventory/IInventory;" +
            "Lforestry/worktable/tiles/ICrafterWorktable;III)Lforestry/core/gui/slots/SlotCrafter;"),method="<init>")
    private SlotCrafter dimhoppertweaks$inheritStages(EntityPlayer player, InventoryCraftingForestry inventory,
            IInventory display, ICrafterWorktable crafter, int index, int x, int y,
            Operation<SlotCrafter> operation) {
        Collection<String> stages = DelayedModAccess.getPlayerStages(player);
        DelayedModAccess.setTileStages((TileWorktable)crafter,stages);
        DelayedModAccess.setInventoryStages(inventory,stages);
        return operation.call(player,inventory,display,crafter,index,x,y);
    }
}