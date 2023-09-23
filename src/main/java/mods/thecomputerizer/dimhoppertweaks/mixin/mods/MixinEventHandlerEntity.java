package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.event.EventHandlerEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Objects;

@Mixin(value = EventHandlerEntity.class, remap = false)
public abstract class MixinEventHandlerEntity {

    @Shadow protected abstract void syncSlot(EntityPlayer player, int slot, ItemStack stack, Collection<? extends EntityPlayer> receivers);

    @Shadow protected abstract void syncBaubles(EntityPlayer player, IBaublesItemHandler baubles);

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

    /**
     * @author The_Computerizer
     * @reason Handle null BaublesApi#getBaublesHandler return
     */
    @SubscribeEvent
    @Overwrite
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase==TickEvent.Phase.END) {
            EntityPlayer player = event.player;
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
            if(Objects.nonNull(baubles) && Objects.nonNull(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE)) {
                for(int i=0; i<baubles.getSlots(); ++i) {
                    ItemStack stack = baubles.getStackInSlot(i);
                    IBauble bauble = stack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE,null);
                    if(Objects.nonNull(bauble)) bauble.onWornTick(stack,player);
                }
                if(!player.world.isRemote) this.syncBaubles(player,baubles);
            }
        }

    }
}
