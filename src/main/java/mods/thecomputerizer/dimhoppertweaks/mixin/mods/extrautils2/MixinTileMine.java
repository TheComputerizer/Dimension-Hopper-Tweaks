package mods.thecomputerizer.dimhoppertweaks.mixin.mods.extrautils2;

import com.rwtema.extrautils2.tile.TileMine;
import de.ellpeck.actuallyadditions.mod.items.InitItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileMine.class, remap = false)
public abstract class MixinTileMine {

    @Redirect(at = @At(value = "NEW", target = "(Lnet/minecraft/item/Item;I)Lnet/minecraft/item/ItemStack;"),
            method = "<clinit>")
    private static ItemStack dimhoppertweaks$reassignMiningTool(Item item, int amount) {
        return new ItemStack(InitItems.itemPaxelCrystalBlue,amount);
    }
}