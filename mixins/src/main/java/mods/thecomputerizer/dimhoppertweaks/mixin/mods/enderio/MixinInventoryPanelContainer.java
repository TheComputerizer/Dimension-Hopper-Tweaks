package mods.thecomputerizer.dimhoppertweaks.mixin.mods.enderio;

import crazypants.enderio.base.invpanel.database.IChangeLog;
import crazypants.enderio.base.machine.gui.AbstractMachineContainer;
import crazypants.enderio.invpanel.invpanel.InventoryPanelContainer;
import crazypants.enderio.invpanel.invpanel.TileInventoryPanel;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;

@Mixin(value = InventoryPanelContainer.class, remap = false)
public abstract class MixinInventoryPanelContainer extends AbstractMachineContainer<TileInventoryPanel>
        implements IChangeLog {
    
    public MixinInventoryPanelContainer(@Nonnull InventoryPlayer playerInv, @Nonnull TileInventoryPanel tile) {
        super(playerInv,tile);
    }
    
    @Inject(at=@At("TAIL"),method="<init>")
    private void dimhoppertweaks$inheritStages(InventoryPlayer player, TileInventoryPanel tile, CallbackInfo ci) {
        DelayedModAccess.inheritPlayerStages(player.player,tile);
        DelayedModAccess.inheritContainerStages(player.player,this);
    }
}
