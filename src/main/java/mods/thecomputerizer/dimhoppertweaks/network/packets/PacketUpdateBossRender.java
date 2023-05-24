package mods.thecomputerizer.dimhoppertweaks.network.packets;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimhoppertweaks.network.ClientPacketHandlers;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateBossRender implements IMessageHandler<PacketUpdateBossRender.Message, IMessage> {

    @Override
    public IMessage onMessage(Message message, MessageContext ctx) {
        ClientPacketHandlers.handleUpdateBossRender(message.bossID, message.phase, message.isShieldUp,
                message.animationState, message.projectileChargeTime);
        return null;
    }

    public static class Message implements IMessage {
        private int bossID;
        private int phase;
        private boolean isShieldUp;
        private String animationState;
        private int projectileChargeTime;

        public Message() {
        }

        public Message(int bossID, int phase, boolean shield, String animation, int projectileCharge) {
            this.bossID = bossID;
            this.phase = phase;
            this.isShieldUp = shield;
            this.animationState = animation;
            this.projectileChargeTime = projectileCharge;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.bossID = buf.readInt();
            this.phase = buf.readInt();
            this.isShieldUp = buf.readBoolean();
            this.animationState = NetworkUtil.readString(buf);
            this.projectileChargeTime = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(this.bossID);
            buf.writeInt(this.phase);
            buf.writeBoolean(this.isShieldUp);
            NetworkUtil.writeString(buf,this.animationState);
            buf.writeInt(this.projectileChargeTime);
        }
    }
}
