package mods.thecomputerizer.dimhoppertweaks.network.packets;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimhoppertweaks.network.ClientPacketHandlers;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketBossClientEffects implements IMessageHandler<PacketBossClientEffects.Message, IMessage> {

    @Override
    public IMessage onMessage(PacketBossClientEffects.Message message, MessageContext ctx) {
        ClientPacketHandlers.handleBossClientEffects();
        return null;
    }

    public static class Message implements IMessage {
        private boolean setLimboSky;
        private float screenShake;

        public Message() {}

        public Message(boolean limboify, float screenShake) {
            this.setLimboSky = limboify;
            this.screenShake = screenShake;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.setLimboSky = buf.readBoolean();
            this.screenShake = buf.readFloat();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeBoolean(this.setLimboSky);
            buf.writeFloat(this.screenShake);
        }
    }
}
