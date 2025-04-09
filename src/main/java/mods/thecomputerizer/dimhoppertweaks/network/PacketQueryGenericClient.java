package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.api.network.NetworkHelper;
import mods.thecomputerizer.theimpossiblelibrary.api.network.message.MessageAPI;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketQueryGenericClient extends MessageAPI<MessageContext> {

    private final String type;

    public PacketQueryGenericClient(String type) {
        this.type = type;
    }
    
    public PacketQueryGenericClient(ByteBuf buf) {
        this.type = NetworkHelper.readString(buf);
    }

    @Override public void encode(ByteBuf buf) {
        NetworkHelper.writeString(buf,this.type);
    }
    
    @Override public MessageAPI<MessageContext> handle(MessageContext ctx) {
        ClientPacketHandlers.handleGenericClientQuery(this.type);
        return null;
    }
}