package mods.thecomputerizer.dimhoppertweaks.mixin.mods.appliedenergistics2;

import appeng.container.implementations.ContainerMEPortableTerminal;
import appeng.container.implementations.ContainerWirelessCraftingTerminal;
import appeng.helpers.IContainerCraftingPacket;
import appeng.helpers.WirelessTerminalGuiObject;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ContainerWirelessCraftingTerminal.class, remap = false)
public abstract class MixinContainerWirelessCraftingTerminal extends ContainerMEPortableTerminal
        implements IContainerCraftingPacket {
    
    public MixinContainerWirelessCraftingTerminal(InventoryPlayer ip, WirelessTerminalGuiObject object) {
        super(ip,object,false);
    }
    
    @Redirect(at=@At(value="NEW",target="(Lnet/minecraft/inventory/Container;II)"+
            "Lnet/minecraft/inventory/InventoryCrafting;"), method = "onCraftMatrixChanged")
    private InventoryCrafting dimhoppertweaks$inheritStages(Container eventHandler, int width, int height) {
        InventoryCrafting inventory = new InventoryCrafting(eventHandler,width,height);
        DelayedModAccess.inheritInventoryStages(getInventoryPlayer().player,inventory);
        return inventory;
    }
}
