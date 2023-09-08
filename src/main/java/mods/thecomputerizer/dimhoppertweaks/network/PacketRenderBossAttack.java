package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class PacketRenderBossAttack extends MessageImpl {

    private List<Vec3d> vecList;
    private int start;
    private int size;
    private int bossID;
    private int phase;

    public PacketRenderBossAttack() {}

    public PacketRenderBossAttack(List<Vec3d> vecList, int start, int size, int bossID, int phase) {
        this.vecList = vecList;
        this.start = start;
        this.size = size;
        this.bossID = bossID;
        this.phase = phase;
    }

    @Override
    public IMessage handle(MessageContext messageContext) {
        ClientPacketHandlers.handleRenderBossAttack(this.start, this.bossID, this.vecList, this.size, this.phase);
        return null;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
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
