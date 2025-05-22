package mods.thecomputerizer.dimhoppertweaks.mixin.mods.appliedenergistics2;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.helpers.PatternHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PatternHelper.class, remap = false)
public abstract class MixinPatternHelper implements ICraftingPatternDetails, Comparable<PatternHelper> {

    @WrapOperation(at=@At(value="NEW",target="(Lnet/minecraft/inventory/Container;II)"+
            "Lnet/minecraft/inventory/InventoryCrafting;",remap=true),method="<init>")
    private InventoryCrafting dimhoppertweaks$retrieveStages(Container container, int width, int height,
            Operation<InventoryCrafting> operation, @Local(argsOnly=true)ItemStack stack) {
        return DelayedModAccess.inheritInventoryStagesAndReturn(stack,operation.call(container,width,height));
    }
}