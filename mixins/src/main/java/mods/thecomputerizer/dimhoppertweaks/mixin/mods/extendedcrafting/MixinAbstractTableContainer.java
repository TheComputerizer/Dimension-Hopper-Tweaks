package mods.thecomputerizer.dimhoppertweaks.mixin.mods.extendedcrafting;

import com.blakebr0.extendedcrafting.client.container.AbstractTableContainer;
import com.blakebr0.extendedcrafting.tile.AbstractExtendedTable;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractTableContainer.class, remap = false)
public abstract class MixinAbstractTableContainer extends Container {
    
    @Shadow @Final public InventoryCrafting matrix;
    
    @Inject(at=@At("TAIL"),method="<init>")
    private void dimhoppertweaks$inheritStages(InventoryPlayer player, AbstractExtendedTable tile, int guiWidth,
            int guiHeight, int gridStartX, int outputSlotX, CallbackInfo ci) {
        DelayedModAccess.inheritInventoryStages(tile,this.matrix);
    }
}
