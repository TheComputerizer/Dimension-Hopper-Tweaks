package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.ISPacketEntityEffect;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(EntityTrackerEntry.class)
public abstract class MixinEntityTrackerEntry {

    @Redirect(at = @At(value = "NEW", target = "(ILnet/minecraft/potion/PotionEffect;)" +
            "Lnet/minecraft/network/play/server/SPacketEntityEffect;"), method = "updatePlayerEntity")
    private SPacketEntityEffect dimhoppertweaks$catchErroredEntityEffectPacket(int entityID, PotionEffect effect) {
        try {
            return new SPacketEntityEffect(entityID,effect);
        } catch (NullPointerException ex) {
            DHTRef.LOGGER.error("Could not create entity effect packet for effect {}!",
                    Objects.nonNull(effect) ? effect.getPotion().getRegistryName() : "NULL EFFECT");
            return new SPacketEntityEffect();
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetHandlerPlayServer;" +
            "sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 6), method = "updatePlayerEntity")
    private void dimhoppertweaks$noSendingNullPotionEffectPackets(NetHandlerPlayServer network, Packet<?> packet) {
        if(!(packet instanceof SPacketEntityEffect) || ((ISPacketEntityEffect) packet).dimhoppertweaks$isNotErrored())
            network.sendPacket(packet);
    }
}