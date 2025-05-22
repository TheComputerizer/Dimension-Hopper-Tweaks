package mods.thecomputerizer.dimhoppertweaks.mixin.mods.rftools;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mcjty.rftools.craftinggrid.StorageCraftingTools;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.minecraft.item.ItemStack.EMPTY;

@Mixin(value = StorageCraftingTools.class, remap = false)
public abstract class MixinStorageCraftingTools {

    @WrapOperation(at=@At(value="NEW", target="(Lnet/minecraft/inventory/Container;II)"+
            "Lnet/minecraft/inventory/InventoryCrafting;",remap=true),method="tryRecipe")
    private static InventoryCrafting dimhoppertweaks$inheritStages1(Container container, int width, int height,
            Operation<InventoryCrafting> operation, @Local(argsOnly=true)EntityPlayer player) {
        InventoryCrafting inventory = operation.call(container,width,height);
        DelayedModAccess.inheritInventoryStages(player,inventory);
        return inventory;
    }

    @WrapOperation(at=@At(value="NEW", target="(Lnet/minecraft/inventory/Container;II)"+
            "Lnet/minecraft/inventory/InventoryCrafting;",remap=true),method="testAndConsumeCraftingItems")
    private static InventoryCrafting dimhoppertweaks$inheritStages2(Container container, int width, int height,
            Operation<InventoryCrafting> operation, @Local(argsOnly=true)EntityPlayer player) {
        InventoryCrafting inventory = operation.call(container,width,height);
        DelayedModAccess.inheritInventoryStages(player,inventory);
        return inventory;
    }

    @WrapOperation(at=@At(value="INVOKE",target="Lnet/minecraft/item/crafting/IRecipe;getRecipeOutput()"+
            "Lnet/minecraft/item/ItemStack;",remap=true),method="craftItems")
    private static ItemStack dimhoppertweaks$fixOutput(IRecipe recipe, Operation<ItemStack> operation,
            @Local(argsOnly=true)EntityPlayer player) {
        ItemStack stack = operation.call(recipe);
        return DelayedModAccess.hasStageForItem(player,stack) ? stack : EMPTY;
    }
}