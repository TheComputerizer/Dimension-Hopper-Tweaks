package mods.thecomputerizer.dimhoppertweaks.mixin.mods.forestry;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import forestry.core.tiles.TileBase;
import forestry.worktable.inventory.InventoryCraftingForestry;
import forestry.worktable.tiles.TileWorktable;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = TileWorktable.class, remap = false)
public abstract class MixinTileWorktable extends TileBase {

    @WrapOperation(at=@At(value="INVOKE",target="Lforestry/core/recipes/RecipeUtil;"+
            "getCraftRecipe(Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/util/NonNullList;"+
            "Lnet/minecraft/world/World;Lnet/minecraft/item/crafting/IRecipe;)"+
            "Lforestry/worktable/inventory/InventoryCraftingForestry;"),method="craftRecipe")
    private InventoryCraftingForestry dimhoppertweaks$inheritStages(InventoryCrafting inventory,
            NonNullList<ItemStack> stacks, World world, IRecipe recipe,
            Operation<InventoryCraftingForestry> operation) {
        DelayedModAccess.inheritInventoryStages(this,inventory);
        return operation.call(inventory,stacks,world,recipe);
    }
}