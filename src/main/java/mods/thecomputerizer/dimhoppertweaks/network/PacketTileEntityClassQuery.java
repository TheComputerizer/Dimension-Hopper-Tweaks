package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.api.network.message.MessageAPI;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTileEntityClassQuery extends MessageAPI<MessageContext> {

    public PacketTileEntityClassQuery() {}
    
    public PacketTileEntityClassQuery(ByteBuf buf) {}
    
    @Override public void encode(ByteBuf buf) {}

    @Override public MessageAPI<MessageContext> handle(MessageContext ctx) {
        ClientPacketHandlers.handleTileEntityClassQuery();
        return null;
    }
}