package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.brandon3055.draconicevolution.handlers.CustomArmorHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Objects;

@Mixin(value = CustomArmorHandler.ArmorSummery.class, remap = false)
public class MixinArmorSummery {

    @Shadow public NonNullList<ItemStack> baublesStacks;

    /**
     * @author The_Computerizer
     * @reason Fix null capability issues
     */
    @Overwrite
    private void getBaubles(EntityPlayer player, List<ItemStack> stacks) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
        if(Objects.nonNull(baubles)) {
            this.baublesStacks = NonNullList.withSize(baubles.getSlots(), ItemStack.EMPTY);
            for (int i = 0; i < baubles.getSlots(); ++i)
                this.baublesStacks.set(i,baubles.getStackInSlot(i).copy());
            stacks.addAll(this.baublesStacks);
        } else this.baublesStacks = NonNullList.withSize(0, ItemStack.EMPTY);
    }
}
