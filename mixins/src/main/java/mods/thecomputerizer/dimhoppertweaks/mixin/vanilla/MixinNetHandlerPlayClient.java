package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketOpenWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {
    
    @Inject(at = @At("TAIL"), method = "handleOpenWindow")
    private void dimhoppertweaks$handleOpenWindow(SPacketOpenWindow packet, CallbackInfo ci) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if(Objects.nonNull(player) && Objects.nonNull(player.openContainer))
            DelayedModAccess.inheritContainerStages(player);
    }
}