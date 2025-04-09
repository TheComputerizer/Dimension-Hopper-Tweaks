package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.api.network.message.MessageAPI;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncPlayerHealth extends MessageAPI<MessageContext> {

    private final boolean isActive;

    public PacketSyncPlayerHealth(boolean isActive) {
        this.isActive = isActive;
    }
    
    public PacketSyncPlayerHealth(ByteBuf buf) {
        this.isActive = buf.readBoolean();
    }

    @Override public void encode(ByteBuf buf) {
        buf.writeBoolean(this.isActive);
    }
    
    @Override public MessageAPI<MessageContext> handle(MessageContext ctx) {
        ClientPacketHandlers.handleSyncPlayerHealth(this.isActive);
        return null;
    }
}