package mods.thecomputerizer.dimhoppertweaks.mixin.mods.forestry;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import forestry.core.recipes.RecipeUtil;
import forestry.worktable.inventory.InventoryCraftingForestry;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = RecipeUtil.class,remap = false)
public abstract class MixinRecipeUtil {

    @WrapOperation(at=@At(value="NEW", target="()Lforestry/worktable/inventory/InventoryCraftingForestry;"),
            method="getCraftRecipe")
    private static InventoryCraftingForestry dimhoppertweaks$inheritStages(
            Operation<InventoryCraftingForestry> operation, @Local(argsOnly=true)InventoryCrafting inventory) {
        return DelayedModAccess.inheritInventoryStagesAndReturn(inventory,operation.call());
    }
}