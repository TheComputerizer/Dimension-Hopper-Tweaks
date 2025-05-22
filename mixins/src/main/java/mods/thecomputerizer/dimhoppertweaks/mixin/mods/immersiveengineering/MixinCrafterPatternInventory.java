package mods.thecomputerizer.dimhoppertweaks.mixin.mods.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityAssembler;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityAssembler.CrafterPatternInventory;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = CrafterPatternInventory.class, remap = false)
public abstract class MixinCrafterPatternInventory {
    
    @Shadow @Final TileEntityAssembler tile;
    
    @WrapOperation(at=@At(value="INVOKE",target=
            "Lblusunrize/immersiveengineering/common/util/Utils$InventoryCraftingFalse;createFilledCraftingInventory("+
                    "IILnet/minecraft/util/NonNullList;)Lnet/minecraft/inventory/InventoryCrafting;"),
            method="recalculateOutput")
    private InventoryCrafting dimhoppertweaks$inheritStages(int j, int w, NonNullList<ItemStack> h,
            Operation<InventoryCrafting> original) {
        return DelayedModAccess.inheritInventoryStagesAndReturn(this.tile,original.call(j,j,h));
    }
}