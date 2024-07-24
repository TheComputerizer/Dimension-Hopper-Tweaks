package mods.thecomputerizer.dimhoppertweaks.mixin.mods.waystones;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.blay09.mods.waystones.PlayerWaystoneData;
import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.client.ClientProxy;
import net.blay09.mods.waystones.client.gui.GuiWaystoneList;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mixin(value = ClientProxy.class, remap = false)
public abstract class MixinClientProxy {
    
    /**
     * @author The_Computerizer
     * @reason Skip waystone entries that the player does not have the stage for
     */
    @Overwrite
    public void openWaystoneSelection(
            EntityPlayer player, WarpMode mode, EnumHand hand, @Nullable WaystoneEntry fromWaystone) {
        EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
        if(player==clientPlayer) {
            List<WaystoneEntry> waystones = new ArrayList<>();
            for(WaystoneEntry entry : PlayerWaystoneData.fromPlayer(clientPlayer).getWaystones())
                if(DelayedModAccess.canTravelToDimension(clientPlayer,entry.getDimensionId()))
                    waystones.add(entry);
            Minecraft.getMinecraft().displayGuiScreen(new GuiWaystoneList(
                    waystones.toArray(new WaystoneEntry[0]),mode,hand,fromWaystone));
        }
    }
}