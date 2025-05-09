package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipes.class)
public class MixinShapedRecipes {
    
    @Inject(at = @At("RETURN"), method = "matches", cancellable = true)
    private void dimhoppertweaks$matches(InventoryCrafting inv, World world, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValueZ() && DelayedModAccess.canCraft(inv));
    }
    
    @Inject(at = @At("RETURN"), method = "getCraftingResult", cancellable = true)
    private void dimhoppertweaks$getCraftingResult(InventoryCrafting inv, CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(DelayedModAccess.getCraftingResult(inv,cir.getReturnValue()));
    }
}
