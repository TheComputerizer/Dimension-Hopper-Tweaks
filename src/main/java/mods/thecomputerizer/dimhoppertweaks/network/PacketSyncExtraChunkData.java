package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.api.network.message.MessageAPI;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncExtraChunkData extends MessageAPI<MessageContext> {

    private final int chunkX;
    private final int chunkZ;
    private final boolean isFast;

    public PacketSyncExtraChunkData(int chunkX, int chunkZ, boolean isFast) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.isFast = isFast;
    }
    
    public PacketSyncExtraChunkData(ByteBuf buf) {
        this.chunkX = buf.readInt();
        this.chunkZ = buf.readInt();
        this.isFast = buf.readBoolean();
    }

    @Override public void encode(ByteBuf buf) {
        buf.writeInt(this.chunkX);
        buf.writeInt(this.chunkZ);
        buf.writeBoolean(this.isFast);
    }
    
    @Override public MessageAPI<MessageContext> handle(MessageContext ctx) {
        ClientPacketHandlers.handleExtraChunkData(this.chunkX,this.chunkZ,this.isFast);
        return null;
    }
}