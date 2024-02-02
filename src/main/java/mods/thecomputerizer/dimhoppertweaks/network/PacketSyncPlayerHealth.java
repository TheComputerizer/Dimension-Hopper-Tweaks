package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncPlayerHealth extends MessageImpl {

    private boolean isActive;

    public PacketSyncPlayerHealth() {}

    public PacketSyncPlayerHealth(boolean isActive) {
        this.isActive = isActive;
    }


    @Override
    public IMessage handle(MessageContext ctx) {
        ClientPacketHandlers.handleSyncPlayerHealth(this.isActive);
        return null;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.isActive = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.isActive);
    }
}
