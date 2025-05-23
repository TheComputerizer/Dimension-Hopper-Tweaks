package mods.thecomputerizer.dimhoppertweaks.mixin.mods.botania;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.botania.common.block.tile.TileCraftCrate;
import vazkii.botania.common.block.tile.TileOpenCrate;

@Mixin(value = TileCraftCrate.class, remap = false)
public abstract class MixinTileCraftCrate extends TileOpenCrate {

    @WrapOperation(at=@At(value="NEW",target="(Lnet/minecraft/inventory/Container;II)"+
            "Lnet/minecraft/inventory/InventoryCrafting;"),method="craft")
    private InventoryCrafting dimhoppertweaks$attachCraftingStages(Container container, int width, int height,
            Operation<InventoryCrafting> operation) {
        return DelayedModAccess.inheritInventoryStagesAndReturn(this,operation.call(container,width,height));
    }
}