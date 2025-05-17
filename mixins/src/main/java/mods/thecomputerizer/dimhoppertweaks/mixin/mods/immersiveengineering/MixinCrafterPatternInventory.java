package mods.thecomputerizer.dimhoppertweaks.mixin.mods.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityAssembler;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityAssembler.CrafterPatternInventory;
import blusunrize.immersiveengineering.common.util.Utils.InventoryCraftingFalse;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = CrafterPatternInventory.class, remap = false)
public abstract class MixinCrafterPatternInventory {
    
    @Shadow @Final TileEntityAssembler tile;
    
    @Redirect(at=@At(value="INVOKE",target="Lblusunrize/immersiveengineering/common/util/Utils$InventoryCraftingFalse;"+
                   "createFilledCraftingInventory(IILnet/minecraft/util/NonNullList;)"+
                   "Lnet/minecraft/inventory/InventoryCrafting;",remap=true),method="recalculateOutput")
    private InventoryCrafting dimhoppertweaks$inheritStages(int j, int w, NonNullList<ItemStack> h) {
        InventoryCrafting inventory = InventoryCraftingFalse.createFilledCraftingInventory(j,j,h);
        DelayedModAccess.inheritInventoryStages(this.tile,inventory);
        return inventory;
    }
}