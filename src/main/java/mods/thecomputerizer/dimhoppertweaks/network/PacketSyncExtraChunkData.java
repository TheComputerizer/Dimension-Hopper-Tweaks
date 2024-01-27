package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncExtraChunkData extends MessageImpl {

    private int chunkX;
    private int chunkZ;
    private boolean isFast;

    public PacketSyncExtraChunkData() {}

    public PacketSyncExtraChunkData(int chunkX, int chunkZ, boolean isFast) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.isFast = isFast;
    }

    @Override
    public IMessage handle(MessageContext ctx) {
        ClientPacketHandlers.handleExtraChunkData(this.chunkX,this.chunkZ,this.isFast);
        return null;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.chunkX = buf.readInt();
        this.chunkZ = buf.readInt();
        this.isFast = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.chunkX);
        buf.writeInt(this.chunkZ);
        buf.writeBoolean(this.isFast);
    }
}
