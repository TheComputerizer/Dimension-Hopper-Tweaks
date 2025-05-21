package mods.thecomputerizer.dimhoppertweaks.mixin.mods.appliedenergistics2;

import appeng.container.slot.AppEngCraftingSlot;
import appeng.container.slot.SlotCraftingTerm;
import com.llamalad7.mixinextras.sugar.Local;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = SlotCraftingTerm.class, remap = false)
public abstract class MixinSlotCraftingTerm extends AppEngCraftingSlot {
    
    public MixinSlotCraftingTerm(EntityPlayer player, IItemHandler matrix,
            IItemHandler output, int x, int y) {
        super(player,matrix,output,0,x,y);
    }
    
    @Redirect(at=@At(value="NEW",target="(Lnet/minecraft/inventory/Container;II)"+
                                "Lnet/minecraft/inventory/InventoryCrafting;",remap=true),method="craftItem")
    private InventoryCrafting dimhoppertweaks$inheritStages(Container container, int width, int height,
            @Local(argsOnly=true) EntityPlayer player) {
        InventoryCrafting inventory = new InventoryCrafting(container,width,height);
        DelayedModAccess.inheritInventoryStages(player,inventory);
        return inventory;
    }
}
