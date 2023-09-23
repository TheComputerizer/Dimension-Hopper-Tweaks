package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.event.EventHandlerEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Objects;

@Mixin(value = EventHandlerEntity.class, remap = false)
public abstract class MixinEventHandlerEntity {

    @Shadow protected abstract void syncSlot(EntityPlayer player, int slot, ItemStack stack, Collection<? extends EntityPlayer> receivers);

    /**
     * @author The_Computerizer
     * @reason Handle null BaublesApi#getBaublesHandler return
     */
    @Overwrite
    private void syncSlots(EntityPlayer player, Collection<? extends EntityPlayer> receivers) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
        if(Objects.nonNull(baubles))
            for(int i=0; i<baubles.getSlots(); ++i)
                this.syncSlot(player,i,baubles.getStackInSlot(i),receivers);
    }
}
