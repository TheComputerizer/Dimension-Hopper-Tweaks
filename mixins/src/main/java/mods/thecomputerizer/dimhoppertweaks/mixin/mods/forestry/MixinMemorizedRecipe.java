package mods.thecomputerizer.dimhoppertweaks.mixin.mods.forestry;

import forestry.api.core.INbtReadable;
import forestry.api.core.INbtWritable;
import forestry.core.network.IStreamable;
import forestry.worktable.inventory.InventoryCraftingForestry;
import forestry.worktable.recipes.MemorizedRecipe;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.item.crafting.IRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;

@Mixin(value = MemorizedRecipe.class, remap = false)
public abstract class MixinMemorizedRecipe implements INbtWritable, INbtReadable, IStreamable {

    @Shadow private InventoryCraftingForestry craftMatrix;

    @Inject(at=@At("TAIL"),method="<init>(Lforestry/worktable/inventory/InventoryCraftingForestry;Ljava/util/List;)V")
    private void dimhoppertweaks$inheritStages(InventoryCraftingForestry matrix, List<IRecipe> recipes, CallbackInfo ci) {
        Collection<String> stages = DelayedModAccess.getInventoryStages(matrix);
        if(!stages.isEmpty()) DelayedModAccess.setInventoryStages(this.craftMatrix,stages);
    }

    @Inject(at=@At("HEAD"),method="setCraftMatrix")
    private void dimhoppertweaks$inheritStages(InventoryCraftingForestry matrix, CallbackInfo ci) {
        Collection<String> stages = DelayedModAccess.getInventoryStages(this.craftMatrix);
        if(!stages.isEmpty()) DelayedModAccess.setInventoryStages(matrix,stages);
    }
}