package mods.thecomputerizer.dimhoppertweaks.mixin.mods.botania;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IInventoryCrafting;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.ITileEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.block.tile.TileCraftCrate;

@Mixin(value = TileCraftCrate.class, remap = false)
public abstract class MixinTileCraftCrate {

    @Unique
    private TileEntity dimhoppertweaks$cast() {
        return (TileEntity)(Object)this;
    }

    @Redirect(at = @At(value = "NEW", target = "(Lnet/minecraft/inventory/Container;II)"+
            "Lnet/minecraft/inventory/InventoryCrafting;"), method = "craft")
    private InventoryCrafting dimhoppertweaks$attachCraftingStages(Container container, int width, int height) {
        InventoryCrafting inv = new InventoryCrafting(container,width,height);
        ((IInventoryCrafting)inv).dimhoppertweaks$setStages(((ITileEntity)dimhoppertweaks$cast()).dimhoppertweaks$getStages());
        return null;
    }
}