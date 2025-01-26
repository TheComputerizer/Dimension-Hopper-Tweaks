package mods.thecomputerizer.dimhoppertweaks.mixin.mods.forestry;

import forestry.core.recipes.RecipeUtil;
import forestry.core.tiles.TileBase;
import forestry.worktable.inventory.InventoryCraftingForestry;
import forestry.worktable.tiles.TileWorktable;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IInventoryCrafting;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.ITileEntity;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(value = TileWorktable.class, remap = false)
public abstract class MixinTileWorktable extends TileBase {

    @Redirect(at = @At(value = "INVOKE", target = "Lforestry/core/recipes/RecipeUtil;"+
            "getCraftRecipe(Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/util/NonNullList;"+
            "Lnet/minecraft/world/World;Lnet/minecraft/item/crafting/IRecipe;)"+
            "Lforestry/worktable/inventory/InventoryCraftingForestry;"), method = "craftRecipe")
    private InventoryCraftingForestry dimhoppertweaks$syncStages(
            InventoryCrafting equivalent, NonNullList<ItemStack> stacks, World world, IRecipe recipe) {
        TileWorktable instance = (TileWorktable)(Object)this;
        InventoryCraftingForestry inventory = RecipeUtil.getCraftRecipe(equivalent,stacks,world,recipe);
        if(Objects.nonNull(inventory))
            ((IInventoryCrafting)inventory).dimhoppertweaks$setStages(((ITileEntity)instance).dimhoppertweaks$getStages());
        return inventory;
    }
}