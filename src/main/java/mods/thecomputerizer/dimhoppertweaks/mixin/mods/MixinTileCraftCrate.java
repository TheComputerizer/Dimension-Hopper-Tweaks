package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.InventoryCraftingAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.TileEntityAccess;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.block.tile.TileCraftCrate;

@Mixin(value = TileCraftCrate.class, remap = false)
public class MixinTileCraftCrate {

    @Unique private TileEntity dimhoppertweaks$cast() {
        return (TileEntity)(Object)this;
    }

    @Redirect(at = @At(value = "NEW", target = "(Lnet/minecraft/inventory/Container;II)"+
            "Lnet/minecraft/inventory/InventoryCrafting;"), method = "craft")
    private InventoryCrafting dimhoppertweaks$attachCraftingStages(Container container, int width, int height) {
        InventoryCrafting inv = new InventoryCrafting(container,width,height);
        ((InventoryCraftingAccess)inv).dimhoppertweaks$setStages(((TileEntityAccess)dimhoppertweaks$cast()).dimhoppertweaks$getStages());
        return null;
    }
}
