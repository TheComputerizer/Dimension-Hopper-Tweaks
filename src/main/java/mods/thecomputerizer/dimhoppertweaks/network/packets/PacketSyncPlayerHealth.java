package mods.thecomputerizer.dimhoppertweaks.network.packets;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimhoppertweaks.network.ClientPacketHandlers;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncPlayerHealth implements IMessageHandler<PacketSyncPlayerHealth.Message, IMessage> {

    @Override
    public IMessage onMessage(PacketSyncPlayerHealth.Message message, MessageContext ctx) {
        ClientPacketHandlers.handleSyncPlayerHealth(message.health);
        return null;
    }

    public static class Message implements IMessage {
        private double health;
        public Message() {}

        public Message(double health) {
            this.health = health;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.health = buf.readDouble();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeDouble(this.health);
        }
    }
}
