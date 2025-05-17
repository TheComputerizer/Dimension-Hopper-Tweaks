package mods.thecomputerizer.dimhoppertweaks.mixin.mods.calculator;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sonar.calculator.mod.common.containers.ContainerCraftingCalculator;
import sonar.core.handlers.inventories.containers.ContainerSonar;
import sonar.core.handlers.inventories.slots.InventoryStoredCrafting;

import javax.annotation.Nonnull;

@Mixin(value = ContainerCraftingCalculator.class, remap = false)
public abstract class MixinContainerCraftingCalculator extends ContainerSonar {
    
    @Shadow(remap=true) public EntityPlayer player;
    
    @Shadow public InventoryStoredCrafting craftMatrix;
    
    @Shadow(remap=true) public abstract void onCraftMatrixChanged(@Nonnull IInventory inv);
    
    @SuppressWarnings("RedundantCast")
    @Redirect(at=@At(value="INVOKE",target="Lsonar/calculator/mod/common/containers/ContainerCraftingCalculator;" +
            "onCraftMatrixChanged(Lnet/minecraft/inventory/IInventory;)V",remap=true),method="<init>")
    private void dimhoppertweaks$inheritStages(ContainerCraftingCalculator instance, IInventory inv) {
        DelayedModAccess.inheritInventoryStages(this.player,this.craftMatrix);
        onCraftMatrixChanged((IInventory)null);
    }
}