package mods.thecomputerizer.dimhoppertweaks.mixin.mods.enderio;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import crazypants.enderio.invpanel.invpanel.TileInventoryPanel;
import crazypants.enderio.invpanel.util.StoredCraftingRecipe;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = StoredCraftingRecipe.class, remap = false)
public abstract class MixinStoredCraftingRecipe {

    @WrapOperation(at=@At(value="NEW", target="(Lnet/minecraft/inventory/Container;II)" +
            "Lnet/minecraft/inventory/InventoryCrafting;",remap=true),method="findCraftingResult")
    private InventoryCrafting dimhoppertweaks$inheritStages(Container container, int width, int height,
            Operation<InventoryCrafting> operation, @Local(argsOnly=true)TileInventoryPanel tile) {
        InventoryCrafting inventory = operation.call(container,width,height);
        DelayedModAccess.inheritInventoryStages(tile,inventory);
        return inventory;
    }
}