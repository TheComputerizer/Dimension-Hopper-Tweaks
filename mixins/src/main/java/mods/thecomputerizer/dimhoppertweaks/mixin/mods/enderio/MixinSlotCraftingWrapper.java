package mods.thecomputerizer.dimhoppertweaks.mixin.mods.enderio;

import crazypants.enderio.invpanel.util.SlotCraftingWrapper;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;

@Mixin(value = SlotCraftingWrapper.class, remap = false)
public abstract class MixinSlotCraftingWrapper extends SlotCrafting {

    public MixinSlotCraftingWrapper(@Nonnull EntityPlayer player, @Nonnull InventoryCrafting crafting,
            @Nonnull IInventory inventory, int index, int x, int y) {
        super(player,crafting,inventory,index,x,y);
    }

    @Inject(at=@At("TAIL"),method="<init>")
    private void dimhoppertweaks$inheritStages(EntityPlayer player, InventoryCrafting crafting, IInventory inventory,
            int index, int x, int y, CallbackInfo ci) {
        DelayedModAccess.inheritInventoryStages(player,crafting);
    }
}