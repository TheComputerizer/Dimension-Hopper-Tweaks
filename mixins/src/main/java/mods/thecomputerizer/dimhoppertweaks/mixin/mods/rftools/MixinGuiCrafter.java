package mods.thecomputerizer.dimhoppertweaks.mixin.mods.rftools;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mcjty.lib.container.GenericContainer;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.rftools.blocks.crafter.CrafterBaseTE;
import mcjty.rftools.blocks.crafter.GuiCrafter;
import mcjty.rftools.craftinggrid.CraftingRecipe;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static mcjty.rftools.RFTools.instance;
import static mcjty.rftools.network.RFToolsMessages.INSTANCE;
import static mcjty.rftools.setup.GuiProxy.GUI_MANUAL_MAIN;

@Mixin(value = GuiCrafter.class, remap = false)
public abstract class MixinGuiCrafter extends GenericGuiContainer<CrafterBaseTE> {

    public MixinGuiCrafter(CrafterBaseTE tile, GenericContainer container) {
        super(instance,INSTANCE,tile,container,GUI_MANUAL_MAIN,"crafter");
    }

    @WrapOperation(at=@At(value="NEW",target="(Lnet/minecraft/inventory/Container;II)" +
            "Lnet/minecraft/inventory/InventoryCrafting;",remap=true),method="testRecipe")
    private InventoryCrafting dimhoppertweaks$inheritStages(Container container, int width, int height,
            Operation<InventoryCrafting> operation) {
        return DelayedModAccess.inheritInventoryStagesAndReturn(this.tileEntity,operation.call(container,width,height));
    }

    @WrapOperation(at=@At(value="INVOKE",target="Lmcjty/rftools/craftinggrid/CraftingRecipe;getInventory()" +
            "Lnet/minecraft/inventory/InventoryCrafting;"),method="applyRecipe")
    private InventoryCrafting dimhoppertweaks$inheritStages(CraftingRecipe recipe,
            Operation<InventoryCrafting> operation) {
        return DelayedModAccess.inheritInventoryStagesAndReturn(this.tileEntity,operation.call(recipe));
    }
}