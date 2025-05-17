package mods.thecomputerizer.dimhoppertweaks.mixin.mods.forestry;

import forestry.worktable.gui.ContainerWorktable;
import forestry.worktable.inventory.InventoryCraftingForestry;
import forestry.worktable.tiles.TileWorktable;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ContainerWorktable.class, remap = false)
public class MixinContainerWorktable {
    
    @Shadow @Final private InventoryCraftingForestry craftMatrix;
    
    @Inject(at=@At("TAIL"),method="<init>")
    private void dimhoppertweaks$inheritStages(EntityPlayer player, TileWorktable tile, CallbackInfo ci) {
        DelayedModAccess.inheritInventoryStages(tile,this.craftMatrix);
    }
}