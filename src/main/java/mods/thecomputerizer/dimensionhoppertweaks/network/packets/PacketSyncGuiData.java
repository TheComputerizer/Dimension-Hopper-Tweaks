package mods.thecomputerizer.dimensionhoppertweaks.network.packets;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimensionhoppertweaks.common.skills.Events;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PacketSyncGuiData implements IMessageHandler<PacketSyncGuiData.PacketSyncGuiDataMessage, IMessage> {

    @Override
    public IMessage onMessage(PacketSyncGuiData.PacketSyncGuiDataMessage message, MessageContext ctx) {
        Events.updateTokenDrainValues(message.skill, message.level,
                FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(message.playerUUID));
        return null;
    }

    public static class PacketSyncGuiDataMessage implements IMessage {

        private String skill;
        private int level;
        private UUID playerUUID;

        public PacketSyncGuiDataMessage() {
        }

        public PacketSyncGuiDataMessage(String skill, int level, UUID player) {
            this.skill = skill;
            this.level = level;
            this.playerUUID = player;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.skill = (String) buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8);
            this.level = buf.readInt();
            this.playerUUID = UUID.fromString((String) buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8));
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(this.skill.length());
            buf.writeCharSequence(this.skill, StandardCharsets.UTF_8);
            buf.writeInt(this.level);
            String uuidString = this.playerUUID.toString();
            buf.writeInt(uuidString.length());
            buf.writeCharSequence(uuidString, StandardCharsets.UTF_8);
        }
    }
}