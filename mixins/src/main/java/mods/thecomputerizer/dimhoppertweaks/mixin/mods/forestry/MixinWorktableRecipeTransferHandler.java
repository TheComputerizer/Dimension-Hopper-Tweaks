package mods.thecomputerizer.dimhoppertweaks.mixin.mods.forestry;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import forestry.worktable.gui.ContainerWorktable;
import forestry.worktable.inventory.InventoryCraftingForestry;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "forestry.worktable.compat.WorktableRecipeTransferHandler", remap = false)
public abstract class MixinWorktableRecipeTransferHandler {
    
    @WrapOperation(at=@At(value="NEW",target="(Lforestry/worktable/gui/ContainerWorktable;)"+
            "Lforestry/worktable/inventory/InventoryCraftingForestry;"),method="transferRecipe("+
            "Lforestry/worktable/gui/ContainerWorktable;Lmezz/jei/api/gui/IRecipeLayout;"+
            "Lnet/minecraft/entity/player/EntityPlayer;ZZ)Lmezz/jei/api/recipe/transfer/IRecipeTransferError;")
    private InventoryCraftingForestry dimhoppertweaks$inheritStages(ContainerWorktable container,
            Operation<InventoryCraftingForestry> operation, @Local(argsOnly=true)EntityPlayer player) {
        InventoryCraftingForestry inventory = operation.call(container);
        DelayedModAccess.inheritInventoryStages(player,inventory);
        return inventory;
    }
}