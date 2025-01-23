package mods.thecomputerizer.dimhoppertweaks.mixin.mods.travelersbackpack;

import com.tiviacz.travelersbackpack.gui.container.ContainerTravelersBackpack;
import com.tiviacz.travelersbackpack.gui.inventory.InventoryCraftingImproved;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IInventoryCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ContainerTravelersBackpack.class, remap = false)
public abstract class MixinContainerTravelersBackpack {

    @Shadow public InventoryPlayer playerInv;

    @Shadow public InventoryCraftingImproved craftMatrix;

    @Inject(at = @At("HEAD"), method = "addCraftResult")
    private void dimhoppertweaks$syncCraft(EntityPlayer player, CallbackInfo ci) {
        ((IInventoryCrafting)this.craftMatrix).dimhoppertweaks$setStages(DelayedModAccess.getGameStages(player));
    }
    @Inject(at = @At("HEAD"), method = "onCraftMatrixChanged", remap = true)
    private void dimhoppertweaks$syncStages(IInventory inventoryIn, CallbackInfo ci) {
        ((IInventoryCrafting)this.craftMatrix).dimhoppertweaks$setStages(DelayedModAccess.getGameStages(this.playerInv.player));
    }

    @Inject(at = @At("HEAD"), method = "slotClick", remap = true)
    private void dimhoppertweaks$clickStages(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player,
                                             CallbackInfoReturnable<ItemStack> cir) {
        ((IInventoryCrafting)this.craftMatrix).dimhoppertweaks$setStages(DelayedModAccess.getGameStages(this.playerInv.player));
    }
}