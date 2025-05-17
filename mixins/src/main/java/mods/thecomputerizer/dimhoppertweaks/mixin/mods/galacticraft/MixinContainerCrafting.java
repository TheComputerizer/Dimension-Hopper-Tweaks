package mods.thecomputerizer.dimhoppertweaks.mixin.mods.galacticraft;

import com.llamalad7.mixinextras.sugar.Local;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCrafting;
import micdoodle8.mods.galacticraft.core.inventory.PersistantInventoryCrafting;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ContainerCrafting.class, remap = false)
public abstract class MixinContainerCrafting {
    
    @Shadow public PersistantInventoryCrafting craftMatrix;
    
    @Shadow public abstract void onCraftMatrixChanged(IInventory inventoryIn);
    
    @Redirect(at=@At(value="INVOKE",target="Lmicdoodle8/mods/galacticraft/core/inventory/ContainerCrafting;"+
                                           "onCraftMatrixChanged(Lnet/minecraft/inventory/IInventory;)V"),method="<init>")
    private void dimhoppertweaks$inheritStages(ContainerCrafting instance, IInventory inventory,
            @Local(argsOnly=true) InventoryPlayer player) {
        DelayedModAccess.inheritInventoryStages(player.player,this.craftMatrix);
        onCraftMatrixChanged(this.craftMatrix);
    }
}
