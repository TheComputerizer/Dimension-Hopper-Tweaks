package mods.thecomputerizer.dimhoppertweaks.mixin.mods.forestry;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import forestry.worktable.inventory.InventoryCraftingForestry;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = InventoryCraftingForestry.class, remap = false)
public abstract class MixinInventoryCraftingForestry extends InventoryCrafting {

    private MixinInventoryCraftingForestry(Container container, int width, int height) {
        super(container,width,height);
    }

    @WrapOperation(at=@At(value="NEW", target="(Lnet/minecraft/inventory/Container;II)" +
            "Lforestry/worktable/inventory/InventoryCraftingForestry;"),method="copy")
    private InventoryCraftingForestry dimhoppertweaks$copyStages(Container container, int width, int height,
            Operation<InventoryCraftingForestry> operation) {
        return DelayedModAccess.inheritInventoryStagesAndReturn(this,operation.call(container,width,height));
    }
}