package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketQueryGenericClient extends MessageImpl {

    private String type;

    public PacketQueryGenericClient() {}

    public PacketQueryGenericClient(String type) {
        this.type = type;
    }

    @Override
    public IMessage handle(MessageContext ctx) {
        ClientPacketHandlers.handleGenericClientQuery(this.type);
        return null;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.type = NetworkUtil.readString(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkUtil.writeString(buf,this.type);
    }
}
