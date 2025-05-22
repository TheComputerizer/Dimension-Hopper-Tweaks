package mods.thecomputerizer.dimhoppertweaks.mixin.mods.forestry;

import forestry.core.gui.ContainerTile;
import forestry.core.gui.IContainerCrafting;
import forestry.core.gui.IGuiSelectable;
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
public abstract class MixinContainerWorktable extends ContainerTile<TileWorktable> implements IContainerCrafting, IGuiSelectable {
    
    @Shadow @Final private InventoryCraftingForestry craftMatrix;

    @SuppressWarnings("unused")
    public MixinContainerWorktable(EntityPlayer player, TileWorktable tile) {
        super(tile);
    }
    
    @Inject(at=@At("TAIL"),method="<init>")
    private void dimhoppertweaks$inheritStages(EntityPlayer player, TileWorktable tile, CallbackInfo ci) {
        DelayedModAccess.setTileStages(tile,DelayedModAccess.getPlayerStages(player));
        DelayedModAccess.inheritInventoryStages(player,this.craftMatrix);
    }
}