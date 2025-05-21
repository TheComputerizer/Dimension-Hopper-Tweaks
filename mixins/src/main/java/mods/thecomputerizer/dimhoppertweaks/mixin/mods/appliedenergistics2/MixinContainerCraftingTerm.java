package mods.thecomputerizer.dimhoppertweaks.mixin.mods.appliedenergistics2;

import appeng.api.storage.ITerminalHost;
import appeng.container.implementations.ContainerCraftingTerm;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.helpers.IContainerCraftingPacket;
import appeng.util.inv.IAEAppEngInventory;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ContainerCraftingTerm.class, remap = false)
public abstract class MixinContainerCraftingTerm extends ContainerMEMonitorable implements IAEAppEngInventory,
        IContainerCraftingPacket {
    
    public MixinContainerCraftingTerm(InventoryPlayer player, ITerminalHost host) {
        super(player,host);
    }
    
    @Redirect(at=@At(value="NEW",target="(Lnet/minecraft/inventory/Container;II)"+
            "Lnet/minecraft/inventory/InventoryCrafting;"),method="onCraftMatrixChanged",remap=true)
    private InventoryCrafting dimhoppertweaks$inheritStages(Container container, int width, int height) {
        InventoryCrafting inventory = new InventoryCrafting(container,width,height);
        EntityPlayer player = getInventoryPlayer().player;
        DHTRef.LOGGER.info("Terminal player: {}", player.getName());
        DelayedModAccess.inheritInventoryStages(player,inventory);
        return inventory;
    }
}
