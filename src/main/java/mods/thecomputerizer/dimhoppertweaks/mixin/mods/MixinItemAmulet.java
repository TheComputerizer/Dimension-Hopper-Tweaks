package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import baubles.api.BaublesApi;
import com.teammetallurgy.atum.items.ItemAmulet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = ItemAmulet.class, remap = false)
public abstract class MixinItemAmulet {

    @Shadow protected static IInventory getBaublesInventory(EntityPlayer player) {
        return null;
    }

    /**
     * @author The_Computerizer
     * @reason Fix null capabilities
     */
    @Overwrite
    @Optional.Method(modid = "baubles")
    public static ItemStack getAmulet(EntityPlayer player) {
        if(Objects.isNull(player)) return ItemStack.EMPTY;
        IInventory inventory = getBaublesInventory(player);
        if(Objects.isNull(inventory)) return ItemStack.EMPTY;
        return inventory.getStackInSlot(0);
    }
    @Inject(at = @At("HEAD"), method = "getBaublesInventory", cancellable = true)
    private static void dimhoppertweaks$getBaublesInventory(EntityPlayer player, CallbackInfoReturnable<IInventory> cir) {
        if(Objects.isNull(BaublesApi.getBaublesHandler(player))) cir.setReturnValue(null);
    }
}
