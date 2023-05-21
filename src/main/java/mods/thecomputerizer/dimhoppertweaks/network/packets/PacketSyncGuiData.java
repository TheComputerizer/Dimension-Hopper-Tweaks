package mods.thecomputerizer.dimhoppertweaks.network.packets;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PacketSyncGuiData implements IMessageHandler<PacketSyncGuiData.Message, IMessage> {

    @Override
    public IMessage onMessage(Message message, MessageContext ctx) {
        PlayerList players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
        EntityPlayerMP player = players.getPlayerByUUID(message.playerUUID);
        SkillWrapper.getSkillCapability(player).setDrainSelection(message.skill,message.level,player);
        return null;
    }

    public static class Message implements IMessage {

        private String skill;
        private int level;
        private UUID playerUUID;

        public Message() {
        }

        public Message(String skill, int level, UUID player) {
            this.skill = skill;
            this.level = level;
            this.playerUUID = player;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.skill = NetworkUtil.readString(buf);
            this.level = buf.readInt();
            this.playerUUID = UUID.fromString(NetworkUtil.readString(buf));
        }

        @Override
        public void toBytes(ByteBuf buf) {
            NetworkUtil.writeString(buf,this.skill);
            buf.writeInt(this.level);
            NetworkUtil.writeString(buf,this.playerUUID.toString());
        }
    }
}
