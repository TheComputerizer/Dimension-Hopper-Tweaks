package mods.thecomputerizer.dimhoppertweaks.network.packets;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimhoppertweaks.client.entity.render.RenderDelayedAOE;
import mods.thecomputerizer.dimhoppertweaks.client.entity.render.RenderEvents;
import mods.thecomputerizer.dimhoppertweaks.common.objects.DimensionHopperSounds;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class PacketRenderBossAttack implements IMessageHandler<PacketRenderBossAttack.Message, IMessage> {

    @Override
    public IMessage onMessage(Message message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        if(message.start>0) {
            Entity entity = mc.player.world.getEntityByID(message.bossID);
            if(entity instanceof EntityFinalBoss) {
                EntityFinalBoss boss = (EntityFinalBoss)entity;
                for (Vec3d posVec : message.vecList)
                    RenderEvents.ATTACKS.add(new RenderDelayedAOE(posVec,message.start,message.size,boss, message.phase));
            }
        } else mc.getSoundHandler().playSound(PositionedSoundRecord.getMusicRecord(DimensionHopperSounds.MUSIC));
        return null;
    }

    public static class Message implements IMessage {
        private List<Vec3d> vecList;
        private int start;
        private int size;
        private int bossID;
        private int phase;

        public Message() {}

        public Message(List<Vec3d> vecList, int start, int size, int bossID, int phase) {
            this.vecList = vecList;
            this.start = start;
            this.size = size;
            this.bossID = bossID;
            this.phase = phase;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.vecList = NetworkUtil.readGenericList(buf,this::readVec);
            this.start = buf.readInt();
            this.size = buf.readInt();
            this.bossID = buf.readInt();
            this.phase = buf.readInt();
        }

        private Vec3d readVec(ByteBuf buf) {
            return new Vec3d(buf.readDouble(),buf.readDouble(),buf.readDouble());
        }

        @Override
        public void toBytes(ByteBuf buf) {
            NetworkUtil.writeGenericList(buf,this.vecList,this::writeVec);
            buf.writeInt(this.start);
            buf.writeInt(this.size);
            buf.writeInt(this.bossID);
            buf.writeInt(this.phase);
        }

        private void writeVec(ByteBuf buf, Vec3d vec) {
            buf.writeDouble(vec.x);
            buf.writeDouble(vec.y);
            buf.writeDouble(vec.z);
        }
    }
}
