package mods.thecomputerizer.dimhoppertweaks.mixin.mods.translocators;

import codechicken.translocators.tile.TileCraftingGrid;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileCraftingGrid.class, remap = false)
public abstract class MixinTileCraftingGrid extends TileEntity {

    @Inject(at=@At("TAIL"),method="onPlaced")
    private void dimhoppertweaks$inheritStages(EntityLivingBase entity, CallbackInfo ci) {
        DelayedModAccess.setTileStages(this,DelayedModAccess.getEntityStages(entity));
    }

    @WrapOperation(at=@At(value="NEW",target="(Lnet/minecraft/inventory/Container;II)"+
            "Lnet/minecraft/inventory/InventoryCrafting;",remap=true),method="getCraftMatrix")
    private InventoryCrafting dimhoppertweaks$setTileStages(Container container, int width, int height,
            Operation<InventoryCrafting> operation) {
        return DelayedModAccess.inheritInventoryStagesAndReturn(this,operation.call(container,width,height));
    }

    @Inject(at=@At("HEAD"),method="doCraft",cancellable=true)
    private void dimhoppertweaks$handleEmptyResult(ItemStack result, InventoryCrafting matrix,
            EntityPlayer player, CallbackInfo ci) {
        if(result.isEmpty()) ci.cancel();
    }
}