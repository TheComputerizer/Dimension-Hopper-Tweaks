package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.api.network.NetworkHelper;
import mods.thecomputerizer.theimpossiblelibrary.api.network.message.MessageAPI;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class PacketRenderBossAttack extends MessageAPI<MessageContext> {

    private final List<Vec3d> vecList;
    private final int start;
    private final int size;
    private final int bossID;
    private final int phase;

    public PacketRenderBossAttack(List<Vec3d> vecList, int start, int size, int bossID, int phase) {
        this.vecList = vecList;
        this.start = start;
        this.size = size;
        this.bossID = bossID;
        this.phase = phase;
    }
    
    public PacketRenderBossAttack(ByteBuf buf) {
        this.vecList = NetworkHelper.readList(buf,() -> readVec(buf));
        this.start = buf.readInt();
        this.size = buf.readInt();
        this.bossID = buf.readInt();
        this.phase = buf.readInt();
    }
    
    @Override public void encode(ByteBuf buf) {
        NetworkHelper.writeList(buf,this.vecList,v -> writeVec(buf,v));
        buf.writeInt(this.start);
        buf.writeInt(this.size);
        buf.writeInt(this.bossID);
        buf.writeInt(this.phase);
    }

    @Override public MessageAPI<MessageContext> handle(MessageContext ctx) {
        ClientPacketHandlers.handleRenderBossAttack(this.start,this.bossID,this.vecList,this.size,this.phase);
        return null;
    }

    private Vec3d readVec(ByteBuf buf) {
        return new Vec3d(buf.readDouble(),buf.readDouble(),buf.readDouble());
    }

    private void writeVec(ByteBuf buf, Vec3d vec) {
        buf.writeDouble(vec.x);
        buf.writeDouble(vec.y);
        buf.writeDouble(vec.z);
    }
}