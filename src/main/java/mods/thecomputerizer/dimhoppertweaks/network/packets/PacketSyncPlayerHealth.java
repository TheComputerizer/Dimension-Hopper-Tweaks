package mods.thecomputerizer.dimhoppertweaks.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Objects;

public class PacketSyncPlayerHealth implements IMessageHandler<PacketSyncPlayerHealth.Message, IMessage> {

    @Override
    public IMessage onMessage(PacketSyncPlayerHealth.Message message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if(Objects.nonNull(player))
            player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(message.health);
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
