package mods.thecomputerizer.dimensionhoppertweaks.network.packets;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimensionhoppertweaks.client.entity.render.RenderEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRenderBossAttack implements IMessageHandler<PacketRenderBossAttack.PacketRenderBossAttackMessage, IMessage> {

    @Override
    public IMessage onMessage(PacketRenderBossAttack.PacketRenderBossAttackMessage message, MessageContext ctx) {
        RenderEvents.attacks.put(message.pos,message.start);
        RenderEvents.attackSize.put(message.pos,message.size);
        return null;
    }

    public static class PacketRenderBossAttackMessage implements IMessage {
        BlockPos pos;
        int start;
        int size;

        public PacketRenderBossAttackMessage() {
        }

        public PacketRenderBossAttackMessage(BlockPos pos, int start, int size) {
            this.pos = pos;
            this.start = start;
            this.size = size;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.pos = BlockPos.fromLong(buf.readLong());
            this.start = buf.readInt();
            this.size = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeLong(this.pos.toLong());
            buf.writeInt(this.start);
            buf.writeInt(this.size);
        }
    }
}
