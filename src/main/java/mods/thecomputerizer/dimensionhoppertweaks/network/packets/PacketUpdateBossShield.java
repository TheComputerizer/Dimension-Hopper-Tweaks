package mods.thecomputerizer.dimensionhoppertweaks.network.packets;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimensionhoppertweaks.client.entity.render.RenderEvents;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;

public class PacketUpdateBossShield implements IMessageHandler<PacketUpdateBossShield.PacketUpdateBossShieldMessage, IMessage> {

    @Override
    public IMessage onMessage(PacketUpdateBossShield.PacketUpdateBossShieldMessage message, MessageContext ctx) {
        if(message.getUUID()!=null) RenderEvents.bossShields.put(message.getUUID(), message.getShieldUp());
        return null;
    }

    public static class PacketUpdateBossShieldMessage implements IMessage {
        String playerUUID;
        boolean isShieldUp;

        public PacketUpdateBossShieldMessage() {
        }

        public PacketUpdateBossShieldMessage(String uuid, boolean shield) {
            this.playerUUID = uuid;
            this.isShieldUp = shield;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            int size = buf.readInt();
            this.playerUUID = (String) buf.readCharSequence(size, StandardCharsets.UTF_8);
            this.isShieldUp = buf.readBoolean();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(this.playerUUID.length());
            buf.writeCharSequence(this.playerUUID, StandardCharsets.UTF_8);
            buf.writeBoolean(this.isShieldUp);
        }

        public String getUUID() {
            return this.playerUUID;
        }

        public boolean getShieldUp() {
            return this.isShieldUp;
        }
    }
}
